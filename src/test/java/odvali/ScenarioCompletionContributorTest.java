package odvali;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScenarioCompletionContributorTest extends BasePlatformTestCase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    protected String getTestDataPath() {
        return System.getProperty("user.dir");
    }


    @Test
    public void testSingleSuggestions() throws Exception {
        myFixture.configureByFile("src/test/resources/singleSuggestionBefore.json");
        myFixture.type("sc");
        codeComplete();
        myFixture.type(" req");
        codeComplete();
        myFixture.type(" ur");
        codeComplete();
        myFixture.type(" asser");
        codeComplete();
        myFixture.checkResultByFile("src/test/resources/singleSuggestionAfter.json");
    }

    @Test
    public void testMultipleSuggestions() {
        myFixture.configureByFile("src/test/resources/multipleSuggestion.json");
        myFixture.type("ver");
        codeComplete();
        LookupElement[] lookupElements = myFixture.completeBasic();
        assertTrue(lookupElements.length > 1);

        myFixture.type(" m");
        codeComplete();
        LookupElement[] lookupElements2 = myFixture.completeBasic();
        assertTrue(lookupElements2.length > 1);
    }


    private void codeComplete() {
        myFixture.performEditorAction("CodeCompletion");
    }

}
