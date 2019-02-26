import java.util.ArrayList;
import java.util.List;

public class CsvColumnValidator implements ICsvValidator {

    private String  filename;
    private List<String> errors;

    public CsvColumnValidator(String filename) {
        this.filename = filename;
        this.errors = new ArrayList<String>();
    }

    @Override
    public boolean isEmpty(List<List<String>> csvValues) {
        return csvValues.size() == 0;
    }

    @Override
    public boolean isValid(List<List<String>> csvValues) {

        if(csvValues.size() == 0) {
            this.errors.add("Missing header, or empty file");
            return false;
        }

        List<String> headers = csvValues.get(0);

        String firstValue = "";
        int firstVal = 0;
        try {
            firstValue = headers.get(0);
            firstVal = Integer.parseInt(firstValue);
        } catch(NumberFormatException ex) {
        }

        if(firstVal != 0) {
            this.errors.add("File: " + this.filename + "  First element invalid: " + firstValue + ". File missing header?");
            return false;
        }

        int nameIndex = headers.indexOf("Naam");
        if(nameIndex < 0) {
            String errorMsg = "File: " + this.filename + "  Column: Naam not found: " + String.join(",", headers);
            this.errors.add(errorMsg);
            System.out.println(errorMsg);
            return false;
        }

        return true;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }
}
