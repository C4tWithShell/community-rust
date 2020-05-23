package org.elegoff.plugins.rust.highlighting;

import org.elegoff.plugins.rust.checks.RustSourceCode;
import org.elegoff.plugins.rust.linecounter.Parser;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class in charge of RUST code highlighting in SonarQube
 */
public class RustHighlighting {
    private static final Logger LOGGER = Loggers.get(RustHighlighting.class);

    /**
     * The optional series of 3 bytes that mark the beginning of an UTF-8 file
     */
    public static final String BOM_CHAR = "\ufeff";


    private List<HighlightingData> highlighting = new ArrayList<>();
    private TypeOfText currentCode = TypeOfText.KEYWORD;
    private String content;


    /**
     * Constructor
     *
     * @param sourceCode the RUST source code to be highlighted
     * @throws IOException if an error occurred reading the file
     * @throws IllegalArgumentException if {@code sourceCode} is {@code null}
     */
    public RustHighlighting(RustSourceCode sourceCode) throws IOException {
        if (sourceCode == null) {
            throw new IllegalArgumentException("Input RUST source code cannot be null");
        }
        process(sourceCode.getContent());
    }


    /**
     * Processes the passed RUST string
     *
     * @param rustStrContent the RUST code to be highlighted in SonarQube. Cannot be {@code null}.
     */
    private void process(String rustStrContent) {
        if ("".equals(rustStrContent)) {
            return;
        }

        if (rustStrContent.startsWith(BOM_CHAR)) {
            // remove it immediately
            LOGGER.debug("Document starts with BOM sequence");
            rustStrContent = rustStrContent.substring(1);
        }

        content = rustStrContent;
       
    }

    /**
     * Returns the list of highlighting data found for the RUST code
     *
     * @return the list of highlighting data found for the RUST code (possibly empty but never {@code null})
     */
    public List<HighlightingData> getHighlightingData() {
        //TODO elg
        return null;
    }


   

   
}