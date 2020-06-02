package org.elegoff.rust;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.squidbridge.api.SquidConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RustSquidConfiguration extends SquidConfiguration {

    //public static final String OVERALLINCLUDEKEY = "RustOverallInclude";
    //public static final String OVERALLDEFINEKEY = "RustOverallDefine";
    private static final Logger LOG = Loggers.get(RustSquidConfiguration.class);

    private boolean ignoreHeaderComments;

    private List<String> forceIncludeFiles = new ArrayList<>();
    private String baseDir = "";
    private boolean errorRecoveryEnabled = true;
    private String jsonCompilationDatabaseFile;
    private String[] publicApiFileSuffixes = new String[]{};
    private int functionComplexityThreshold = 10;
    private int functionSizeThreshold = 20;
    private boolean cpdIgnoreLiteral = false;
    private boolean cpdIgnoreIdentifier = false;



    public RustSquidConfiguration() {
        this(Charset.defaultCharset());
    }

    public RustSquidConfiguration(Charset encoding) {
        super(encoding);

    }


    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setErrorRecoveryEnabled(boolean errorRecoveryEnabled) {
        this.errorRecoveryEnabled = errorRecoveryEnabled;
    }

    public boolean getErrorRecoveryEnabled() {
        return this.errorRecoveryEnabled;
    }

    public String getJsonCompilationDatabaseFile() {
        return jsonCompilationDatabaseFile;
    }

    public void setJsonCompilationDatabaseFile(String jsonCompilationDatabaseFile) {
        this.jsonCompilationDatabaseFile = jsonCompilationDatabaseFile;
    }

    public void setPublicApiFileSuffixes(String[] suffixes) {
        publicApiFileSuffixes = suffixes.clone();
    }

    public String[] getPublicApiFileSuffixes() {
        return publicApiFileSuffixes.clone();
    }

    public void setFunctionComplexityThreshold(int threshold) {
        functionComplexityThreshold = threshold;
    }

    public int getFunctionComplexityThreshold() {
        return functionComplexityThreshold;
    }

    public void setFunctionSizeThreshold(int threshold) {
        functionSizeThreshold = threshold;
    }

    public int getFunctionSizeThreshold() {
        return functionSizeThreshold;
    }

    public void setCpdIgnoreLiteral(boolean cpdIgnoreLiteral) {
        this.cpdIgnoreLiteral = cpdIgnoreLiteral;
    }

    public boolean getCpdIgnoreLiteral() {
        return this.cpdIgnoreLiteral;
    }

    public void setCpdIgnoreIdentifier(boolean cpdIgnoreIdentifier) {
        this.cpdIgnoreIdentifier = cpdIgnoreIdentifier;
    }

    public boolean getCpdIgnoreIdentifier() {
        return this.cpdIgnoreIdentifier;
    }

    public void setCompilationPropertiesWithBuildLog(@Nullable List<File> reports,
                                                     String fileFormat,
                                                     String charsetName) {

        if (reports == null || reports.isEmpty()) {
            return;
        }

        for (var buildLog : reports) {
            if (!buildLog.exists()) {
                LOG.error("Compilation log file not found: '{}'", buildLog.getAbsolutePath());
            }
        }
    }

    public Charset getEncoding() {
        return super.getCharset();
    }

}