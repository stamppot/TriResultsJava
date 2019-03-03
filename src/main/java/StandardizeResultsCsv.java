import TriResultsJava.Column;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StandardizeResultsCsv {

//    private List<Column> _csvColumnConfig;

    private ICsvValidator csvValidator;
    private ColumnStandardizer columnStandardizer;


    public StandardizeResultsCsv(/*List<Column> columnsConfig, */ ICsvValidator csvValidator, ColumnStandardizer columnStandardizer) {

//        this._csvColumnConfig = columnsConfig;
        this.csvValidator = csvValidator;
        this.columnStandardizer = columnStandardizer;
    }

    public final List<List<String>> FilterOnName(String[] csvLines, Map<String,String> memberNames) {
        List<List<String>> csvValues = ReadAndStandardize(csvLines);

        return FilterOnName(csvValues, memberNames);
    }


    public final List<List<String>> FilterOnName(List<List<String>> csvLines, Map<String, String>  memberNames) {
        List<List<String>> standardizedResultLines = csvLines;

//        /* validate csv with business rules */
        if (!csvValidator.isValid(standardizedResultLines)) {
            String errorMsg = "CSV error: " + String.join(",", csvValidator.getErrors());
            System.out.println(errorMsg);
//            throw new RuntimeException(errorMsg);
        }

        if (standardizedResultLines.size() == 0) {
            System.out.println("FilterOnName: No csvLines");
        }
        List<String> headers = standardizedResultLines.get(0);

        /* find index of Name header */
        int nameIndex = headers.indexOf("Naam");

        List<List<String>> filteredResults = new ArrayList<>();
        filteredResults.add(headers);

        for (List<String> currLine : standardizedResultLines) {  // start after headers
            List<String> columnValues = new ArrayList<>();

            String name = currLine.get(nameIndex);

            /* skip to next if name is not in list */
            if (!memberNames.containsKey(name.trim())) {
                continue;
            } else {
                System.out.println("Name: " + name + "  " + String.join(", ", currLine));
            }

            for (int j = 0; j < headers.size(); j++) {
                int lineSize = currLine.size();
                if (j >= lineSize) {
                    currLine.add(""); // add empty result for fx. DQ column
                }

//                    String column = headers.get(j);
                String value = currLine.get(j);
//                System.out.println("Add " + column + " : " + value);
                columnValues.add(value);
            }
            if (columnValues.size() > 0) {
                filteredResults.add(columnValues);
            }
        }

        if(filteredResults.size() == 1) { // only headers
            filteredResults.clear();
            System.out.println("No member results: file can be removed");
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
        Map<String, String> memberMap = memberNames.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));

        return FilterOnName(standardizedResultLines, memberMap);
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
    public final List<List<String>> ReadAndStandardize(String[] csvLines) {
        ICsvHelper csvHelper = new MyCsvHelper();
        List<List<String>> standardizedCsv = csvHelper.replaceHeaders(csvLines, this.columnStandardizer);
        return standardizedCsv;
    }

    public final List<List<String>> ReadAndStandardize(List<List<String>> csvLines) {
        ICsvHelper csvHelper = new MyCsvHelper();
        List<List<String>> standardizedCsv = csvHelper.replaceHeaders(csvLines, this.columnStandardizer);
        return standardizedCsv;
    }

//    private List<Column> GetColumnConfiguration() {
////        if (null == _csvColumnConfig) {
////            ColumnsConfigParser configReader = new ColumnsConfigParser();
////            try {
////                _csvColumnConfig = configReader.parse(_csvColumnConfixXml);
////            } catch(BadConfigurationException | IOException e) {
////                System.out.printf("Configuration error: " + e.getMessage());
////            }
////        }
//        return this.columnStandardizer._columnConfig;
//    }
}