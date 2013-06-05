package example.controller;

import java.sql.SQLException;

import example.auth.Roles;
import example.model.User;
import juva.Controller;
import juva.rbac.PermissionTable.METHODS;


public class Index extends Controller {

	final static String[] URL_PATTERN = {"/", "/Index"};
	
	@Override
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.Everyone, METHODS.GET);
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
	}

	public Index() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void before() throws ClassNotFoundException, SQLException{
		User temp = new User();
		this.currentUser = temp.getCurrentUser(request);
	}

	public void get() throws Throwable{
		String test = request.getParameter("test");
		String random = (String) session.getAttribute("randomString");
		if (test != null){
			session.setAttribute("test", test);	
			out.println(test.equals(random));
		}
		else{
			out.println(session.getAttribute("test"));
		}
		out.println(session.getAttribute("username"));
		
		
		putVar("home", "Shonenada");
		render("test.html");
	}
	
}
