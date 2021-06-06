package org.elegoff.plugins.rust.coverage.lcov;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.CheckForNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LCOVParser {

        private static final String SF = "SF:";
        private static final String DA = "DA:";
        private static final String BRDA = "BRDA:";

        private final Map<InputFile, NewCoverage> coverageByFile;
        private final SensorContext context;
        // deduplicated list of unresolved paths (keep order of insertion)
        private final Set<String> unresolvedPaths = new LinkedHashSet<>();
        private final FileChooser fileChooser;
        private int inconsistenciesCounter = 0;

        private static final Logger LOG = Loggers.get(LCOVParser.class);

        private LCOVParser(List<String> lines, SensorContext context, FileChooser fileChooser) {
            this.context = context;
            this.fileChooser = fileChooser;
            this.coverageByFile = parse(lines);
        }

        static LCOVParser create(SensorContext context, List<File> files, FileChooser fileChooser) {
            final List<String> lines = new LinkedList<>();
            for (File file : files) {
                try (Stream<String> fileLines = Files.lines(file.toPath())) {
                    lines.addAll(fileLines.collect(Collectors.toList()));
                } catch (IOException e) {
                    throw new IllegalArgumentException("Could not read content from file: " + file, e);
                }
            }
            return new LCOVParser(lines, context, fileChooser);
        }

        Map<InputFile, NewCoverage> coverageByFile() {
            return coverageByFile;
        }

        List<String> unresolvedPaths() {
            return new ArrayList<>(unresolvedPaths);
        }

        int inconsistenciesNumber() {
            return inconsistenciesCounter;
        }

        private Map<InputFile, NewCoverage> parse(List<String> lines) {
            final Map<InputFile, FileData> files = new HashMap<>();
            FileData fileData = null;
            int reportLineNum = 0;

            for (String line : lines) {
                reportLineNum++;
                if (line.startsWith(SF)) {
                    fileData = files.computeIfAbsent(inputFileForSourceFile(line),
                            inputFile -> inputFile == null ? null : new FileData(inputFile));

                } else if (fileData != null) {
                    if (line.startsWith(DA)) {
                        parseLineCoverage(fileData, reportLineNum, line);

                    } else if (line.startsWith(BRDA)) {
                        parseBranchCoverage(fileData, reportLineNum, line);
                    }
                }

            }

            Map<InputFile, NewCoverage> coveredFiles = new HashMap<>();

            for (Map.Entry<InputFile, FileData> e : files.entrySet()) {
                NewCoverage newCoverage = context.newCoverage().onFile(e.getKey());
                e.getValue().save(newCoverage);
                coveredFiles.put(e.getKey(), newCoverage);
            }
            return coveredFiles;
        }

        private void parseBranchCoverage(FileData fileData, int reportLineNum, String line) {
            try {
                // BRDA:<line number>,<block number>,<branch number>,<taken>
                String[] tokens = line.substring(BRDA.length()).trim().split(",");
                String lineNumber = tokens[0];
                String branchNumber = tokens[1] + tokens[2];
                String taken = tokens[3];

                fileData.addBranch(Integer.valueOf(lineNumber), branchNumber, "-".equals(taken) ? 0 : Integer.valueOf(taken));
            } catch (Exception e) {
                logWrongDataWarning("BRDA", reportLineNum, e);
            }
        }

        private void parseLineCoverage(FileData fileData, int reportLineNum, String line) {
            try {
                // DA:<line number>,<execution count>[,<checksum>]
                String execution = line.substring(DA.length());
                String executionCount = execution.substring(execution.indexOf(',') + 1);
                String lineNumber = execution.substring(0, execution.indexOf(','));

                fileData.addLine(Integer.valueOf(lineNumber), Integer.valueOf(executionCount));
            } catch (Exception e) {
                logWrongDataWarning("DA", reportLineNum, e);
            }
        }

        private void logWrongDataWarning(String dataType, int reportLineNum, Exception e) {
            LOG.debug(String.format("Problem during processing LCOV report: can't save %s data for line %s of coverage report file (%s).", dataType, reportLineNum, e.toString()));
            inconsistenciesCounter++;
        }

        @CheckForNull
        private InputFile inputFileForSourceFile(String line) {
            // SF:<absolute path to the source file>
            String filePath = line.substring(SF.length());
            // some tools (like Istanbul, Karma) provide relative paths, so let's consider them relative to project directory
            InputFile inputFile = context.fileSystem().inputFile(context.fileSystem().predicates().hasPath(filePath));
            if (inputFile == null) {
                inputFile = fileChooser.getInputFile(filePath);
            }
            if (inputFile == null) {
                unresolvedPaths.add(filePath);
            }
            return inputFile;
        }

private static class FileData {
    /**
     * line number -> branch number -> taken
     */
    private Map<Integer, Map<String, Integer>> branches = new HashMap<>();

    /**
     * line number -> execution count
     */
    private Map<Integer, Integer> hits = new HashMap<>();

    /**
     * Number of lines in the file
     * Required to check if line exist in a file, see {@link #checkLine(Integer)}
     */
    private final int linesInFile;

    private final String filename;
    private static final String WRONG_LINE_EXCEPTION_MESSAGE = "Line with number %s doesn't belong to file %s";

    FileData(InputFile inputFile) {
        linesInFile = inputFile.lines();
        filename = inputFile.filename();
    }

    void addBranch(Integer lineNumber, String branchNumber, Integer taken) {
        checkLine(lineNumber);
        Map<String, Integer> branchesForLine = branches.computeIfAbsent(lineNumber, l -> new HashMap<>());
        branchesForLine.merge(branchNumber, taken, Integer::sum);
    }

    void addLine(Integer lineNumber, Integer executionCount) {
        checkLine(lineNumber);
        hits.merge(lineNumber, executionCount, Integer::sum);
    }

    void save(NewCoverage newCoverage) {
        for (Map.Entry<Integer, Integer> e : hits.entrySet()) {
            newCoverage.lineHits(e.getKey(), e.getValue());
        }
        for (Map.Entry<Integer, Map<String, Integer>> e : branches.entrySet()) {
            int line = e.getKey();
            int conditions = e.getValue().size();
            int covered = 0;
            for (Integer taken : e.getValue().values()) {
                if (taken > 0) {
                    covered++;
                }
            }

            newCoverage.conditions(line, conditions, covered);
            newCoverage.lineHits(line, hits.getOrDefault(line, 0) + covered);
        }
    }

    private void checkLine(Integer lineNumber) {
        if (lineNumber < 1 || lineNumber > linesInFile) {
            throw new IllegalArgumentException(String.format(WRONG_LINE_EXCEPTION_MESSAGE, lineNumber, filename));
        }
    }

}

}
