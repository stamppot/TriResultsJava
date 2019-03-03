import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MemberFilter implements IRowFilter {

    private String columnName;
    private Map<String,String> whitelist;

    public MemberFilter(String columnName, List<String> whitelist) {
        this.columnName = columnName;
        this.whitelist = whitelist.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
    }

    @Override
    public String getColumnName() {
        return this.columnName;
    }

    @Override
    public Map<String, String> getWhitelist() {
        return this.whitelist;
    }

    @Override
    public boolean accept(String value) {
        return this.whitelist.containsKey(value);
    }
}
