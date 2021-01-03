package odvali;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ScenarioHighlightTest extends BasePlatformTestCase {

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
    public void testZeroHighlight() {
        myFixture.configureByFile("/src/test/resources/zero_highlight_scenario.json");
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting();
        List<HighlightInfo> errorHighlights = new ArrayList<>();
        highlightInfos.forEach(h -> {
            if (h.getSeverity().equals(HighlightSeverity.ERROR)) {
                errorHighlights.add(h);
            }
        });
        assertEquals(0, errorHighlights.size());
    }

    @Test
    public void testSingleHighlight() {
        myFixture.configureByFile("/src/test/resources/single_highlight_scenario.json");
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting();
        assertTrue(highlightInfos.size() > 0);

        HighlightInfo highlightInfo = highlightInfos.get(0);
        assertTrue(highlightInfo.getDescription().contains("not_allowed"));
        assertEquals(highlightInfo.getSeverity(), HighlightSeverity.ERROR);
    }


}
