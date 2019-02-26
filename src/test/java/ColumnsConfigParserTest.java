import TriResultsJava.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ColumnsConfigParserTest {

    private ColumnsConfigParser parser;
    private String filename;

    @BeforeEach
    void setUp() {
//        String cwd = new File(ColumnsConfigParserTest.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toString();
        String resourceDirectory = Paths.get("src","test","resources").toString();
        Path p = Paths.get(resourceDirectory,"column_config.xml");
        filename =  p.toString();

        parser = new ColumnsConfigParser();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parseTest() {

        List<Column> columns = new ArrayList<Column>();

        try {
            columns = parser.parse(filename);
        } catch(IOException | BadConfigurationException e) {
            System.out.println(e.getMessage());
        }

        assertTrue(columns.size() > 0);


    }
}
