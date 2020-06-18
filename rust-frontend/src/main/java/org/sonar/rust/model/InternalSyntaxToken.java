package org.sonar.rust.model;

import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.plugins.rust.api.tree.TreeVisitor;
import org.sonar.rust.tree.SyntaxToken;
import org.sonar.rust.tree.SyntaxTrivia;
import org.sonar.sslr.grammar.GrammarRuleKey;

import java.util.List;

public class InternalSyntaxToken extends RustTree implements SyntaxToken {

    private List<SyntaxTrivia> trivias;
    private int startIndex;
    private int endIndex;
    private final int line;
    private final int column;
    private final String value;
    private final boolean isEOF;

    protected InternalSyntaxToken(InternalSyntaxToken internalSyntaxToken) {
        super(null);
        this.value = internalSyntaxToken.value;
        this.line = internalSyntaxToken.line;
        this.column = internalSyntaxToken.column;
        this.trivias = internalSyntaxToken.trivias;
        this.startIndex = internalSyntaxToken.startIndex;
        this.endIndex = internalSyntaxToken.endIndex;
        this.isEOF = internalSyntaxToken.isEOF;
    }

    public InternalSyntaxToken(int line, int column, String value, List<SyntaxTrivia> trivias, int startIndex, int endIndex, boolean isEOF) {
        super(null);
        this.value = value;
        this.line = line;
        this.column = column;
        this.trivias = trivias;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.isEOF = isEOF;
    }

    @Override
    public SyntaxToken firstToken() {
        return this;
    }

    @Override
    public SyntaxToken lastToken() {
        return this;
    }

    @Override
    public String text() {
        return value;
    }

    @Override
    public List<SyntaxTrivia> trivias() {
        return trivias;
    }

    @Override
    public void accept(TreeVisitor visitor) {
        // FIXME do nothing at the moment
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public int line() {
        return line;
    }

    @Override
    public int column() {
        return column;
    }

    @Override
    public Kind kind() {
        return Kind.TOKEN;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    public boolean isEOF() {
        return isEOF;
    }

    @Override
    public Iterable<Tree> children() {
        throw new UnsupportedOperationException();
    }

    public void setGrammarRuleKey(GrammarRuleKey grammarRuleKey) {
        this.grammarRuleKey = grammarRuleKey;
    }


}
