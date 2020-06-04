package org.sonar.rust.tree;

import com.sonar.sslr.api.AstNode;
import org.sonar.plugins.rust.api.tree.Token;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Separators {
    public static final Separators EMPTY = new Separators(null, null);
    @Nullable
    private final Token separator;
    @Nullable
    private final Token newline;
    private final List<Token> elements;

    Separators(@Nullable AstNode separator, @Nullable AstNode newline) {
        this.separator = separator == null ? null : new TokenImpl(separator.getToken());
        this.newline = newline == null ? null : new TokenImpl(newline.getToken());
        this.elements = Stream.of(this.separator, this.newline).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @CheckForNull
    public Token last() {
        return newline == null ? separator : newline;
    }

    public List<Token> elements() {
        return elements;
    }
}

