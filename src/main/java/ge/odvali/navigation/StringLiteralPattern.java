package ge.odvali.navigation;

import com.intellij.json.JsonElementTypes;
import com.intellij.json.JsonLanguage;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementTypes;
import org.jetbrains.yaml.YAMLLanguage;

public class StringLiteralPattern extends PatternCondition<PsiElement> {
    public StringLiteralPattern() {
        super("");
    }

    @Override
    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
        boolean forJson = PlatformPatterns.psiElement()
                .withLanguage(JsonLanguage.INSTANCE)
                .withElementType(JsonElementTypes.STRING_LITERAL)
                .notNull()
                .withParent(
                        PlatformPatterns.psiElement()
                                .withLanguage(JsonLanguage.INSTANCE)
                                .notNull()
                                .withElementType(JsonElementTypes.PROPERTY)
                                .withName("url")
                )
                .accepts(element, context) ||
                (PlatformPatterns.psiElement()
                        .withLanguage(JsonLanguage.INSTANCE)
                        .notNull()
                        .withElementType(JsonElementTypes.STRING_LITERAL)
                        .withParent(
                                PlatformPatterns.psiElement()
                                        .withLanguage(JsonLanguage.INSTANCE)
                                        .notNull()
                                        .withElementType(JsonElementTypes.PROPERTY)

                        ).accepts(element, context) && (element.getText().contains("JSON.FILE:") || element.getText().contains("XML" +
                        ".FILE:")));
        if (forJson) return forJson;
        return
                PlatformPatterns.psiElement()
                        .withLanguage(YAMLLanguage.INSTANCE)
                        .withElementType(YAMLElementTypes.SCALAR_TEXT_VALUE)
                        .notNull()
                        .withParent(
                                PlatformPatterns.psiElement()
                                        .withLanguage(YAMLLanguage.INSTANCE)
                                        .notNull()
                                        .withElementType(YAMLElementTypes.KEY_VALUE_PAIR)
                                        .withName("url")


                        )
                        .accepts(element, context) ||
                        (PlatformPatterns.psiElement()
                                .withLanguage(YAMLLanguage.INSTANCE)
                                .notNull()
                                .withElementType(YAMLElementTypes.SCALAR_TEXT_VALUE)
                                .withParent(
                                        PlatformPatterns.psiElement()
                                                .withLanguage(YAMLLanguage.INSTANCE)
                                                .notNull()
                                                .withElementType(YAMLElementTypes.KEY_VALUE_PAIR)

                                )
                                .accepts(element, context) && (element.getText().contains("JSON.FILE:") || element.getText().contains("XML" +
                                ".FILE:")));

    }
}
