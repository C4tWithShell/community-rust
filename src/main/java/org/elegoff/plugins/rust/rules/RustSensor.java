package org.elegoff.plugins.rust.rules;


import org.elegoff.plugins.rust.languages.RustLanguage;
import org.elegoff.plugins.rust.languages.RustSourceCode;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.elegoff.plugins.rust.linecounter.LineCounter;
import org.elegoff.plugins.rust.settings.RustLanguageSettings;
import java.io.IOException;


/**
 * Main sensor
 */
public class RustSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(RustSensor.class);


    private final FileSystem fileSystem;
    private final FilePredicate mainFilesPredicate;
    private final FileLinesContextFactory fileLinesContextFactory;


    /**
     * Constructor
     *
     * @param fileSystem the file system on which the sensor will find the files to be analyzed
     * @param checkFactory check factory used to get the checks to execute against the files
     * @param fileLinesContextFactory factory used to report measures
     */
    public RustSensor(FileSystem fileSystem, CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.fileSystem = fileSystem;

        this.mainFilesPredicate = fileSystem.predicates().and(
                fileSystem.predicates().hasType(InputFile.Type.MAIN),
                fileSystem.predicates().hasLanguage(RustLanguage.KEY));
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(RustLanguage.KEY);
        descriptor.name("RUST Sensor");
    }

    @Override
    public void execute(SensorContext context) {
        LOGGER.debug("RUST sensor executed with context: " + context);


        // Skip analysis if no rules enabled from this plugin
        boolean skipChecks = true;


        for (InputFile inputFile : fileSystem.inputFiles(mainFilesPredicate)) {
            LOGGER.debug("Analyzing file: " + inputFile.filename());
            try {
                RustSourceCode sourceCode = new RustSourceCode(inputFile, context.config().getBoolean(RustLanguageSettings.FILTER_UTF8_LB_KEY));
                computeLinesMeasures(context, sourceCode);
               } catch (IOException e) {
                LOGGER.warn("Error reading source file " + inputFile.filename(), e);
            }
        }
    }


    /**
     * Calculates and feeds line measures (comments, actual number of code lines)
     *
     *
     * @param context the sensor context
     * @param sourceCode the RUST source code to be analyzed
     */
    private void computeLinesMeasures(SensorContext context, RustSourceCode sourceCode) {
        LineCounter.analyse(context, fileLinesContextFactory, sourceCode);
    }





}