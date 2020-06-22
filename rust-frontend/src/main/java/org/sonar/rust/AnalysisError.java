package org.sonar.rust;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AnalysisError
    {

        public enum Kind {
            PARSE_ERROR,
            SEMANTIC_ERROR,
            CHECK_ERROR,
            SE_ERROR,
        }

        private final String message;
        private final String cause;
        private final String filename;
        private final Kind kind;

  public AnalysisError(Exception exception, String filename, Kind kind) {
        this.message = exception.getMessage() == null ? "" : exception.getMessage();
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        this.cause = sw.toString();
        this.filename = filename;
        this.kind = kind;
    }

        public String getMessage() {
        return message;
    }

        public String getCause() {
        return cause;
    }

        public String getFilename() {
        return filename;
    }

        public Kind getKind() {
        return kind;
    }

        public int serializedSize() {
        return message.length()+cause.length()+filename.length()+ kind.name().length();
    }
    }

