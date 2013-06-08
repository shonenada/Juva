package example.controller;

import java.sql.SQLException;

import javax.servlet.http.Cookie;

import example.auth.Roles;
import example.model.User;
import juva.Controller;
import juva.rbac.PermissionTable.METHODS;

public class Logout extends Controller{
	
	final static String URL_PATTERN = "/logout";
	
	public void initPermission(){
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
	}
	
	public Logout() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void before() throws ClassNotFoundException, SQLException{
		User temp = new User();
		this.currentUser = temp.getCurrentUser(request);
	}
	
	public void get() throws Throwable{
		this.session.removeAttribute("username");
		Cookie[] cookies = request.getCookies();
	    if (cookies != null){
	        for(int i=0;i <cookies.length; ++i){
	            if (cookies[i].getName().equals("username")){
	                cookies[i].setMaxAge(0);
	                response.addCookie(cookies[i]);
	            }
	            if (cookies[i].getName().equals("token")){
	                cookies[i].setMaxAge(0);
	                response.addCookie(cookies[i]);
	            }
	        }
	    }
	    String url_prefix = (String) this.variables.get("url_prefix");
	    response.sendRedirect(url_prefix + "/");
	}

}
