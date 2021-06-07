package org.elegoff.plugins.rust.coverage.lcov;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.elegoff.plugins.rust.RustPlugin;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.WildcardPattern;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.CheckForNull;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


public class CoverageSensor implements Sensor {
        private static final Logger LOG = Loggers.get(CoverageSensor.class);

        @Override
        public void describe(SensorDescriptor descriptor) {
            descriptor
                    .name("LCOV Sensor for Rust coverage")
                    .onlyOnLanguage(RustLanguage.KEY);
        }

        @Override
        public void execute(SensorContext context) {
            String baseDir = context.fileSystem().baseDir().getPath();
            List<File> lcovFiles = getLcovReports(baseDir, context.config());

            if (lcovFiles.isEmpty()) {
                LOG.warn("No coverage information will be saved because all LCOV files cannot be found.");
                return;
            }
            saveCoverageFromLcovFiles(context, lcovFiles);
        }

        private static void saveCoverageFromLcovFiles(SensorContext context, List<File> lcovFiles) {
            LOG.info("Importing {}", lcovFiles);

            FileSystem fileSystem = context.fileSystem();
            FilePredicate mainFilePredicate = fileSystem.predicates().hasLanguages(RustLanguage.KEY);
            FileChooser fileChooser = new FileChooser(fileSystem.inputFiles(mainFilePredicate));

            LCOV_Parser parser = LCOV_Parser.build(context, lcovFiles, fileChooser);
            Map<InputFile, NewCoverage> coveredFiles = parser.getFileCoverage();


            for (Iterator<InputFile> iterator = fileSystem.inputFiles(mainFilePredicate).iterator(); iterator.hasNext(); ) {
                InputFile inputFile = iterator.next();
                NewCoverage fileCoverage = coveredFiles.get(inputFile);

                if (fileCoverage != null) {
                    fileCoverage.save();
                }
            }

            List<String> unresolvedPaths = parser.unknownPaths();
            if (unresolvedPaths.isEmpty()) {
            } else {
                LOG.warn(String.format("Could not resolve %d file paths in %s", unresolvedPaths.size(), lcovFiles));
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unresolved paths:\n" + String.join("\n", unresolvedPaths));
                } else {
                    LOG.warn("First unresolved path: " + unresolvedPaths.get(0) + " (Run in DEBUG mode to get full list of unresolved paths)");
                }
            }

            int pbCount = parser.pbCount();
            if (pbCount > 0) {
                LOG.warn("Found {} inconsistencies in coverage report", pbCount);
            }
        }

        @CheckForNull
        private static File getFile(File baseDir, String path) {
            File file = new File(path);
            if (!file.isAbsolute()) {
                file = new File(baseDir, path);
            }
            if (!file.isFile()) {
                LOG.warn("No coverage information will be saved because LCOV file cannot be found.");
                LOG.warn("Provided LCOV file path: {}. Seek file with path: {}", path, file.getAbsolutePath());
                return null;
            }
            return file;
        }

    private static List<File> getLcovReports(String baseDir, Configuration config) {
        if (!config.hasKey(RustPlugin.LCOV_REPORT_PATHS)) {
            return getReports(config, baseDir, RustPlugin.LCOV_REPORT_PATHS, RustPlugin.DEFAULT_LCOV_REPORT_PATHS);
        }

        return Arrays.stream(config.getStringArray(RustPlugin.LCOV_REPORT_PATHS))
                .flatMap(path -> getReports(config, baseDir, RustPlugin.LCOV_REPORT_PATHS, path).stream())
                .collect(Collectors.toList());
    }

    public static List<File> getReports(Configuration conf, String baseDirPath, String reportPathPropertyKey, String reportPath) {
        LOG.debug("Using pattern '{}' to find reports", reportPath);

        DirectoryLookup scanner = new DirectoryLookup(new File(baseDirPath), WildcardPattern.create(reportPath));
        List<File> includedFiles = scanner.getIncludedFiles();

        if (includedFiles.isEmpty()) {
            if (conf.hasKey(reportPathPropertyKey)) {
                // try absolute path
                File file = new File(reportPath);
                if (!file.exists()) {
                    LOG.warn("No report was found for {} using pattern {}", reportPathPropertyKey, reportPath);
                } else {
                    includedFiles.add(file);
                }
            } else {
                LOG.debug("No report was found for {} using default pattern {}", reportPathPropertyKey, reportPath);
            }
        }
        return includedFiles;
    }


    private static class DirectoryLookup {
        private final File baseDir;
        private final WildcardPattern pattern;

        public DirectoryLookup(File baseDir, WildcardPattern pattern) {
            this.baseDir = baseDir;
            this.pattern = pattern;
        }

        public List<File> getIncludedFiles() {
            final String baseDirAbsolutePath = baseDir.getAbsolutePath();
            IOFileFilter fileFilter = new IOFileFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return accept(new File(dir, name));
                }

                @Override
                public boolean accept(File file) {
                    String path = file.getAbsolutePath();
                    path = path.substring(Math.min(baseDirAbsolutePath.length(), path.length()));
                    return pattern.match(FilenameUtils.separatorsToUnix(path));
                }
            };
            return new ArrayList<>(FileUtils.listFiles(baseDir, fileFilter, TrueFileFilter.INSTANCE));
        }
    }
}
