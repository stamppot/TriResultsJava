import TriResultsJava.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
            StandardizeResultsCsv standardizer = new StandardizeResultsCsv(new CsvColumnValidator(filename), new ColumnStandardizerSql(config));
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


    @Test
    void ReadAllAndOutputToSqlTest() {
        String resourceDirectory = Paths.get("src","test","resources", "uitslagen").toString();

        File folder = new File(resourceDirectory);

        List<String> filenames = new ArrayList<>();

//        for(final File fileEntry : folder.listFiles((dir, name) -> (name.startsWith("2018") || name.startsWith("2019")) && name.endsWith(".csv"))) {
            for(final File fileEntry : folder.listFiles((dir, name) -> name.endsWith(".csv"))) {
            filenames.add(fileEntry.getName());
        }

        TreeMap<RaceInfo,List<List<String>>> csvResults = new TreeMap<>();

        CreateOutputDirectory();

        List<String> filesWithoutMembers = new ArrayList<String>();

        for(String filename : filenames) {
            String fullPath = Paths.get(resourceDirectory, filename).toString();


            System.out.println(filename);
            StandardizeResultsCsv standardizer = new StandardizeResultsCsv(new CsvColumnValidator(filename), new ColumnStandardizerSql(config));
            List<List<String>> csvValues = standardizer.readAndStandardizeFromFile(fullPath, members);
            if(csvValues.size() > 0) {
                RaceInfo race = RaceInfo.create(filename);
                race.setCsvValues(csvValues);
                csvResults.put(race, csvValues);
            }
            else {
                System.out.println("length: " + csvValues.size());
                filesWithoutMembers.add(filename);
            }
//            String sqlFilename = fullPath.replace("csv", "sql");
        }


        filesWithoutMembers.sort(Comparator.naturalOrder());
        System.out.println("Unused files:");
        for(String f : filesWithoutMembers) {
            System.out.println(f);
        }
        Path outputDir = Paths.get(resourceDirectory).getParent().resolve("output").resolve("sql");
        CreateOutputDirectory(outputDir);

//        for(Map.Entry<String,List<List<String>>> entry : csvResults.entrySet()) {
//            String sqlFilename = entry.getKey().replace("csv", "sql");
//            List<List<String>> csvValues = entry.getValue();
//            IResultsOutput sqlOutput = new SqlResultsOutput();
//            WriteOutput(sqlOutput, outputDir, sqlFilename, csvValues);
//        }

        // output all sql to one file
        StringBuilder sqlOutputSb = new StringBuilder();
        for(RaceInfo race : csvResults.descendingKeySet()) {
            race.setCsvValues(race.getCsvValues());
            IResultsOutput sqlOutputBuilder = new SqlResultsOutput();
            sqlOutputSb.append(sqlOutputBuilder.Generate(race));
        }
        String filename = DateHelper.toString(LocalDateTime.now()) + "-output.sql";
        Path outputPath = outputDir.resolve(filename);
        System.out.println("output path: " + outputPath);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toString(), false))) {
            writer.write(sqlOutputSb.toString());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }


    @Test
    void Read2018AndOutputToHtmlTest() {
        String resourceDirectory = Paths.get("src","test","resources", "uitslagen").toString();

        File folder = new File(resourceDirectory);

        List<String> filenames = new ArrayList<>();

        for(final File fileEntry : folder.listFiles((dir, name) -> (name.startsWith("2018") || name.startsWith("2019")) && name.endsWith(".csv"))) {
            filenames.add(fileEntry.getName());
        }

        TreeMap<RaceInfo,List<List<String>>> csvResults = new TreeMap<>();

        CreateOutputDirectory();

        for(String filename : filenames) {
            String fullPath = Paths.get(resourceDirectory, filename).toString();


            System.out.println(filename);
            StandardizeResultsCsv standardizer = new StandardizeResultsCsv(new CsvColumnValidator(filename), new ColumnStandardizer(config));
            List<List<String>> csvValues = standardizer.readAndStandardizeFromFile(fullPath, members);
            if(csvValues.size() > 0) {
                RaceInfo race = RaceInfo.create(filename);
                race.setCsvValues(csvValues);
                csvResults.put(race, csvValues);
            }
            else {
                System.out.println("length: " + csvValues.size() + ", filename: " + filename);
            }
        }


        Path outputDir = Paths.get(resourceDirectory).getParent().resolve("output").resolve("sql");
        CreateOutputDirectory(outputDir);

        // output all sql to one file
        StringBuilder htmlOutputSb = new StringBuilder();
        for(RaceInfo key : csvResults.descendingKeySet()) {
//            List<List<String>> csvValues = csvResults.get(key);
            IResultsOutput htmlOutputBuilder = new HtmlResultsOutput();
            htmlOutputSb.append(htmlOutputBuilder.Generate(key));
        }
        String filename = DateHelper.toString(LocalDateTime.now()) + "-html_output.html";
        Path outputPath = outputDir.resolve(filename);
        System.out.println("output path: " + outputPath);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toString(), false))) {
            writer.write(htmlOutputSb.toString());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }



    private void CreateOutputDirectory() {
        // make sure output dir exists
        Path outputDirectory = Paths.get("src","test","resources", "output");
        try {
            if(!Files.exists(outputDirectory)) {
                Files.createDirectory(outputDirectory);
            }
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void CreateOutputDirectory(Path dir) {
        // make sure output dir exists
        try {
            if(!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
        } catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
