/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

/*
See https://doc.rust-lang.org/reference/keywords.html
 */
public enum RustKeyword implements TokenType {
    KW_AS("as"),
    KW_BREAK("break"),
    KW_CONST("const"),
    KW_CONTINUE("continue"),
    KW_CRATE("crate"),
    KW_ELSE("else"),
    KW_ENUM("enum"),
    KW_EXTERN("extern"),
    KW_FALSE("false"),
    KW_FN("fn"),
    KW_FOR("for"),
    KW_IF("if"),
    KW_IMPL("impl"),
    KW_IN("in"),
    KW_LET("let"),
    KW_LOOP("loop"),
    KW_MATCH("match"),
    KW_MOD("mod"),
    KW_MOVE("move"),
    KW_MUT("mut"),
    KW_PUB("pub"),
    KW_REF("ref"),
    KW_RETURN("return"),
    KW_SELFVALUE("self"),
    KW_SELFTYPE("Self"),
    KW_STATIC("static"),
    KW_STRUCT("struct"),
    KW_SUPER("super"),
    KW_TRAIT("trait"),
    KW_TRUE("true"),
    KW_TYPE("type"),
    KW_UNSAFE("unsafe"),
    KW_USE("use"),
    KW_WHERE("where"),
    KW_WHILE("while"),
    /* Reserved keywords */
    KW_ABSTRACT("abstract"),
    KW_BECOME("become"),
    KW_BOX("box"),
    KW_DO(" do"),
    KW_FINAL("final"),
    KW_MACRO("macro"),
    KW_OVERRIDE(" override"),
    KW_PRIV("priv"),
    KW_TYPEOF("typeof"),
    KW_UNSIZED("unsized"),
    KW_VIRTUAL("virtual"),
    KW_YIELD("yield"),
    /*Weak keywords*/
    KW_UNION ("union"),
    KW_STATICLIFETIME("'static")
    ;

    private final String value;

    RustKeyword(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean hasToBeSkippedFromAst(AstNode node) {
        return false;
    }

    public static String[] keywordValues() {
        RustKeyword[] keywordsEnum = RustKeyword.values();
        String[] keywords = new String[keywordsEnum.length];
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = keywordsEnum[i].getValue();
        }
        return keywords;
    }
}
