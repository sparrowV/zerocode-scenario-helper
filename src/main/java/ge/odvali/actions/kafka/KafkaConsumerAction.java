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

public class KafkaConsumerAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            Document document = editor.getDocument();
            JsonNode jsonNode = Utils.OBJECT_MAPPER.readTree(document.getText());
            var steps = jsonNode.get("steps");

            ((ArrayNode) steps).add(createConsumeTemplateStep());
            WriteCommandAction.runWriteCommandAction(e.getProject(), () -> document.setText(jsonNode.toPrettyString().replace("\r",
                    ""))
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObjectNode createConsumeTemplateStep() {
        ObjectNode objectNode = Utils.OBJECT_MAPPER.createObjectNode();
        objectNode.put("name", "");
        objectNode.put("url", "kafka-topic:");
        objectNode.put("method", "consume");
        objectNode.putIfAbsent("request", Utils.OBJECT_MAPPER.createObjectNode());

        //verify block
        objectNode.putIfAbsent("verify", Utils.OBJECT_MAPPER.createObjectNode());

        return objectNode;
    }
}
