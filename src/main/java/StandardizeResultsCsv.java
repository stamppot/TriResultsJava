import TriResultsJava.Column;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StandardizeResultsCsv {

    private List<Column> _csvColumnConfig;

    private String _csvColumnConfixXml;
    private ICsvValidator csvValidator;

    public StandardizeResultsCsv(String columnConfigXml) {
        this._csvColumnConfixXml = columnConfigXml;
    }

    public StandardizeResultsCsv(List<Column> columnsConfig, ICsvValidator csvValidator) {

        this._csvColumnConfig = columnsConfig;
        this.csvValidator = csvValidator;
    }

    public final List<List<String>> FilterOnName(String[] csvLines, List<String> memberNames) {
        List<String> standardizedLines = Arrays.asList(ReadAndStandardize(csvLines));
        String headerLine = standardizedLines.get(0);
        String separator = new MyCsvHelper().determineSeparator(headerLine);
        final String delimiter = separator;

        List<List<String>> csvValues = standardizedLines.stream()
                .map(line -> Arrays.asList(line.split(delimiter)))
                .collect(Collectors.toList());

//        csvValues = ReadAndStandardize(csvValues);

        return FilterOnName(csvValues, memberNames);
    }


    public final List<List<String>> FilterOnName(List<List<String>> csvLines, List<String> memberNames) {
        List<List<String>> standardizedResultLines = csvLines; // ReadAndStandardize(csvLines);

//        /* validate csv with business rules */
        if(!csvValidator.isValid( standardizedResultLines)) {
            String errorMsg = "CSV error: " + String.join(",", csvValidator.getErrors());
            System.out.println(errorMsg);
//            throw new RuntimeException(errorMsg);
        }

        if(standardizedResultLines.size() == 0) {
            System.out.println("FilterOnName: No csvLines");
        }
        List<String> headers = standardizedResultLines.get(0);

        /* find index of Name header */
        int nameIndex = headers.indexOf("Naam");

        Map<String, String> memberMap = memberNames.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
        List<List<String>> filteredResults = new ArrayList<>();

        for (List<String> currLine : standardizedResultLines) {  // start after headers
            List<String> columnValues = new ArrayList<>();
            String name = currLine.get(nameIndex);

            /* skip to next if name is not in list */
            if (!memberMap.containsKey(name.trim())) {
                continue;
            } else {
                System.out.println("Name: " + name + "  " + String.join(", ", currLine));
            }

            for (int j = 0; j < headers.size(); j++) {
                int lineSize = currLine.size();
                if (j >= lineSize) {
                    currLine.add(""); // add empty result for fx. DQ column
                }

                String column = headers.get(j);
                String value = currLine.get(j);
//                System.out.println("Add " + column + " : " + value);
                columnValues.add(value);
            }
            if (columnValues.size() > 0) {
                filteredResults.add(columnValues);
            }
        }

        if(filteredResults.size() > 0) {
            // add headers
            filteredResults.add(0, headers);
        }

        return filteredResults;
    }

    /// <summary>
    /// Reads file, standardizes headers, filters results on members
    /// </summary>
    public final List<List<String>> readAndStandardizeFromFile(String filename, List<String> memberNames) {
        String delimiter = ",";

        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine(), delimiter));
            }
        } catch(IOException ex) {
            System.out.printf("File not found: " + filename);
        }

        List<List<String>> standardizedResultLines = ReadAndStandardize(records);

        if(!csvValidator.isValid(standardizedResultLines)) {
            String errorMsg = "CSV error: " + String.join(",", csvValidator.getErrors());
            System.out.println(errorMsg);
            return new ArrayList<List<String>>();
//            throw new RuntimeException(errorMsg);
        }

        return FilterOnName(standardizedResultLines, memberNames);
    }

    private List<String> getRecordFromLine(String line, String delimiter) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(delimiter);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }


    ///  <summary>
    ///  Reads csv file and standardizes columnnames
    ///  </summary>
    ///  <param name="csv"></param>
    public final String[] ReadAndStandardize(String[] csvLines) {
        ICsvHelper csvHelper = new MyCsvHelper();
        List<Column> config = GetColumnConfiguration();
        ColumnStandardizer columnStandardizer = new ColumnStandardizer(config);
        String[] standardizedCsv = csvHelper.replaceHeaders(csvLines, columnStandardizer);
        return standardizedCsv;
    }

    public final List<List<String>> ReadAndStandardize(List<List<String>> csvLines) {
        ICsvHelper csvHelper = new MyCsvHelper();
        List<Column> config = GetColumnConfiguration();
        ColumnStandardizer columnStandardizer = new ColumnStandardizer(config);
        List<List<String>> standardizedCsv = csvHelper.replaceHeaders(csvLines, columnStandardizer);
        return standardizedCsv;
    }

    private List<Column> GetColumnConfiguration() {
        if (null == _csvColumnConfig) {
            ColumnsConfigParser configReader = new ColumnsConfigParser();
            try {
                _csvColumnConfig = configReader.parse(_csvColumnConfixXml);
            } catch(BadConfigurationException | IOException e) {
                System.out.printf("Configuration error: " + e.getMessage());
            }
        }
        return _csvColumnConfig;
    }
}