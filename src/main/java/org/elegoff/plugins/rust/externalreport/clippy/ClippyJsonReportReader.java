package org.elegoff.plugins.rust.externalreport.clippy;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import java.util.function.Consumer;
import org.sonarsource.analyzer.commons.internal.json.simple.JSONArray;
import org.sonarsource.analyzer.commons.internal.json.simple.JSONObject;
import org.sonarsource.analyzer.commons.internal.json.simple.parser.JSONParser;
import org.sonarsource.analyzer.commons.internal.json.simple.parser.ParseException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClippyJsonReportReader {
    private final JSONParser jsonParser = new JSONParser();
    private final Consumer<Issue> consumer;

    public static class Issue {
        @Nullable
        String filePath;
        @Nullable
        String ruleKey;
        @Nullable
        String message;
        @Nullable
        Integer lineNumberStart;
        @Nullable
        Integer lineNumberEnd;
        @Nullable
        String severity;
    }

    private ClippyJsonReportReader(Consumer<Issue> consumer) {
        this.consumer = consumer;
    }

    static void read(InputStream in, Consumer<Issue> consumer) throws IOException, ParseException {
        new ClippyJsonReportReader(consumer).read(in);
    }

    private void read(InputStream in) throws IOException, ParseException {
        JSONObject rootObject = (JSONObject) jsonParser.parse(new InputStreamReader(in, UTF_8));
        JSONArray files = (JSONArray) rootObject.get("results");
        if (files != null) {
            ((Stream<JSONObject>) files.stream()).forEach(this::onResult);
        }
    }

    private void onResult(JSONObject result) {
        Issue issue = new Issue();

        JSONObject message = (JSONObject) result.get("message");
        JSONObject code = (JSONObject) message.get("code");
        if (code == null) return;
        issue.ruleKey = (String) code.get("code");

        JSONArray spans = (JSONArray) message.get("spans");
        if ((spans == null)||spans.size()==0) return;
        JSONObject span = (JSONObject) spans.get(0); //TODO what if > 1 span ?
        issue.filePath = (String) span.get("file_name");


        issue.message = (String) message.get("message");

        issue.lineNumberStart = toInteger(span.get("line_start"));
        issue.lineNumberEnd = toInteger(span.get("line_end"));

        issue.severity = (String) message.get("level");

        consumer.accept(issue);
    }

    private static Integer toInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

}
