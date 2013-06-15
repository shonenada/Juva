package example.controller;

import java.io.IOException;
import java.sql.SQLException;

import example.settings;
import example.model.UserProxy;
import example.model.WeiboProxy;
import example.model.CommentProxy;
import example.model.FocusProxy;


public class Controller extends juva.Controller{
	
	protected WeiboProxy weiboProxy = new WeiboProxy();
	protected UserProxy userProxy = new UserProxy();
	protected FocusProxy focusProxy = new FocusProxy();
	protected CommentProxy commentProxy = new CommentProxy();

	public Controller() throws Throwable {
		super();
	}
	
	public Controller(String urlPattern) throws Throwable {
		super(urlPattern);
	}
	
	public Controller(String[] urlPatterns) throws Throwable {
		super(urlPatterns);
	}

	public void before() throws ClassNotFoundException, SQLException{
		this.currentUser = userProxy.getCurrentUser(request);
		this.variables.clear();
		putVar("url_prefix", settings.URL_PREFIX);
		
		userProxy.connect();
		weiboProxy.connect();
		commentProxy.connect();
		focusProxy.connect();
	}
	
	public void after() throws Throwable{
//		weiboProxy.close();
//		userProxy.close();
//		commentProxy.close();
//		focusProxy.close();
	}
	
	public String getEmail(){
		String s_email = (String) session.getAttribute("email");
		String c_email = (String) this.getCookies("email");
		String email = s_email != null ? s_email : c_email;
		return email;
	}

}
