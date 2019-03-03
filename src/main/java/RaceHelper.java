public class RaceHelper {

    public static String getDistance(String str) {
        String result = "";

        String s = str.toLowerCase();
        if(s.contains("sprint")) { result = "sprint"; };
        if(s.contains("kwart") || s.contains("1/4")) { result = "sprint"; };
        if(s.contains("-od") || s.contains("olympi")) { result = "OD"; };
        if(s.contains("achtste")) { result = "achtste"; };
        if(s.contains("halve-tri") || s.contains("70.3")) { result = "half tri"; };
        if(s.contains("marathon")) { result = "marathon"; };
        if(s.contains("halve-marathon") || s.contains("half-marathon")) { result = "half marathon"; };
        if(s.contains("-5k")) { result = "5k"; };
        if(s.contains("-10k")) { result = "10k"; };
        if(s.contains("-15k")) { result = "15k"; };
        if(s.contains("-20k")) { result = "20k"; };
        if(s.contains("-21k")) { result = "half marathon"; };
        if(s.contains("-42k")) { result = "marathon"; };

        return result;
    }

    public static String getGender(String str) {
        String result = "";

        String s = str;
        if(s.contains("-V") || s.contains("Vrouwen") || s.contains("vrouwen")) { result = "Vrouwen"; };
        if(s.contains("-H") || s.contains("Heren") || s.contains("-heren")) { result = "Heren"; };
        return result;
    }

    public static String getDivision(String str) {
        String result = "";

        String s = str.toLowerCase();
        if(s.contains("-ere-") || s.contains("-erediv"))  { result = "1e"; };
        if(s.contains("-1e"))  { result = "1e"; };
        if(s.contains("-2e")) { result = "2e"; };
        if(s.contains("-3e"))  { result = "3e"; };
        if(s.contains("-4e")) { result = "4e"; };
        return result;
    }
}
