import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    public static String monthToNumber(String filename) {

        filename = filename.replace("-jan-", "-01-");
        filename = filename.replace("-feb-", "-02-");
        filename = filename.replace("-mar-", "-03-");
        filename = filename.replace("-apr-", "-04-");
        filename = filename.replace("-may-", "-05-");
        filename = filename.replace("-jun-", "-06-");
        filename = filename.replace("-jul-", "-07-");
        filename = filename.replace("-aug-", "-08-");
        filename = filename.replace("-sep-", "-09-");
        filename = filename.replace("-okt-", "-10-");
        filename = filename.replace("-oct-", "-10-");
        filename = filename.replace("-nov-", "-11-");
        filename = filename.replace("-dec-", "-12-");

        filename = filename.replace("-Jan-", "-01-");
        filename = filename.replace("-Feb-", "-02-");
        filename = filename.replace("-Mar-", "-03-");
        filename = filename.replace("-Apr-", "-04-");
        filename = filename.replace("-May-", "-05-");
        filename = filename.replace("-Jun-", "-06-");
        filename = filename.replace("-Jul-", "-07-");
        filename = filename.replace("-Aug-", "-08-");
        filename = filename.replace("-Sep-", "-09-");
        filename = filename.replace("-Okt-", "-10-");
        filename = filename.replace("-Nov-", "-11-");
        filename = filename.replace("-Dec-", "-12-");

        return filename;
    }

    public static String toString(LocalDateTime date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
        return dtf.format(date);
    }
}
