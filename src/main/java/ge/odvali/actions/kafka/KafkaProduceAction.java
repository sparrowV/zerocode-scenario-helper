package ge.odvali.actions.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class KafkaProduceAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(document.getText());
            var steps = jsonNode.get("steps");

            ((ArrayNode) steps).add(createProduceTemplateStep());
            WriteCommandAction.runWriteCommandAction(e.getProject(), () -> document.setText(jsonNode.toPrettyString().replace("\r",
                    ""))
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObjectNode createProduceTemplateStep() {
        ObjectNode objectNode = Utils.OBJECT_MAPPER.createObjectNode();
        objectNode.put("name", "");
        objectNode.put("url", "kafka-topic:");
        objectNode.put("method", "produce");
        ObjectNode request = Utils.OBJECT_MAPPER.createObjectNode();
        request.putIfAbsent("records", Utils.OBJECT_MAPPER.createArrayNode());
        objectNode.putIfAbsent("request", request);

        //verify block
        ObjectNode verify = Utils.OBJECT_MAPPER.createObjectNode();
        verify.put("status", "Ok");
        objectNode.putIfAbsent("verify", verify);

        return objectNode;
    }
}
