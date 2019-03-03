import java.util.List;

public interface ICsvHelper
{
    List<List<String>> replaceHeaders(String[] csvLines, ColumnStandardizer columnStandardizer);
    List<List<String>> replaceHeaders(List<List<String>> csvLines, ColumnStandardizer columnStandardizer);
    String determineSeparator(String csvLine);
    List<String> readFile(String filename, String columnToGet);
    String join(List<List<String>> csvValues);
}