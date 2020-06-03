package org.sonar.rust.lexer;

import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;
import org.sonar.rust.api.RustTokenType;
import org.sonar.sslr.channel.Channel;
import org.sonar.sslr.channel.CodeReader;



public class NewLineChannel extends Channel<Lexer> {



    public NewLineChannel() {

    }

    @Override
    public boolean consume(CodeReader code, Lexer output) {
        char ch = (char) code.peek();

        if ((ch == '\\') && isNewLine(code.charAt(1))) {
            // Explicit line joining
            code.pop();
            consumeEOL(code);
            return true;
        }

        if (isNewLine(ch)) {
            processNewLine(code, output);
            return true;
        }

        return false;
    }

    private boolean processNewLine(CodeReader code, Lexer output) {

        if (output.getTokens().isEmpty() || (output.getTokens().get(output.getTokens().size() - 1).getType().equals(RustTokenType.NEWLINE))) {
            // Blank line
            consumeEOL(code);
            return true;
        }

        // NEWLINE token
        output.addToken(Token.builder()
                .setLine(code.getLinePosition())
                .setColumn(code.getColumnPosition())
                .setURI(output.getURI())
                .setType(RustTokenType.NEWLINE)
                .setValueAndOriginalValue("\n")
                .setGeneratedCode(true)
                .build());
        consumeEOL(code);
        return false;
    }



    private void joinLines(CodeReader code) {
        while (Character.isWhitespace(code.peek())) {
            code.pop();
        }
    }

    private static void consumeEOL(CodeReader code) {
        if ((code.charAt(0) == '\r') && (code.charAt(1) == '\n')) {
            // \r\n
            code.pop();
            code.pop();
        } else {
            // \r or \n
            code.pop();
        }
    }

    private static boolean isNewLine(char ch) {
        return (ch == '\n') || (ch == '\r');
    }

}