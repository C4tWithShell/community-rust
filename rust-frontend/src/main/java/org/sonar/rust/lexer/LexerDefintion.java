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
package org.sonar.rust.lexer;

import com.sonar.sslr.impl.channel.RegexpChannel;
import org.sonar.rust.api.RustTokenType;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public class LexerDefintion {

    private final static String TUPLE_INDEX_REGEXP="";

    public static RegexpChannel TUPLE_INDEX = regexp(RustTokenType.STRING,  TUPLE_INDEX_REGEXP);

}
