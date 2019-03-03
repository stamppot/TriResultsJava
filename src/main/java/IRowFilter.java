import java.util.Map;

public interface IRowFilter {

    String getColumnName();
    Map<String,String> getWhitelist();
    boolean accept(String value);
}
