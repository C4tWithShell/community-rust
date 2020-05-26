package org.elegoff.plugins.rust.languages;

import org.sonar.api.batch.fs.InputFile;

import java.io.IOException;
import java.util.Optional;

public class RustSourceCode {

    private InputFile rustFile;
    private String content = null;

    /**
     * Constructor. Parses the passed file to determine if it is syntactically correct.
     *
     * @param rustFile a supposedly Rust file
     * @param filter {@code true} to filter out UTF-8 line break characters (U+2028, U+2029 and U+0085) that may not be
     *               correctly supported by SonarQube
     * @throws IOException if there is a problem reading the passed file
     */
    public RustSourceCode(InputFile rustFile, Optional<Boolean> filter) throws IOException {
        this.rustFile = rustFile;

    }

    /**
     * Returns the {@code InputFile} of this class.
     * <p><strong>WARNING!!!</strong> Do not use {@code getRustFile.contents()} to get the source; use {@link #getContent()}
     * instead.</p>
     *
     * @return the {@code InputFile} of this class
     * @see InputFile
     * @see #getContent()
     */
    public InputFile getRustFile() {
        return rustFile;
    }

    /**
     * Returns the content of the RUST file as a {@code String} with UTF-8 line breaks possibly removed.
     * <p><strong>WARNING!!</strong> Use this method instead of {@code InputFile.contents()} in order to have the source
     * code to be cleanup if needed.</p>
     *
     * @return the RUST content
     * @throws IOException if an error occurred reading the RUST file
     * @see #RustSourceCode(InputFile, Optional)
     */
    public String getContent() throws IOException {
        if (content == null) {
                this.content = rustFile.contents();
        }

        return content;
    }
}
