package org.elegoff.plugins.rust.coverage.lcov;

import org.elegoff.plugins.rust.RustPlugin;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
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
                    .onlyOnLanguages(RustLanguage.KEY)
                    .onlyWhenConfiguration(conf -> conf.hasKey(RustPlugin.LCOV_REPORT_PATHS))
                    .name("Rust Coverage")
                    .onlyOnFileType(InputFile.Type.MAIN);
        }

        @Override
        public void execute(SensorContext context) {
            Set<String> reports = new HashSet<>(Arrays.asList(context.config().getStringArray(RustPlugin.LCOV_REPORT_PATHS)));


            List<File> lcovFiles = reports.stream()
                    .map(report -> getIOFile(context.fileSystem().baseDir(), report))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (lcovFiles.isEmpty()) {
                LOG.warn("No coverage information will be saved because all LCOV files cannot be found.");
                return;
            }
            saveCoverageFromLcovFiles(context, lcovFiles);
        }

        private static void saveCoverageFromLcovFiles(SensorContext context, List<File> lcovFiles) {
            LOG.info("Analysing {}", lcovFiles);

            FileSystem fileSystem = context.fileSystem();
            FilePredicate mainFilePredicate = fileSystem.predicates().hasLanguages(RustLanguage.KEY);
            FileChooser fileChooser = new FileChooser(fileSystem.inputFiles(mainFilePredicate));

            LCOVParser parser = LCOVParser.create(context, lcovFiles, fileChooser);
            Map<InputFile, NewCoverage> coveredFiles = parser.coverageByFile();

            Iterable<InputFile> ericfiles = fileSystem.inputFiles(mainFilePredicate);

            for (InputFile ipf : ericfiles){
                String name = ipf.filename();
            }


            for (InputFile inputFile : fileSystem.inputFiles(mainFilePredicate)) {
                NewCoverage fileCoverage = coveredFiles.get(inputFile);

                if (fileCoverage != null) {
                    fileCoverage.save();
                }
            }

            List<String> unresolvedPaths = parser.unresolvedPaths();
            if (!unresolvedPaths.isEmpty()) {
                LOG.warn(String.format("Could not resolve %d file paths in %s", unresolvedPaths.size(), lcovFiles));
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unresolved paths:\n" + String.join("\n", unresolvedPaths));
                } else {
                    LOG.warn("First unresolved path: " + unresolvedPaths.get(0) + " (Run in DEBUG mode to get full list of unresolved paths)");
                }
            }

            int inconsistenciesNumber = parser.inconsistenciesNumber();
            if (inconsistenciesNumber > 0) {
                LOG.warn("Found {} inconsistencies in coverage report. Re-run analyse in debug mode to see details.", inconsistenciesNumber);
            }
        }

        /**
         * Returns a java.io.File for the given path.
         * If path is not absolute, returns a File with module base directory as parent path.
         */
        @CheckForNull
        private static File getIOFile(File baseDir, String path) {
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
}
