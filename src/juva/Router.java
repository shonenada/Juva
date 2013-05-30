package juva;

import java.util.ArrayList;
import java.util.regex.*;

public class Router {
	
	String URI_PREFIX = "/Juva"; 

    class RouterMap{
    
        String _urlPattern = ".*";
        Controller _controller = null;
    
        public RouterMap(String urlPattern, Controller controller) {
            this._urlPattern = urlPattern;
            this._controller = controller;
        }
        
        public String getPattern(){
        	return _urlPattern;
        }
        
        public Controller getController(){
        	return _controller;
        }
    
    }
    
    public Router(){
    	
    }
    
    public void setPrefix(String prefix){
    	this.URI_PREFIX = prefix;
    }
    
    ArrayList routers = new ArrayList();

    public void addRouter(String urlPattern, Controller controller){
    	RouterMap map = new RouterMap("^" + URI_PREFIX + urlPattern + "$", controller);
        routers.add(map);
    }

    public Controller getController(String url){
        for (int i=0; i<routers.size();++i){
        	RouterMap mapping = (RouterMap) routers.get(i);
            String urlParttern = mapping.getPattern();
        	Pattern pattern = Pattern.compile(urlParttern);
            Matcher matcher = pattern.matcher(url);

            if (matcher.find()){
                return mapping.getController();
            }
        }
        return null;
    }

}
