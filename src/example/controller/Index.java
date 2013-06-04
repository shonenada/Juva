package example.controller;


import juva.Controller;
import juva.Utils;
import net.sf.json.JSONArray;


public class Index extends Controller {

	final static String[] URL_PATTERN = {"/", "/Index"};

	public Index(){
		super(URL_PATTERN);
	}

	public void get() throws Throwable{
	
		String[] array = {"test", "test2", "test3"};
		
		JSONArray jsonArray = Utils.Json.encode(array);
		
//		out.println(jsonArray);
//		
//		putVar("Test", array);
//		putVar("var2", "hahaha");
//		putTrueVar("thisIsTrue");
//		putTrueVar("thisIsTrueToo");
//		putTrueVar("thisIsTrueThree");
//		
//		User userModel = new User();
		
//		user.setValue("user", "test");
//		user.setValue("passwd", "testtest");
//		db.insert();
		
//		db.addSelectFilter("user", "test");
//	    Column[] columns = {user.getColumn("id"), user.getColumn("user")};
//	    ResultSet rs = db.select(columns);
//	    putRs("rs", rs);
	    
//	    user.setValue("id", "1");
//	    user.setValue("user", "tests2");
		
//		User user = (User) userModel.get("1");

//	    db.update();
//		db.addSelectFilter("user", "test");
//	    ResultSet rs = db.select();
//	    putRs("rs", rs);
//	    putModel("user123", user);
	    
		render("test.html");
	}
	
}
