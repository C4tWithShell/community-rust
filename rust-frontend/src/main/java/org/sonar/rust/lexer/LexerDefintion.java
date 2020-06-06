package org.sonar.rust.lexer;

import com.sonar.sslr.impl.channel.RegexpChannel;
import org.sonar.rust.api.RustTokenType;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public class LexerDefintion {

    private final static String TUPLE_INDEX_REGEXP="";

    public static RegexpChannel TUPLE_INDEX = regexp(RustTokenType.STRING,  TUPLE_INDEX_REGEXP);

}
