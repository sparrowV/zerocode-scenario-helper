package ge.odvali.validation;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class ScenarioAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        PsiFile containingFile = element.getContainingFile();
        if (!containingFile.getVirtualFile().getPath().contains("/src/test/resources")) return;
        String jsonFile = containingFile.getText();
        validateFile(jsonFile, holder, element);
    }


    private void validateFile(String jsonFile, AnnotationHolder holder, PsiElement psiElement) {
        try (InputStream inputStream = getClass().getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(jsonFile));
        } catch (ValidationException e) {
            holder.newAnnotation(HighlightSeverity.ERROR, e.getAllMessages().get(0)).tooltip("qqq")
                    .fileLevel()
                    .create();


        } catch (Exception e) {
//            e.printStackTrace();

        }
    }

}
