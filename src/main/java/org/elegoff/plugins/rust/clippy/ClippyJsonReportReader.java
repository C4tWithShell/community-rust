package org.elegoff.plugins.rust.clippy;

import javax.annotation.Nullable;
import java.io.*;
import java.util.stream.Stream;
import java.util.function.Consumer;

import org.sonarsource.analyzer.commons.internal.json.simple.JSONArray;
import org.sonarsource.analyzer.commons.internal.json.simple.JSONObject;
import org.sonarsource.analyzer.commons.internal.json.simple.parser.JSONParser;
import org.sonarsource.analyzer.commons.internal.json.simple.parser.ParseException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClippyJsonReportReader {
    private final JSONParser jsonParser = new JSONParser();
    private final Consumer<ClippyIssue> consumer;
    private static final String BEGINJSON = "{\"results\": [";
    private static final String ENDJSON = "]}";

    public static class ClippyIssue {
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
        Integer colNumberStart;
        @Nullable
        Integer colNumberEnd;
        @Nullable
        String severity;
    }

    private ClippyJsonReportReader(Consumer<ClippyIssue> consumer) {
        this.consumer = consumer;
    }

    static void read(InputStream in, Consumer<ClippyIssue> consumer) throws IOException, ParseException {
        new ClippyJsonReportReader(consumer).read(in);
    }

    private void read(InputStream in) throws IOException, ParseException {
        JSONObject rootObject = (JSONObject) jsonParser.parse(new InputStreamReader(in, UTF_8));
        JSONArray results = (JSONArray) rootObject.get("results");
        if (results != null) {
            ((Stream<JSONObject>) results.stream()).forEach(this::onResult);
        }
    }

    private void onResult(JSONObject result) {
        ClippyIssue clippyIssue = new ClippyIssue();


        //Exit silently when JSON is not compliant

        JSONObject message = (JSONObject) result.get("message");
        if (message == null) return;
        JSONObject code = (JSONObject) message.get("code");
        if (code == null) return;
        clippyIssue.ruleKey = (String) code.get("code");
        JSONArray spans = (JSONArray) message.get("spans");
        if ((spans == null) || spans.size() == 0) return;
        JSONObject span = (JSONObject) spans.get(0);
        clippyIssue.filePath = (String) span.get("file_name");
        clippyIssue.message = (String) message.get("message");
        clippyIssue.lineNumberStart = toInteger(span.get("line_start"));
        clippyIssue.lineNumberEnd = toInteger(span.get("line_end"));
        clippyIssue.colNumberStart = toInteger(span.get("column_start"));
        clippyIssue.colNumberEnd = toInteger(span.get("column_end"));
        clippyIssue.severity = (String) message.get("level");

        consumer.accept(clippyIssue);
    }

    private static Integer toInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    public static InputStream toJSON(File rawReport) throws IOException {


        if (rawReport == null) {
            throw new FileNotFoundException();
        }

        StringBuilder sb = new StringBuilder(BEGINJSON);

        //read text report line by line
        String reportPath = rawReport.getAbsolutePath();
        BufferedReader reader;

        try (BufferedReader bufferedReader = reader = new BufferedReader(new FileReader(reportPath))) {
            String line = reader.readLine();
            String separator = "";
            while (line != null) {
                //a valid Clippy result needs to be a valid json String
                if (line.startsWith("{") && line.endsWith("}")) {
                    sb.append(separator).append(line);
                    separator = ",";
                }
                line = reader.readLine();
            }
        }
        reader.close();
        sb.append(ENDJSON);
        return new ByteArrayInputStream(sb.toString().getBytes());

    }

}
