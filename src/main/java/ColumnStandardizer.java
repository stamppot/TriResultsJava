import TriResultsJava.Column;

import java.util.*;

///  <summary>
///  Replaced mapped columns with standardized column names.
///  Unknown columns are kept to keep the number of columns the same
///  </summary>
public class ColumnStandardizer implements INameMapper {

    private List<Column> _columnConfig;

    public ColumnStandardizer(List<Column> columnConfig) {
        this._columnConfig = columnConfig;
    }

    public final List<String> GetStandardColumnNames(List<String> columnNames) {
        return this.GetStandardColumnNames(columnNames, this._columnConfig);
    }

    public final List<String> GetStandardColumnNames(List<String> columnNames, List<Column> columnConfig) {
        ArrayList<String> results = new ArrayList<String>();
        Map<String,String> mapping = this.InvertColumnConfig(columnConfig);
        for (String columnName : columnNames) {
            if (mapping.containsKey(columnName)) {
                String standardizedName = mapping.get(columnName);
                results.add(standardizedName);
            }
            else {
                System.out.println("Column name: '" + columnName + "' not supported");
                results.add(columnName);
            }

        }

        return results;
    }

    private final Map<String, String> InvertColumnConfig(List<Column> columnConfig) {
        Map<String,String> mapping = new HashMap<>();
        //  keys are all columns that can be mapped, including the standardized names
        for (Column col : columnConfig) {
            for (String alternativeName : col.AlternativeNames()) {
//                if (mapping.containsKey(alternativeName)) {
//                    throw new BadConfigurationException("Duplicate column name in configuration: {alternativeName}");
//                }

                mapping.put(alternativeName, col.getName());
            }

            mapping.put(col.getName(), col.getName());
        }

        return mapping;
    }
}