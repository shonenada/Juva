package example;

import java.util.Map;
import java.util.HashMap;


public class settings {

    public final static String PROJECT_NAME = "example";
    public final static String URL_PREFIX = "/Juva";

    public final static Map<String, String> dbInfo =
        new HashMap<String, String>(){
            {
                put("type", "mysql");
                put("host", "localhost");
                put("port", "3306");
                put("name", "S2011150337");
                put("user", "root");
                put("passwd", "");
            }
        };

}