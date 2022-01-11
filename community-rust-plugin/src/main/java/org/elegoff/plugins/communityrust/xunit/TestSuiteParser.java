package org.elegoff.plugins.communityrust.xunit;

import org.codehaus.staxmate.in.ElementFilter;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.utils.ParsingUtils;

import javax.xml.stream.XMLStreamException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class TestSuiteParser implements StaxParser.XmlStreamHandler {
    private List<TestSuite> testSuites = new ArrayList<>();

    @Override
    public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
        SMInputCursor testSuiteCursor = rootCursor.constructDescendantCursor(new ElementFilter("testsuite"));
        while (testSuiteCursor.getNext() != null) {
            String testSuiteClassName = getExpectedAttribute(testSuiteCursor, "name");
            TestSuite testSuite = new TestSuite(testSuiteClassName);
            testSuites.add(testSuite);
            SMInputCursor testCaseCursor = testSuiteCursor.childElementCursor("testcase");

            while (testCaseCursor.getNext() != null) {
                testSuite.addTestCase(parseTestCaseTag(testCaseCursor));
            }
        }
    }

    public Collection<TestSuite> getParsedReports() {
        return testSuites;
    }

    private static TestCase parseTestCaseTag(SMInputCursor testCaseCursor) throws XMLStreamException {
        String name = parseTestCaseName(testCaseCursor);
        Double time = parseTime(testCaseCursor);
        TestCaseStatus status = TestCaseStatus.OK;
        String stack = "";
        String msg = "";

        String file = testCaseCursor.getAttrValue("file");
        String testClassName = testCaseCursor.getAttrValue("classname");

        SMInputCursor childCursor = testCaseCursor.childElementCursor();
        if (childCursor.getNext() != null) {
            String elementName = childCursor.getLocalName();
            TestCaseStatus testCaseStatus = TestCaseStatus.getFromIgnoreCaseString(elementName);
            if (TestCaseStatus.SKIPPED.equals(testCaseStatus)) {
                status = TestCaseStatus.SKIPPED;
            } else if (TestCaseStatus.FAILURE.equals(testCaseStatus)) {
                status = TestCaseStatus.FAILURE;
                msg = getExpectedAttribute(childCursor, "message");
                stack = childCursor.collectDescendantText();
            } else if (TestCaseStatus.ERROR.equals(testCaseStatus)) {
                status = TestCaseStatus.ERROR;
                msg = getExpectedAttribute(childCursor, "message");
                stack = childCursor.collectDescendantText();
            }
        }
        return new TestCase(name, status, stack, msg, time.intValue(), file, testClassName);
    }

    private static double parseTime(SMInputCursor testCaseCursor) throws XMLStreamException {
        double time;
        try {
            double tmp = ParsingUtils.parseNumber(getExpectedAttribute(testCaseCursor, "time"), Locale.ENGLISH);
            time = ParsingUtils.scaleValue(tmp * 1000, 3);
        } catch (ParseException e) {
            throw new XMLStreamException(e);
        }
        return time;
    }

    private static String getExpectedAttribute(SMInputCursor testCaseCursor, String attributeName) throws XMLStreamException {
        String attrValue = testCaseCursor.getAttrValue(attributeName);
        if (attrValue == null) {
            throw new IllegalStateException(String.format("Missing attribute '%s' at line %d", attributeName, testCaseCursor.getStreamLocation().getLineNumber()));
        }
        return attrValue;
    }

    private static String parseTestCaseName(SMInputCursor testCaseCursor) throws XMLStreamException {
        String name = getExpectedAttribute(testCaseCursor, "name");
        String classname = testCaseCursor.getAttrValue("CLASSNAME");
        if (classname != null) {
            name = classname + "/" + name;
        }
        return name;
    }
}
