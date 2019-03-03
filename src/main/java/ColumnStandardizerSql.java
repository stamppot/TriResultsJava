import TriResultsJava.Column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnStandardizerSql extends ColumnStandardizer {

    public ColumnStandardizerSql(List<Column> columnConfig) {
        this._columnConfig = columnConfig;
    }

    @Override
    protected Map<String, String> InvertColumnConfig(List<Column> columnConfig) {
        Map<String,String> mapping = new HashMap<>();
        //  keys are all columns that can be mapped, including the standardized names
        for (Column col : columnConfig) {
            for (String alternativeName : col.AlternativeNames()) {
//                }

                mapping.put(alternativeName, col.getName());
            }

            String colName = col.getName();
            mapping.put(colName, colName);
        }

        return mapping;
    }
}
