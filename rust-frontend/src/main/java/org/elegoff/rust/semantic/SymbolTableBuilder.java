package org.elegoff.rust.semantic;


import org.elegoff.plugins.rust.api.RustFile;
import org.elegoff.plugins.rust.api.symbols.Symbol;
import org.elegoff.plugins.rust.api.tree.FileInput;

import java.util.Map;
import java.util.Set;

public class SymbolTableBuilder {
    public SymbolTableBuilder(String packageName, RustFile rustFile) {
    }

    public SymbolTableBuilder(String packageName, RustFile rustFile, Map<String, Set<Symbol>> globalSymbols) {
    }

    public SymbolTableBuilder(RustFile rustFile) {
    }

    public void visitFileInput(FileInput rootTree) {
    }
}
