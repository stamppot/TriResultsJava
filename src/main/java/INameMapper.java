import java.util.List;
import java.util.TreeMap;

public interface INameMapper {

    TreeMap<Integer,String> GetStandardColumnNames(List<String> columnNames);
}