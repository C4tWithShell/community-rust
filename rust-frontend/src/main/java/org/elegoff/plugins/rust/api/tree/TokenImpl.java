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
package org.elegoff.plugins.rust.api.tree;

import com.sonar.sslr.api.TokenType;

import java.util.List;
import java.util.stream.Collectors;

public class TokenImpl implements Token{
    private com.sonar.sslr.api.Token token;
    private List<Trivia> trivia;

    public TokenImpl(com.sonar.sslr.api.Token token) {
        this.token = token;
        this.trivia = token.getTrivia().stream().map(tr -> new TriviaImpl(new TokenImpl(tr.getToken()))).collect(Collectors.toList());
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public int line() {
        return 0;
    }

    @Override
    public int column() {
        return 0;
    }

    @Override
    public List<Trivia> trivia() {
        return null;
    }

    @Override
    public TokenType type() {
        return null;
    }

    @Override
    public Token firstToken() {
        return null;
    }

    @Override
    public Token lastToken() {
        return null;
    }
}
