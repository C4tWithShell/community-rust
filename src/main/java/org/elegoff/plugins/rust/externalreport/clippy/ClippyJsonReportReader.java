package org.elegoff.plugins.rust.externalreport.clippy;

import javax.annotation.Nullable;
import java.io.*;
import java.util.stream.Stream;
import java.util.function.Consumer;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.analyzer.commons.internal.json.simple.JSONArray;
import org.sonarsource.analyzer.commons.internal.json.simple.JSONObject;
import org.sonarsource.analyzer.commons.internal.json.simple.parser.JSONParser;
import org.sonarsource.analyzer.commons.internal.json.simple.parser.ParseException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClippyJsonReportReader {
    private final JSONParser jsonParser = new JSONParser();
    private final Consumer<Issue> consumer;
    private static final Logger LOG = Loggers.get(ClippyJsonReportReader.class);

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
        Integer colNumberStart;
        @Nullable
        Integer colNumberEnd;
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
        JSONArray results = (JSONArray) rootObject.get("results");
        if (results != null) {
            ((Stream<JSONObject>) results.stream()).forEach(this::onResult);
        }
    }

    private void onResult(JSONObject result) {
        Issue issue = new Issue();

        JSONObject message = (JSONObject) result.get("message");
        if (message == null) return;
        JSONObject code = (JSONObject) message.get("code");
        if (code == null) return;
        issue.ruleKey = (String) code.get("code");

       LOG.debug("Clippy rule found : " + issue.ruleKey);


        JSONArray spans = (JSONArray) message.get("spans");
        if ((spans == null)||spans.size()==0) return;
        JSONObject span = (JSONObject) spans.get(0);
        issue.filePath = (String) span.get("file_name");


        issue.message = (String) message.get("message");

        issue.lineNumberStart = toInteger(span.get("line_start")) ;
        issue.lineNumberEnd = toInteger(span.get("line_end"));
        issue.colNumberStart = toInteger(span.get("column_start"));
        issue.colNumberEnd = toInteger(span.get("column_end")) ;

        issue.severity = (String) message.get("level");

        consumer.accept(issue);
    }

    private static Integer toInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    public static InputStream toJSON(File rawReport) throws IOException{
        String BEGIN="{\"results\": [";
        String END="]}";

        if (rawReport == null) {
            throw new FileNotFoundException();
        }

       StringBuffer sb = new StringBuffer(BEGIN);

        //read text report line by line
        String reportPath = rawReport.getAbsolutePath();
        BufferedReader reader;

        reader = new BufferedReader(new FileReader(
                reportPath));
        String line = reader.readLine();
        String separator="";
        while (line != null) {

            // read next line

            //a valid Clippy result needs to be a valid json String
            if (line.startsWith("{") && line.endsWith("}")){
                sb.append(separator).append(line);
                separator= ",";
            }
            line = reader.readLine();
        }
        reader.close();

       sb.append(END);

        InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        return in;
    }

}
