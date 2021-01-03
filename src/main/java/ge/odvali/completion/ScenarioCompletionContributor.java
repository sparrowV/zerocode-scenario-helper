package ge.odvali.completion;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jsoup.internal.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScenarioCompletionContributor extends CompletionContributor {
    private static List<String> scenarioKeywords;

    static {
        try {
            JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(
                    ScenarioCompletionContributor.class.getClassLoader().getResourceAsStream("keywords.json"))));
            scenarioKeywords = Arrays.<String>asList(new Gson().fromJson(reader, String[].class));
        } catch (Exception e) {
            System.out.println("Cant read keywords");
        }
    }


    public ScenarioCompletionContributor() {
        String stringToRemove = "IntellijIdeaRulezzz";
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PsiElement.class), new CompletionProvider<CompletionParameters>() {
            protected void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet resultSet) {
                PsiElement context = completionParameters.getPosition().getContext();
                if (context == null || context.getContainingFile() == null || !context.getContainingFile().getOriginalFile().getVirtualFile().getPath().contains("/src/test/resources"))
                    return;
//context.getContainingFile().getOriginalFile().getParent().getVirtualFile().getPath()
                String psiText = context.getText()
                        .replace(stringToRemove, "");
                if (psiText.length() > 1 && psiText.charAt(0) == '"' && psiText.charAt(psiText.length() - 1) == '"') {
                    psiText = psiText.substring(1, psiText.length() - 1);
                }
                psiText = psiText.trim();

                if (!StringUtil.isBlank(psiText)) {
                    resultSet.addAllElements(getSuggestedWordsToDisplay(psiText));
                }
            }
        });
    }

    private List<LookupElement> getSuggestedWordsToDisplay(String userInput) {
        List<LookupElement> res = new ArrayList<>();
        scenarioKeywords.forEach(scenarioKeyword -> {
            if (scenarioKeyword.startsWith(userInput)) {
                res.add(LookupElementBuilder.
                        create(scenarioKeyword));
            }
        });
        return res;
    }

}
