package org.elegoff.plugins.rust.coverage.cobertura;

import org.elegoff.plugins.rust.RustPlugin;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.ce.measure.Settings;
import org.sonar.api.utils.log.Loggers;

import java.io.File;

public class Cobertura_Sensor implements Sensor {

    private static final org.sonar.api.utils.log.Logger LOG = Loggers.get(Cobertura_Sensor.class);

    private final Settings settings;
    private final FileSystem fileSystem;


    public Cobertura_Sensor(Settings settings, FileSystem fileSystem) {
        this.settings = settings;
        this.fileSystem = fileSystem;
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(RustLanguage.KEY).name(this.toString());

    }

    @Override
    public void execute(SensorContext context) {
        String reportPath = settings.getString(RustPlugin.COBERTURA_REPORT_PATHS);

        if (reportPath != null) {
            File xmlFile = new File(reportPath);
            if (!xmlFile.isAbsolute()) {
                xmlFile = new File(fileSystem.baseDir(), reportPath);
            }
            if (xmlFile.exists()) {
                LOG.info("Analyzing Cobertura report: " + reportPath);
                new Cobertura_Parser(context, fileSystem).parseReport(xmlFile);
            } else {
                LOG.info("Cobertura xml report not found: " + reportPath);
            }
        } else {
            LOG.info("No Cobertura report provided (see '" + RustPlugin.COBERTURA_REPORT_PATHS + "' property)");
        }
    }
}
