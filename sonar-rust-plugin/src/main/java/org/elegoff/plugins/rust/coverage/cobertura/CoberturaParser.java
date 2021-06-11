package org.elegoff.plugins.rust.coverage.cobertura;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;

import com.ctc.wstx.exc.WstxEOFException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;


public class CoberturaParser {

    private static final Logger LOG = Loggers.get(CoberturaParser.class);
    private static final String SOURCES="sources";
    private static final String SOURCE="source";
    private static final String PACKAGES="packages";
    private static final String LINES="lines";
    private static final String LINE="line";
    private static final String HITS="hits";
    private static final String BRANCH="branch";
    private static final String CONDITION_COVERAGE="condition-coverage";

    private int unresolvedCpt;

    public void importReport(File xmlFile, SensorContext context, final Map<InputFile, NewCoverage> coverageMap) throws XMLStreamException {
        LOG.info("Importing report '{}'", xmlFile);
        unresolvedCpt = 0;

        StaxParser parser;
        parser = new StaxParser(rootCursor -> {
            File baseDir = context.fileSystem().baseDir();
            List<File> baseDirs = Collections.singletonList(baseDir);
            try {
                rootCursor.advance();
            } catch (WstxEOFException e) {
                LOG.debug("Reaching end of file unexpectedly", e);
                throw new EmptyReportException();
            }
            SMInputCursor cursor = rootCursor.childElementCursor();
            while (cursor.getNext() != null) {
                if (SOURCES.equals(cursor.getLocalName())) {
                    baseDirs = lookupBaseDirs(cursor, baseDir);
                } else if (PACKAGES.equals(cursor.getLocalName())) {
                    getFileMeasures(cursor.descendantElementCursor("class"), context, coverageMap, baseDirs);
                }
            }
        });
        parser.parse(xmlFile);
        if (unresolvedCpt > 1) {
            LOG.error("Cannot resolve {} file paths, ignoring coverage measures for those files", unresolvedCpt);
        }
    }

    private static List<File> lookupBaseDirs(SMInputCursor sources, File baseDir) throws XMLStreamException {
        List<File> baseDirs = new ArrayList<>();
        SMInputCursor source = sources.childElementCursor(SOURCE);
        while (source.getNext() != null) {
            String path = FilenameUtils.normalize(source.collectDescendantText());
            if (!StringUtils.isBlank(path)) {
                File baseDirectory = new File(path);
                if (baseDirectory.isDirectory()) {
                    baseDirs.add(baseDirectory);
                } else {
                    LOG.warn("Invalid directory path in 'source' element: {}", path);
                }
            }
        }
        if (baseDirs.isEmpty()) {
            return Collections.singletonList(baseDir);
        }
        return baseDirs;
    }

    private void getFileMeasures(SMInputCursor classCursor, SensorContext context, Map<InputFile, NewCoverage> coverageData, List<File> baseDirectories)
            throws XMLStreamException {
        while (classCursor.getNext() != null) {
            String filename = FilenameUtils.normalize(classCursor.getAttrValue("filename"));
            InputFile inputFile = resolve(context, baseDirectories, filename);
            if (inputFile != null) {
                NewCoverage coverage = coverageData.computeIfAbsent(inputFile, f -> context.newCoverage().onFile(f));
                readFileContent(classCursor, coverage);
            } else {
                classCursor.advance();
            }
        }
    }

    private static void readFileContent(SMInputCursor classCursor, NewCoverage coverage) throws XMLStreamException {
        SMInputCursor line = classCursor.childElementCursor(LINES).advance().childElementCursor(LINE);
        while (line.getNext() != null) {
            int lineId = Integer.parseInt(line.getAttrValue("number"));
            coverage.lineHits(lineId, Integer.parseInt(line.getAttrValue(HITS)));

            String isBranch = line.getAttrValue(BRANCH);
            String text = line.getAttrValue(CONDITION_COVERAGE);
            if (StringUtils.equals(isBranch, "true") && StringUtils.isNotBlank(text)) {
                String[] conditions = StringUtils.split(StringUtils.substringBetween(text, "(", ")"), "/");
                coverage.conditions(lineId, Integer.parseInt(conditions[1]), Integer.parseInt(conditions[0]));
            }
        }
    }

    @Nullable
    private InputFile resolve(SensorContext context, List<File> baseDirectories, String filename) {
        String absolutePath;
        File file = new File(filename);
        if (file.isAbsolute()) {
            if (!file.exists()) {
                logUnresolvedFile("Cannot resolve the file path '{}' of the coverage report, the file does not exist in all <source>.", filename);
            }
            absolutePath = file.getAbsolutePath();
        } else {
            List<File> fileList = baseDirectories.stream()
                    .map(base -> new File(base, filename))
                    .filter(File::exists)
                    .collect(Collectors.toList());
            if (fileList.isEmpty()) {
                logUnresolvedFile("Cannot resolve the file path '{}' of the coverage report, the file does not exist in all <source>.", filename);
                return null;
            }
            if (fileList.size() > 1) {
                logUnresolvedFile("Cannot resolve the file path '{}' of the coverage report, ambiguity, the file exists in several <source>.", filename);
                return null;
            }
            absolutePath = fileList.get(0).getAbsolutePath();
        }
        return context.fileSystem().inputFile(context.fileSystem().predicates().hasAbsolutePath(absolutePath));
    }

    private void logUnresolvedFile(String msg, String filename) {
        unresolvedCpt++;
        if (unresolvedCpt == 1) {
            LOG.error(msg, filename);
        }
    }


}