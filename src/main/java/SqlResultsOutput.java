import java.util.List;

public class SqlResultsOutput implements IResultsOutput {

    public String Generate(List<List<String>> csvValues) {

        StringBuilder sb = new StringBuilder();
//        String insertSql = "INSERT INTO Results (Pos,StartNr,Naam,Club,City,PosCat,Cat,Swim,PosSwim,T1,PosT1,PosAfterT1,Bike,PosBike,AfterBike,PosAfterBike,T2,PosT2,PosAfterT2,Run,PosRun,Run1,PosRun1,Run2,PosRun2,TeamPoints,TeamTotalPoints,TeamRank,Difference,Total,RaceDate,Race) ";
//        sb.append(insertSql);
//        sb.append(" VALUES (@Pos,@StartNr,@Naam,@Club,@City,@PosCat,@Cat,@Swim,@PosSwim,@T1,@PosT1,@PosAfterT1,@Bike,@PosBike,@AfterBike,@PosAfterBike,@T2,@PosT2,@PosAfterT2,@Run,@PosRun,@Run1,@PosRun1,@Run2,@PosRun2,@TeamPoints,@TeamTotalPoints,@TeamRank,@Difference,@Total,@RaceDate,@Race)";

//        List<String> allColumns = Arrays.asList("Pos,StartNr,Naam,Club,City,PosCat,Cat,Swim,PosSwim,T1,PosT1,PosAfterT1,Bike,PosBike,AfterBike,PosAfterBike,T2,PosT2,PosAfterT2,Run,PosRun,Run1,PosRun1,Run2,PosRun2,TeamPoints,TeamTotalPoints,TeamRank,Difference,Total,RaceDate,Race".split(","))
        List<String> headers = csvValues.remove(0);

        for (List<String> line : csvValues) {

            sb.append("INSERT INTO RaceResults (");
            sb.append(String.join(",", headers)).append(") ");

            sb.append("VALUES ");

            sb.append("(");
            sb.append(line.stream().map(v -> "'" + v + "'").reduce("", String::concat));
            sb.append(");\n");

            for(int i = 0; i < headers.size(); i++) {
                String value = line.get(i);

                sb.append("(");
                sb.append("'" + value + "'");
                sb.append(")");
            }
        }

        return sb.toString();
    }    
}