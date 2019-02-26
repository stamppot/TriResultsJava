import TriResultsJava.Column;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigHelper {

    public static List<Column> Load() {
//        String cwd = new File(ConfigHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toString();  // System.getProperty("user.dir");
        String resourceDirectory = Paths.get("src","test","resources").toString();
        Path p = Paths.get(resourceDirectory, "column_config.xml");
        String filename =  p.toString();

        ColumnsConfigParser parser = new ColumnsConfigParser();

        List<Column> columns = new ArrayList<Column>();

        try {
            columns = parser.parse(filename);
        } catch(IOException | BadConfigurationException e) {
            System.out.println("ConfigHelper.Load: couldn't load " + filename + " - " + e.getMessage());
        }

        return columns;
    }
}
