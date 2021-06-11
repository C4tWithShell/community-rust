package org.elegoff.plugins.rust.coverage.cobertura;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.elegoff.plugins.rust.RustPlugin;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.coverage.NewCoverage;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.WildcardPattern;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class CoberturaSensor implements Sensor {

    private static final Logger LOG = Loggers.get(CoberturaSensor.class);

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .name("Cobertura Sensor for Rust coverage")
                .onlyOnLanguage(RustLanguage.KEY);
    }

    @Override
    public void execute(SensorContext context) {
        String baseDir = context.fileSystem().baseDir().getPath();
        Configuration config = context.config();

        HashSet<InputFile> filesCovered = new HashSet<>();
        List<File> reports = getCoverageReports(baseDir, config);
        if (!reports.isEmpty()) {
            LOG.info("Rust coverage");
            for (File report : uniqueAbsolutePaths(reports)) {
                Map<InputFile, NewCoverage> coverageMeasures = importReport(report, context);
                saveMeasures(coverageMeasures, filesCovered);
            }
        }
    }

    private static List<File> getCoverageReports(String baseDir, Configuration config) {
        if (!config.hasKey(RustPlugin.COBERTURA_REPORT_PATHS)) {
            return getReports(config, baseDir, RustPlugin.COBERTURA_REPORT_PATHS, RustPlugin.DEFAULT_COBERTURA_REPORT_PATHS);
        }

        return Arrays.stream(config.getStringArray(RustPlugin.COBERTURA_REPORT_PATHS))
                .flatMap(path -> getReports(config, baseDir, RustPlugin.COBERTURA_REPORT_PATHS, path).stream())
                .collect(Collectors.toList());
    }


    private static Set<File> uniqueAbsolutePaths(List<File> reports) {
        return reports.stream()
                .map(File::getAbsoluteFile)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static Map<InputFile, NewCoverage> importReport(File report, SensorContext context)  {
        Map<InputFile, NewCoverage> coverageMeasures = new HashMap<>();
        try {
            CoberturaParser parser = new CoberturaParser();
            parser.importReport(report, context, coverageMeasures);
        } catch (EmptyReportException e) {
            LOG.warn("The report '{}' seems to be empty, ignoring. '{}'", report, e);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Error parsing the report '" + report + "'", e);
        }
        return coverageMeasures;
    }

    private static void saveMeasures(Map<InputFile, NewCoverage> coverageMeasures, HashSet<InputFile> coveredFiles) {
        for (Map.Entry<InputFile, NewCoverage> entry : coverageMeasures.entrySet()) {
            InputFile inputFile = entry.getKey();
            coveredFiles.add(inputFile);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Saving coverage measures for file '{}'", inputFile.toString());
            }
            entry.getValue()
                    .save();
        }
    }

    public static List<File> getReports(Configuration conf, String baseDirPath, String reportPathPropertyKey, String reportPath) {
        LOG.debug("Using pattern '{}' to find reports", reportPath);

        DirectoryScanner scanner = new DirectoryScanner(new File(baseDirPath), WildcardPattern.create(reportPath));
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


    private static class DirectoryScanner {
        private final File baseDir;
        private final WildcardPattern pattern;

        public DirectoryScanner(File baseDir, WildcardPattern pattern) {
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
