package example.controller;

import java.sql.SQLException;

import example.auth.Roles;
import example.model.User;
import juva.Controller;
import juva.rbac.PermissionTable.METHODS;


public class Index extends Controller {

	final static String[] URL_PATTERN = {"/", "/Index"};
	
	private void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.Everyone, METHODS.GET);
	}

	public Index() throws Throwable{
		super(URL_PATTERN);
		initPermission();
	}
	
	public void before() throws ClassNotFoundException, SQLException{
		User temp = new User();
		this.currentUser = temp.beCurrentUser(request);
	}

	public void get() throws Throwable{
		putVar("home", "Shonenada");
		render("test.html");
	}
	
}
