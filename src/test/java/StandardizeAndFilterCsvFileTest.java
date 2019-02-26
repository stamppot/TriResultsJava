import TriResultsJava.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StandardizeAndFilterCsvFileTest {

    private ColumnsConfigParser parser;
    private String filename;
    private List<Column> config;
    private List<String> members;
    private ICsvHelper csvHelper = new MyCsvHelper();

    @BeforeEach
    void setUp() {
        String resourceDirectory = Paths.get("src","test","resources").toString();
        Path p = Paths.get(resourceDirectory,"column_config.xml");
        filename = p.toString();
        parser = new ColumnsConfigParser();
        try {
            config = parser.parse(filename);
        } catch(IOException | BadConfigurationException ex) {
            System.out.printf("Error: couldn't read config: " + ex.getMessage());
        }

        String memberFile = Paths.get(resourceDirectory,"leden2018.csv").toString();

        // read members file
        members = csvHelper.readFile(memberFile, "Naam");
        members.remove(0);


    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void ReadAllTest() {
        String resourceDirectory = Paths.get("src","test","resources", "uitslagen").toString();

        File folder = new File(resourceDirectory);

        List<String> filenames = new ArrayList<>();

        for(final File fileEntry : folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".csv");
            }
        })) {
            filenames.add(fileEntry.getName());
        }


        Map<String,String> csvResults = new HashMap<>();

        for(String filename : filenames) {
            String fullPath = Paths.get(resourceDirectory, filename).toString();

            System.out.println(filename);
            StandardizeResultsCsv standardizer = new StandardizeResultsCsv(config, new CsvColumnValidator(filename));
            List<List<String>> csvValues = standardizer.readAndStandardizeFromFile(fullPath, members);

            if(csvValues.size() > 0) {
                String csv = csvHelper.join(csvValues);
                csvResults.put(filename, csv);
            }
        }


        Path outputDirectory = Paths.get("src","test","resources", "output");

        try {
            if(!Files.exists(outputDirectory)) {
                Files.createDirectory(outputDirectory);
            }
        } catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
        }


        for(Map.Entry<String,String> entry : csvResults.entrySet()) {

            String filename = entry.getKey();
            String fullPath = Paths.get(outputDirectory.toString(), filename).toString();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, true));
                    writer.write(entry.getValue());
                    writer.close();
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
