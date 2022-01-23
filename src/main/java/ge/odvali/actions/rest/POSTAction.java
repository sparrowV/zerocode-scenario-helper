package ge.odvali.actions.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import ge.odvali.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class POSTAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            Document document = editor.getDocument();
            JsonNode jsonNode = Utils.OBJECT_MAPPER.readTree(document.getText());
            var steps = jsonNode.get("steps");

            ((ArrayNode) steps).add(createPOSTTemplateStep());
            WriteCommandAction.runWriteCommandAction(e.getProject(), () -> document.setText(jsonNode.toPrettyString().replace("\r",
                    ""))
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObjectNode createPOSTTemplateStep() {
        ObjectNode objectNode = Utils.OBJECT_MAPPER.createObjectNode();
        objectNode.put("name", "");
        objectNode.put("url", "/");
        objectNode.put("method", "POST");

        ObjectNode requestBodyTemplate = Utils.OBJECT_MAPPER.createObjectNode();
        requestBodyTemplate.putIfAbsent("body", Utils.OBJECT_MAPPER.createObjectNode());
        ObjectNode headersTemplate = Utils.OBJECT_MAPPER.createObjectNode();
        headersTemplate.put("Content-Type", "application/json");
        requestBodyTemplate.putIfAbsent("headers", headersTemplate);
        requestBodyTemplate.putIfAbsent("body", Utils.OBJECT_MAPPER.createObjectNode());

        objectNode.putIfAbsent("request", requestBodyTemplate);

        //verify block
        ObjectNode verify = Utils.OBJECT_MAPPER.createObjectNode();
        verify.put("status", 201);
        objectNode.putIfAbsent("verify", verify);

        return objectNode;
    }
}
