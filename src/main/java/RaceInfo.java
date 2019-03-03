import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class RaceInfo implements Comparable {

    private String filename;

    private RaceInfo(String filename) {
        this.filename = filename.replace(".csv", "");
    }

    private String race = "";
    public void setRace(String race) { this.race = race; }
    public String getRace() { return this.race; }

    private LocalDate date;
    public void setDate(LocalDate date) { this.date = date; }
    public LocalDate getDate() { return this.date; }

    private String distance = "";
    public void setDistance(String date) { this.distance = distance; }
    public String getDistance() { return this.distance; }

    private String division = "";
    public void setDivision(String date) { this.division = division; }
    public String getDivision() { return this.division; }

    private List<List<String>> csvValues;
    public void setCsvValues(List<List<String>> csvValues) { this.csvValues = csvValues; }
    public List<List<String>> getCsvValues() { return this.csvValues; }


    public static RaceInfo create(String filename) {
        RaceInfo r = new RaceInfo(filename);

         List<String> parts = Arrays.asList(DateHelper.monthToNumber(r.filename).split("-|_"));


         Integer yearStr = 2014;
         Integer monthStr = 1;
         Integer dayStr = 1;
         String race = "";
         if(parts.size() >= 3) {
             try {
                 yearStr = Integer.parseInt(parts.get(0));
                 monthStr = Integer.parseInt(parts.get(1));
                 String daySt = parts.get(2);
                 if(daySt.contains("_")) {
                     dayStr = Integer.parseInt(daySt.substring(0, daySt.indexOf("_")));
                     race = daySt.substring(daySt.indexOf("_")+1, daySt.length()) + " ";
                 }
                 else {
                     dayStr = Integer.parseInt(parts.get(2));
                 }
             } catch(NumberFormatException e) {
                 System.out.println("filename: " + filename);
                 System.out.println(e.getMessage());
                 throw e;
             }
             race += String.join(" ", parts.subList(3, parts.size()));
             r.setRace(race);
             LocalDate raceDate = LocalDate.of(yearStr, monthStr, dayStr);
             r.setDate(raceDate);
         }

         r.setDistance(RaceHelper.getDistance(filename));
         r.setDivision(RaceHelper.getDivision(filename));

         return r;
    }

    @Override
    public int compareTo(Object o) {
        int result = this.date.compareTo(((RaceInfo) o).getDate());
        if(result == 0) {
            result = this.filename.compareTo(((RaceInfo) o).filename);
        }
        return result;
    }
}
