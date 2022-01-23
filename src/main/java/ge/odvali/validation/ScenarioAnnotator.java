package ge.odvali.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.intellij.lang.annotation.*;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.Set;

public class ScenarioAnnotator implements Annotator {
    protected static final Key<Boolean> containsZeroCodeSchemaErrors = Key.create("CONTAINS_ZEROCODE_SCHEMA_ERRORS");
    private static JsonSchema schema;
    private static ObjectMapper jsonMapper;
    private static ObjectMapper yamlMapper;

    static {
        JsonSchemaFactory instance = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        InputStream inputStream = ScenarioAnnotator.class.getResourceAsStream("/schema.json");
        schema = instance.getSchema(inputStream);
        jsonMapper = new ObjectMapper();
        yamlMapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        PsiFile containingFile = element.getContainingFile();
        if (!containingFile.getVirtualFile().getPath().contains("/src/test/resources")) return;
        boolean forYaml = containingFile.getVirtualFile().getFileType().getDefaultExtension().equalsIgnoreCase("yml");
        String jsonFile = containingFile.getText();
        validateFile(jsonFile, holder, element, forYaml);
    }


    private void validateFile(String jsonFile, AnnotationHolder holder, PsiElement psiElement, boolean forYaml) {
        AnnotationSession currentAnnotationSession = holder.getCurrentAnnotationSession();
        try {
            Set<ValidationMessage> validationMessages;
            if (forYaml)
                validationMessages = schema.validate(yamlMapper.readTree(jsonFile));
            else
                validationMessages = schema.validate(jsonMapper.readTree(jsonFile));
            Boolean userData = currentAnnotationSession.getUserData(containsZeroCodeSchemaErrors);
            if (validationMessages.size() == 0) {
                currentAnnotationSession.putUserData(containsZeroCodeSchemaErrors, null);

            }
            if (userData == null) {
                for (ValidationMessage validationMessage : validationMessages) {
                    holder.newAnnotation(HighlightSeverity.ERROR, validationMessage.getMessage())
                            .afterEndOfLine().range(new TextRange(0, 1)) //this creates exceptions sometimes
                            .create();
                    currentAnnotationSession.putUserData(containsZeroCodeSchemaErrors, true);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
