import TriResultsJava.Column;

import java.util.*;

///  <summary>
///  Replaced mapped columns with standardized column names.
///  Unknown columns are kept to keep the number of columns the same
///  </summary>
public class ColumnStandardizer implements INameMapper {

    protected List<Column> _columnConfig;

    protected ColumnStandardizer() {}

    public ColumnStandardizer(List<Column> columnConfig) {
        this._columnConfig = columnConfig;
    }

    public static ColumnStandardizer Create(List<Column> columnConfig, boolean useSqlSafeNames) {
        if(useSqlSafeNames) {
            return new ColumnStandardizerSql(columnConfig);
        }
        else {
            return new ColumnStandardizer(columnConfig);
        }
    }

    /// returns column index and name of column
    public final TreeMap<Integer, String> GetStandardColumnNames(List<String> columnNames) {
        return this.GetStandardColumnNames(columnNames, this._columnConfig);
    }

    public final TreeMap<Integer,String> GetStandardColumnNames(List<String> columnNames, List<Column> columnConfig) {
        TreeMap<Integer,String> results = new TreeMap<>();
        Map<String,String> mapping = this.InvertColumnConfig(columnConfig);
        for(int i=0; i < columnNames.size(); i++)  {
            String columnName = columnNames.get(i);
            if (mapping.containsKey(columnName)) {
                String standardizedName = mapping.get(columnName);
                results.put(i, standardizedName);
            }
            else {
                System.out.println("Column name: '" + columnName + "' not supported");
            }
        }

        return results;
    }

    protected Map<String, String> InvertColumnConfig(List<Column> columnConfig) {
        Map<String,String> mapping = new HashMap<>();
        //  keys are all columns that can be mapped, including the standardized names
        for (Column col : columnConfig) {
            for (String alternativeName : col.AlternativeNames()) {

                String colName = col.getShortName();
                if(colName == null || colName.equals("")) {
                    colName = col.getName();
                }

                mapping.put(alternativeName, colName);
            }

            String colName = col.getShortName();
            if(colName == null || colName.equals("")) {
                colName = col.getName();
            }
            mapping.put(colName, colName);
        }

        return mapping;
    }
}