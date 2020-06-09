package org.sonar.rust.lexer;

import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;
import org.sonar.rust.api.RustTokenType;
import org.sonar.sslr.channel.Channel;
import org.sonar.sslr.channel.CodeReader;



public class StringLiteralsChannel extends Channel<Lexer> {

    private static final char EOF = (char) -1;

    private final StringBuilder sb = new StringBuilder(256);

    private int index = 0;
    private char ch = ' ';
    private boolean isRawString = false;

    @Override
    public boolean consume(CodeReader code, Lexer output) {
        int line = code.getLinePosition();
        int column = code.getColumnPosition();
        index = 0;
        readStringPrefix(code);
        if (ch != '\"') {
            return false;
        }
        if (isRawString) {
            if (!readRawString(code)) {
                return false;
            }
        } else {
            if (!readString(code)) {
                return false;
            }
        }
        readUdSuffix(code);
        for (int i = 0; i < index; i++) {
            sb.append((char) code.pop());
        }
        output.addToken(Token.builder()
                .setLine(line)
                .setColumn(column)
                .setURI(output.getURI())
                .setValueAndOriginalValue(sb.toString())
                .setType(RustTokenType.STRING)
                .build());
        sb.setLength(0);
        return true;
    }

    private boolean readString(CodeReader code) {
        index++;
        while (code.charAt(index) != ch) {
            if (code.charAt(index) == EOF) {
                return false;
            }
            if (code.charAt(index) == '\\') {
                // escape
                index++;
            }
            index++;
        }
        index++;
        return true;
    }

    private boolean readRawString(CodeReader code) {
        // "delimiter( raw_character* )delimiter"
        char charAt;
        index++;
        while ((charAt = code.charAt(index)) != '(') { // delimiter in front of (
            if (charAt == EOF) {
                return false;
            }
            sb.append(charAt);
            index++;
        }
        String delimiter = sb.toString();
        sb.setLength(0);
        do {
            index -= sb.length();
            sb.setLength(0);
            while ((charAt = code.charAt(index)) != ')') { // raw_character*
                if (charAt == EOF) {
                    return false;
                }
                index++;
            }
            index++;
            while ((charAt = code.charAt(index)) != '"') { // delimiter after )
                if (charAt == EOF) {
                    return false;
                }
                sb.append(charAt);
                index++;

                if (sb.length() > delimiter.length()) {
                    break;
                }
            }
        } while (!sb.toString().equals(delimiter));
        sb.setLength(0);
        index++;
        return true;
    }

    private void readStringPrefix(CodeReader code) {
        ch = code.charAt(index);
        isRawString = false;
        if ((ch == 'u') || (ch == 'U') || ch == 'L') {
            index++;
            if (ch == 'u' && code.charAt(index) == '8') {
                index++;
            }
            if (code.charAt(index) == ' ') {
                index++;
            }
            ch = code.charAt(index);
        }
        if (ch == 'R') {
            index++;
            isRawString = true;
            ch = code.charAt(index);
        }
    }

    private void readUdSuffix(CodeReader code) {
        for (int start_index = index, len = 0;; index++) {
            char c = code.charAt(index);
            if (c == EOF) {
                return;
            }
            if (Character.isLowerCase(c) || Character.isUpperCase(c) || (c == '_')) {
                len++;
            } else {
                if (Character.isDigit(c)) {
                    if (len > 0) {
                        len++;
                    } else {
                        index = start_index;
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

}
