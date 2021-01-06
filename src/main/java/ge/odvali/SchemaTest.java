//package ge.odvali;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import com.networknt.schema.JsonSchema;
//import com.networknt.schema.JsonSchemaFactory;
//import com.networknt.schema.SpecVersion;
//import com.networknt.schema.ValidationMessage;
//
//import java.io.InputStream;
//import java.util.Set;
//
//public class SchemaTest {
//    //    https://www.jsonschemavalidator.net/
////    https://extendsclass.com/json-schema-validator.html
////    https://json-schema.org/understanding-json-schema/reference/object.html
////    https://github.com/everit-org/json-schema#draft-4-draft-6-or-draft-7
////    public static void main(String[] args) throws JsonProcessingException {
////        JsonSchemaFactory instance = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
////        InputStream inputStream = SchemaTest.class.getResourceAsStream("/schema.json");
////        JsonSchema schema = instance.getSchema(inputStream);
////        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
////        JsonNode node = mapper.readTree("{\n" +
////                "  \"scenarioNames\": \"qwe\",\n" +
////                "  \"steps\": [\n" +
////                "    {\n" +
////                "      \"name\": \"qwe\",\n" +
////                "      \"url\": \"adasd\",\n" +
////                "      \"method\": \"GET\"\n" +
////                "    }\n" +
////                "  ]\n" +
////                "}");
//////        Set<ValidationMessage> validate = schema.validate(node);
//////        validate.forEach(elem -> {
//////            System.out.println(elem.getMessage());
//////        });
////        mapper.readTree("scenarioNames: qwe\n" +
////                "steps:\n" +
////                "- name: qwe\n" +
////                "  url: adasd\n" +
////                "  method: GET");
////        Set<ValidationMessage> validate = schema.validate(node);
////        validate.forEach(elem -> {
////            System.out.println(elem.getMessage());
////        });
//
//    }
//}
