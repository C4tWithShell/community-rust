package org.elegoff.rust.utils;

import org.sonar.api.utils.PathUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public class RustReportLocation {
    private final String file;
    private final String line;
    private final String info;

    public RustReportLocation(@Nullable String file, @Nullable String line, String info) {
        super();
        // normalize file, removing double and single dot path steps => avoids duplicates
        this.file = PathUtils.sanitize(file);
        this.line = line;
        this.info = info;
    }

    public String getFile() {
        return file;
    }

    public String getLine() {
        return line;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "RustReportLocation [file=" + file + ", line=" + line + ", info=" + info + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, info, line);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RustReportLocation other = (RustReportLocation) obj;
        return Objects.equals(file, other.file) && Objects.equals(info, other.info) && Objects.equals(line, other.line);
    }
}
