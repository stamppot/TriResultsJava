import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MyCsvHelper implements ICsvHelper {

    public final List<String> getHeaders(List<String> csvLines) throws InvalidCsvException {
        String headerLine = csvLines.get(0);
        if (Strings.isNullOrEmpty(headerLine)) {
            throw new InvalidCsvException("Csv has no headers");
        }

        String separator = this.determineSeparator(headerLine);
        return new ArrayList<String>();
    }

    public final String[] replaceHeaders(String[] csvLines, ColumnStandardizer columnStandardizer) {
        String separator = ",";
        String[] lines = csvLines;
        if (lines.length > 0) {
            separator = this.determineSeparator(csvLines[0]);
            String headers = lines[0];

            List<String> standardizedHeaders = columnStandardizer.GetStandardColumnNames(Arrays.asList(headers.split(separator)));

            lines[0] = String.join(separator, standardizedHeaders);
        }

        return lines;
    }

    public final List<List<String>> replaceHeaders(List<List<String>> csvLines, ColumnStandardizer columnStandardizer) {
        String separator = ",";
        if (csvLines.size() > 0) {
            List<String> headers = csvLines.get(0);
            if(headers.get(0) == "Rank") {
                System.out.println("Rank...");
            }

            List<String> standardizedHeaders = columnStandardizer.GetStandardColumnNames(headers);

            int nameIndex = standardizedHeaders.indexOf("Naam");
            if(nameIndex < 0) {
                System.out.println("replaceHeaders: Column: Naam not found");
                //throw new RuntimeException("Is file missing header, or column?\n" + String.join(",", headers));

                standardizedHeaders = columnStandardizer.GetStandardColumnNames(headers);

            }

            csvLines.set(0, standardizedHeaders);
        }

        return csvLines;
    }

    public final String determineSeparator(String csvHeader) {
        String result = ";";

        if (!Strings.isNullOrEmpty(csvHeader)) {
            int commaCount = csvHeader.split(",").length;
            int semiColonCount = csvHeader.split(";").length;

            if(commaCount > semiColonCount) {
                result = ",";
            }
        }

        return result;
    }


    public final String join(List<List<String>> csvValues) {

        return csvValues.stream().map(lineParts -> String.join(";", lineParts)).collect(Collectors.joining("\n"));
    }

    /// <summary>
    /// Reads file, standardizes headers, filters results on members
    /// </summary>
    public final List<String> readFile(String filename, String columnToGet) {
        String delimiter = ",";

        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine(), delimiter));
            }
        } catch(IOException ex) {
            System.out.printf("File not found: " + filename);
        }

        List<String> headers = records.get(0);
        int columnIndex = headers.indexOf(columnToGet);
        if(columnIndex < 0) {
            System.out.println("Couldn't find column: " + columnToGet);
        }
        return records.stream().map(ls -> ls.get(columnIndex)).collect(Collectors.toList());
//        return FilterOnName(records, memberNames);
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
}