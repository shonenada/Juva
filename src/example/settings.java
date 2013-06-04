package example;

import java.util.Map;
import java.util.HashMap;


public class settings {
	
	public final static Map<String, String> dbInfo =
		new HashMap<String, String>(){
			{
				put("type", "mysql");
				put("host", "localhost");
				put("port", "3306");
				put("name", "S2011150337");
				put("user", "S2011150337");
				put("passwd", "liuyaodas");
			}
		};
	
}
