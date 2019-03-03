import java.time.format.DateTimeFormatter;
import java.util.List;

public class HtmlResultsOutput implements IResultsOutput {

    @Override
    public String Generate(RaceInfo race) {

        StringBuilder sb = new StringBuilder();

        List<List<String>> csvValues = race.getCsvValues();

        if(csvValues.size() > 1) {
            List<String> headers = csvValues.remove(0);
            List<String> firstrow = csvValues.get(0);

            String raceName = getColumnValue(headers, firstrow, "Race");
            if(raceName == "") {
                raceName = race.getRace();
            }
            String raceDate = getColumnValue(headers, firstrow, "RaceDate");
            if(raceDate == "") {
                raceDate = race.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            sb.append("<div class=\"h3 race\">");

            sb.append("<span class=\"racename\">" + raceName + "</span>");
            sb.append("<span class=\"racedistance\">" + race.getDistance() + "</span>&nbsp;&nbsp;");
            sb.append("<span class=\"racedate\">" + raceDate + "</span>");
            sb.append("</div>");

            sb.append("<table class \"raceresults\" >");
            sb.append("<tr>");

            for(String th : headers) {
                if(th.equals("Race") || th.equals("RaceDate") || th.equals("Date")) continue;
                sb.append("<th>" + th + "</th>");
            }

            sb.append("</tr>");

                for (int j = 0; j < csvValues.size(); j++) {
                    sb.append("<tr>");
                    for (int i = 0; i < headers.size(); i++) {
                        List<String> line = csvValues.get(j);
                        String header = headers.get(i);
                        String value = line.get(i);
                        if(header.equals("Race") || header.equals("RaceDate")) {
                            i = headers.size(); continue;
                        }
                            sb.append("<td class=\"" + header + "\">" + value + "</td>");
                    }
                    sb.append("</tr>");
                }
            }

            sb.append("</table>");
            sb.append("</div>");
            sb.append("<p>&nbsp;</p>");
        return sb.toString();

    }

    private String getColumnValue(List<String> headers, List<String> row, String columnName) {
        int idx = headers.indexOf(columnName);
        String value = "";
        if(idx  != -1) {
            value = row.get(idx);
        }
        return value;
    }
}
