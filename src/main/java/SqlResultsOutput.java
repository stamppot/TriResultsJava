import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlResultsOutput implements IResultsOutput {

    public String Generate(RaceInfo race) {

        StringBuilder sb = new StringBuilder();

        List<List<String>> csvValues = race.getCsvValues();

        if(csvValues.size() > 0) {
            List<String> headers = csvValues.remove(0);

            boolean addRaceAndDateInfo = !headers.contains("Race");

            Stream<Stream<String>> lineStreams;

            if(addRaceAndDateInfo) {
                headers.add("Race");
                headers.add("RaceDate");
                String raceDate = race.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                lineStreams = csvValues.stream().map(line -> Stream.concat(line.stream(), Stream.of(race.getRace(), raceDate)));
            }
            else {
                lineStreams = csvValues.stream().map(line -> line.stream());
            }

            sb.append("INSERT INTO Race_Results (");
            sb.append(String.join(",", headers)).append(") ");
            sb.append("VALUES ");

            Stream<String> lines = lineStreams.map(line -> line.map(v -> "'" + v.replace("\'", "&quot;") + "'").collect(Collectors.joining(", ")));

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