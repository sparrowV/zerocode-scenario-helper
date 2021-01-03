package odvali;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SchemaTest {

    @Test
    public void testSchema() {
        try (InputStream inputStream = getClass().getResourceAsStream("/schema.json")) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            validateFiles(schema);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void validateFiles(Schema schema) {
        String filePath = "";
        String fileToString = "";
        try {
//            schema.validate(new JSONObject("{\"hello\" : \"world\"}"));
            URL resource = getClass().getResource("/scenarios");
            File f = new File(resource.toURI());
            List<File> files = new ArrayList<>();
            search(f, files);
            for (File file : files) {
                filePath = file.getAbsolutePath();
                fileToString = readFile(file);
                schema.validate(new JSONObject(fileToString));
            }
        } catch (ValidationException e) {
            System.out.println(e.toJSON());
            System.out.println(filePath);
            assert false;
        } catch (URISyntaxException | JSONException | FileNotFoundException e) {
            System.out.println(filePath);
            System.out.println(fileToString);
            System.out.println(e);
            assert false;
        }


    }

    private String readFile(File f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        StringBuilder builder = new StringBuilder();
        while (sc.hasNext()) {
            builder.append(sc.next());
        }
        return builder.toString();
    }

    public static void search(final File folder, List<File> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(f, result);
            }

            if (f.isFile()) {
                result.add(f);
            }

        }
    }
}
