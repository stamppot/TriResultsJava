import TriResultsJava.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ColumnStandardizerTest {

    private ColumnStandardizer standardizer;
    private List<Column> config;
    private List<String> columnNames;

    @BeforeEach
    void setUp() {

        config = ConfigHelper.Load();

        columnNames = Arrays.asList(new String[] { "Plts", "StNr", "Name", "Woonplaats", "PltsCat",  "Categ",
                "ZwemTijd", "#Zwem", "Wissel1", "#W1", "#NaW1", "Fiets", "km/h", "#Fiets", "#Fie", "Wis2", "#w2",
                "Lopen", "PltsLoop", "Finish" });

        standardizer = new ColumnStandardizer(config);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parseTest() {

        List<String> standardizedColumns = null;
        standardizedColumns = standardizer.GetStandardColumnNames(columnNames);

        assertTrue(standardizedColumns.contains("Pos"));
        assertFalse(standardizedColumns.contains("Plts"));


    }

//    private List<Column> ReadConfig() {
//        String resourceDirectory = Paths.get("src","test","resources").toString();
//        Path p = Paths.get(resourceDirectory,"column_config.xml");
//        String filename =  p.toString();
//
//        ColumnsConfigParser parser = new ColumnsConfigParser();
//
//        List<Column> config = null;
//
//        try {
//            config = parser.parse(filename);
//        } catch(IOException | BadConfigurationException e) {
//            System.out.println("ConfigError: " + e.getMessage());
//        }
//
//        return config;
//    }
}
