import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlResultsOutput implements IResultsOutput {

    public String Generate(RaceInfo race) {

        StringBuilder sb = new StringBuilder();

        List<List<String>> csvValues = race.getCsvValues();

        if(csvValues.size() > 0) {
            List<String> headers = csvValues.remove(0);

            sb.append("INSERT INTO RaceResults (");
            sb.append(String.join(",", headers)).append(") ");
            sb.append("VALUES ");

//            sb.append(csvValues.stream().collect("", (s1,s2) -> "'" + s1 + "'" + ", " + "'" + s2 + "'", (a,b) -> a + "," + b));

            Stream<String> lines = csvValues.stream().map(line -> line.stream().map(v -> "'" + v + "'").collect(Collectors.joining(", ")));

            String values = lines.map(l -> "(" + l + ")").collect(Collectors.joining(","));

            sb.append(values);

            sb.append(";\n");

        }

        return sb.toString();
    }

//    public static String concat(String a, String b) {
//        return "'" + a + "'" + ", " + "'" + b + "'";
//    }

}