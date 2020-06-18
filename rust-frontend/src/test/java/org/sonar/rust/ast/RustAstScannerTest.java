package org.sonar.rust.ast;

import com.google.common.collect.Lists;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;
import com.sonar.sslr.api.typed.GrammarBuilder;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;
import org.sonar.plugins.rust.api.RustFileScanner;
import org.sonar.plugins.rust.api.RustFileScannerContext;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.plugins.rust.api.tree.TreeVisitor;
import org.sonar.rust.*;
import org.sonar.rust.ast.parser.RustNodeBuilder;
import org.sonar.rust.ast.parser.RustParser;
import org.sonar.rust.model.InternalSyntaxToken;
import org.sonar.rust.model.RustTree;
import org.sonar.rust.model.VisitorsBridge;
import org.sonar.rust.tree.SyntaxToken;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;

import java.io.File;
import java.io.InterruptedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RustAstScannerTest {


        @Rule
        public ExpectedException thrown = ExpectedException.none();
        @Rule
        public LogTester logTester = new LogTester();
        private SensorContextTester context;

        @Before
        public void setUp() throws Exception {
        context = SensorContextTester.create(new File(""));
    }
/*
    @Test
    public void comments() {
        InputFile inputFile = TestUtils.inputFile("src/test/files/metrics/Comments.rs");
        NoSonarFilter noSonarFilter = mock(NoSonarFilter.class);
        RustAstScanner.scanSingleFileForTests(inputFile, new VisitorsBridge(new Measurer(context, noSonarFilter)));
        verify(noSonarFilter).noSonarInFile(inputFile, Collections.singleton(15));
    }

        @Test
        public void noSonarLines() throws Exception {
        InputFile inputFile = TestUtils.inputFile("src/test/files/metrics/NoSonar.java");
        NoSonarFilter noSonarFilter = mock(NoSonarFilter.class);
        RustAstScanner.scanSingleFileForTests(inputFile, new VisitorsBridge(new Measurer(context, noSonarFilter)));
        verify(noSonarFilter).noSonarInFile(inputFile, Collections.singleton(8));
        //No Sonar on tests files
        NoSonarFilter noSonarFilterForTest = mock(NoSonarFilter.class);
        RustAstScanner.scanSingleFileForTests(inputFile, new VisitorsBridge(new Measurer(context, noSonarFilterForTest).new TestFileMeasurer()));
        verify(noSonarFilterForTest).noSonarInFile(inputFile, Collections.singleton(8));
    }

        @Test
        public void scan_single_file_with_dumb_file_should_fail() throws Exception {
        thrown.expect(AnalysisException.class);
        String filename = "!!dummy";
        thrown.expectMessage(filename);
        RustAstScanner.scanSingleFileForTests(TestUtils.emptyInputFile(filename), new VisitorsBridge(null));
    }

        @Test
        public void should_not_fail_whole_analysis_upon_parse_error_and_notify_audit_listeners() {
        FakeAuditListener listener = spy(new FakeAuditListener());
        RustAstScanner scanner = defaultRustAstScanner();
        scanner.setVisitorBridge(new VisitorsBridge(listener));

        scanner.scan(Collections.singletonList(TestUtils.inputFile("src/test/resources/AstScannerParseError.txt")));
        verify(listener).processRecognitionException(any(RecognitionException.class));
    }

        @Test
        public void should_handle_analysis_cancellation() throws Exception {
        RustFileScanner visitor = spy(new RustFileScanner() {
            @Override
            public void scanFile(RustFileScannerContext context) {
                // do nothing
            }
        });
        SonarComponents sonarComponents = mock(SonarComponents.class);
        when(sonarComponents.analysisCancelled()).thenReturn(true);
        RustAstScanner scanner = new RustAstScanner(RustParser.createParser(), sonarComponents);
        scanner.setVisitorBridge(new VisitorsBridge(Lists.newArrayList(visitor), sonarComponents));
        scanner.scan(Collections.singletonList(TestUtils.inputFile("src/test/files/metrics/NoSonar.java")));
        verifyZeroInteractions(visitor);
    }

        @Test
        public void should_interrupt_analysis_when_InterruptedException_is_thrown() {
        InputFile inputFile = TestUtils.inputFile("src/test/files/metrics/NoSonar.java");

        thrown.expectMessage("Analysis cancelled");
        thrown.expect(new AnalysisExceptionBaseMatcher(RecognitionException.class, "instanceof AnalysisException with RecognitionException cause"));

        RustAstScanner.scanSingleFileForTests(inputFile, new VisitorsBridge(new CheckThrowingException(new RecognitionException(42, "interrupted", new InterruptedException()))));
    }

        @Test
        public void should_interrupt_analysis_when_InterruptedIOException_is_thrown() {
        InputFile inputFile = TestUtils.inputFile("src/test/files/metrics/NoSonar.java");

        thrown.expectMessage("Analysis cancelled");
        thrown.expect(new AnalysisExceptionBaseMatcher(RecognitionException.class, "instanceof AnalysisException with RecognitionException cause"));

        RustAstScanner.scanSingleFileForTests(inputFile, new VisitorsBridge(new CheckThrowingException(new RecognitionException(42, "interrupted", new InterruptedIOException()))));
    }

        @Test
        public void should_swallow_log_and_report_checks_exceptions() {
        RustAstScanner scanner = defaultRustAstScanner();
        SonarComponents sonarComponent = new SonarComponents(null, context.fileSystem(),  null);
        sonarComponent.setSensorContext(context);
        scanner.setVisitorBridge(new VisitorsBridge(Collections.singleton(new CheckThrowingException(new NullPointerException("foo"))),  sonarComponent));
        InputFile scannedFile = TestUtils.inputFile("src/test/resources/AstScannerNoParseError.txt");

        scanner.scan(Collections.singletonList(scannedFile));
        assertThat(logTester.logs(LoggerLevel.ERROR)).hasSize(1).contains("Unable to run check class org.sonar.java.ast.JavaAstScannerTest$CheckThrowingException -  on file '"
                + scannedFile.toString()
                + "', To help improve SonarJava, please report this problem to SonarSource : see https://www.sonarqube.org/community/");
        assertThat(sonarComponent.analysisErrors).hasSize(1);
        assertThat(sonarComponent.analysisErrors.get(0).getKind()).isSameAs(AnalysisError.Kind.CHECK_ERROR);
        logTester.clear();
        scanner.setVisitorBridge(new VisitorsBridge(new AnnotatedCheck(new NullPointerException("foo"))));
        scannedFile = TestUtils.inputFile("src/test/resources/AstScannerParseError.txt");
        scanner.scan(Collections.singletonList(scannedFile));
        assertThat(logTester.logs(LoggerLevel.ERROR)).hasSize(3).contains("Unable to run check class org.sonar.java.ast.JavaAstScannerTest$AnnotatedCheck - AnnotatedCheck on file '"
                + scannedFile.toString()
                + "', To help improve SonarJava, please report this problem to SonarSource : see https://www.sonarqube.org/community/");
    }



        @Test
        public void should_propagate_SOError() {
        thrown.expect(StackOverflowError.class);
        RustAstScanner scanner = defaultRustAstScanner();
        scanner.setVisitorBridge(new VisitorsBridge(new CheckThrowingSOError()));
        scanner.scan(Collections.singletonList(TestUtils.inputFile("src/test/resources/AstScannerNoParseError.txt")));

        assertThat(logTester.logs(LoggerLevel.ERROR)).hasSize(1);
        assertThat(logTester.logs(LoggerLevel.ERROR).get(0))
                .startsWith("A stack overflow error occured while analyzing file")
                .contains("java.lang.StackOverflowError: boom")
                .contains("at org.sonar.java.ast.JavaAstScannerTest");
    }

        @Test
        public void should_report_analysis_error_in_sonarLint_context_withSQ_6_0() {
        RustAstScanner scanner = defaultRustAstScanner();
        FakeAuditListener listener = spy(new FakeAuditListener());
        SonarComponents sonarComponents = mock(SonarComponents.class);
        when(sonarComponents.reportAnalysisError(any(RecognitionException.class), any(InputFile.class))).thenReturn(true);
        scanner.setVisitorBridge(new VisitorsBridge(Lists.newArrayList(listener),  sonarComponents));
        scanner.scan(Collections.singletonList(TestUtils.inputFile("src/test/resources/AstScannerParseError.txt")));
        verify(sonarComponents).reportAnalysisError(any(RecognitionException.class), any(InputFile.class));
        verifyZeroInteractions(listener);
    }

        private static RustAstScanner defaultRustAstScanner() {
        return new RustAstScanner(new ActionParser<>(StandardCharsets.UTF_8, FakeLexer.builder(), FakeGrammar.class, new FakeTreeFactory(), new RustNodeBuilder(), FakeLexer.ROOT), null);
    }

        private static class CheckThrowingSOError implements RustFileScanner {

            @Override
            public void scanFile(RustFileScannerContext context) {
                throw new StackOverflowError("boom");
            }
        }
        private static class CheckThrowingException implements RustFileScanner {

            private final RuntimeException exception;

            public CheckThrowingException(RuntimeException e) {
                this.exception = e;
            }

            @Override
            public void scanFile(RustFileScannerContext context) {
                throw exception;
            }
        }

        @org.sonar.check.Rule(key = "AnnotatedCheck")
        private static class AnnotatedCheck extends CheckThrowingException {
            public AnnotatedCheck(RuntimeException e) {
                super(e);
            }
        }

        private static class AnalysisExceptionBaseMatcher extends BaseMatcher {

            private final Class<? extends Exception> expectedCause;
            private final String description;

            public AnalysisExceptionBaseMatcher(Class<? extends Exception> expectedCause, String description) {
                this.expectedCause = expectedCause;
                this.description = description;
            }

            @Override
            public boolean matches(Object item) {
                return item instanceof AnalysisException
                        && expectedCause.equals(((AnalysisException) item).getCause().getClass());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(this.description);
            }

        }

        private static class FakeAuditListener implements RustFileScanner, ExceptionHandler {

            @Override
            public void processRecognitionException(RecognitionException e) {
            }

            @Override
            public void processException(Exception e) {
            }

            @Override
            public void scanFile(RustFileScannerContext context) {

            }
        }

        public static class FakeTreeFactory {
            public FakeTreeFactory(){}
            public Tree root(RustTree RustTree) {
                return new Tree() {
                    @Override
                    public boolean is(Tree.Kind... kind) {
                        return false;
                    }

                    @Override
                    public void accept(TreeVisitor visitor) {

                    }

                    @Override
                    public Kind kind() {
                        return null;
                    }

                    @Override
                    public Tree parent() {
                        return null;
                    }

                    @Override
                    public SyntaxToken firstToken() {
                        return null;
                    }

                    @Override
                    public SyntaxToken lastToken() {
                        return null;
                    }
                };
            }
        }

        public static class FakeGrammar {
            final GrammarBuilder<InternalSyntaxToken> b;
            final FakeTreeFactory f;

            public FakeGrammar(GrammarBuilder<InternalSyntaxToken> b, FakeTreeFactory f) {
                this.b = b;
                this.f = f;
            }

            public Tree ROOT() {
                return b.<Tree>nonterminal(FakeLexer.ROOT).is(f.root(b.token(FakeLexer.TOKEN)));
            }
        }

        public enum FakeLexer implements GrammarRuleKey {
            ROOT, TOKEN;

            public static LexerlessGrammarBuilder builder() {
                LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

                b.rule(TOKEN).is("foo");
                b.setRootRule(ROOT);

                return b;
            }

        }
*/

    }

