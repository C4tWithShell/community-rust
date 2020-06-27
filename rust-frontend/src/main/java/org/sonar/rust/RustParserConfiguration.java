package org.sonar.rust;

import com.google.common.collect.ImmutableSet;
import org.sonar.api.config.Configuration;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class RustParserConfiguration {

    private final Charset charset;


    private RustParserConfiguration(Builder builder) {
        this.charset = builder.charset;

    }

    public Charset getCharset() {
        return charset;
    }



    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Charset charset = null;

        private Builder() {
        }

        public Builder setCharset(Charset charset) {
            this.charset = charset;
            return this;
        }
        public Charset getCharset() {
            return charset;
        }

        public RustParserConfiguration build() {
            checkNotNull(charset, "charset is mandatory and cannot be left null");
            return new RustParserConfiguration(this);
        }

    }

}
