import java.io.File;
import java.io.IOException;
import java.util.*;
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

    public final List<List<String>> replaceHeaders(String[] csvLines, ColumnStandardizer columnStandardizer) {
        String separator = ",";
        List<List<String>> allLines = new ArrayList<>();
        if (csvLines.length > 0) {
            separator = this.determineSeparator(csvLines[0]);

            final String delimiter = separator;
            allLines = Arrays.asList(csvLines).stream().map(line -> Arrays.asList(line.split(delimiter))).collect(Collectors.toList());

            replaceHeaders(allLines, columnStandardizer);
        }

        return allLines;
    }

    public final List<List<String>> replaceHeaders(List<List<String>> csvLines, ColumnStandardizer columnStandardizer) {
        List<List<String>> results = new ArrayList<>();

        if (csvLines.size() > 0) {
            List<String> headers = csvLines.get(0);
            if(headers.get(0) == "Rank") {
                System.out.println("Rank...");
            }

            final TreeMap<Integer,String> standardizedHeaders = columnStandardizer.GetStandardColumnNames(headers);


            List<Integer> allIndices = new ArrayList(headers.size());
            for(int i=0; i<headers.size(); i++) { allIndices.add(i); }

            // remove bad indices from all lines
            List<Integer> goodIndices = allIndices.stream().filter(idx -> standardizedHeaders.containsKey(idx)).collect(Collectors.toList());

            // copy
            for(List<String> lines : csvLines.subList(0, csvLines.size())) {

                List<String> filteredLine = new ArrayList<>(goodIndices.size());

                for (int i = 0; i < goodIndices.size(); i++) {
                    Integer goodIdx = goodIndices.get(i);
                    if (goodIdx < lines.size()) {
                        String value = lines.get(goodIdx);
                        filteredLine.add(value);
                    } else {
                        String good = String.join(",", goodIndices.stream().map(idx -> idx.toString()).collect(Collectors.toList()));
                        System.out.println("\nGood indices:  " + good + "\n Bad line: " + String.join(",", lines));
                        i = goodIndices.size(); // skip whole line
                    }
                }

                results.add(filteredLine);
            }
//            for(Map.Entry<Integer,String> idxColumn : standardizedHeaders.entrySet()) {
//                Integer index = idxColumn.getKey();
//                String column = idxColumn.getValue();
//            }


//            int nameIndex = standardizedHeaders.values().stream().collect(Collectors.toList()).indexOf("Naam");
//            if(nameIndex < 0) {
//                System.out.println("replaceHeaders: Column: Naam not found");
//                //throw new RuntimeException("Is file missing header, or column?\n" + String.join(",", headers));
//
////                standardizedHeaders = columnStandardizer.GetStandardColumnNames(headers);
//
//            }

            try {
                List<String> headerValues = standardizedHeaders.values().stream().collect(Collectors.toList());
                if(results.isEmpty()) {
                    results.add(headerValues);
                }
                else {

                    int columnSize = headerValues.size();
                    int distinctSize = Arrays.asList(headerValues.stream().distinct()).size();
                    if(columnSize > distinctSize) {
                        ;
                    }

                    results.set(0, headerValues);
                }
            } catch(IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
//            csvLines.set(0, standardizedHeaders);
        }

        return results;
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