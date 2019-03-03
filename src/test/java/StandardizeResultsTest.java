import TriResultsJava.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StandardizeResultsTest {

    private List<Column> config;
    private Map<String,String> members;
    private StandardizeResultsCsv standardizeResultsCsv;
    private String[] csvLines;

    @BeforeEach
    void setUp() {
        config = ConfigHelper.Load();
        standardizeResultsCsv = new StandardizeResultsCsv(new CsvColumnValidator("test.csv"), new ColumnStandardizer(config));

        members = new HashMap<String,String>();
        members.put("Jens Rasmussen", "Jens Rasmussen");
        csvLines = new String[]{"Name;Plts;Total", "Jens Rasmussen;4;4:52:34","T Test;5;5:00:01"};
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parseTest() {

        List<List<String>> results = standardizeResultsCsv.FilterOnName(csvLines, members);

        assertTrue(results.size() == 2);

    }
}