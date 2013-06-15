package example.controller;

import juva.rbac.PermissionTable.METHODS;

import example.auth.Roles;


public class Index extends Controller {

	final static String[] URL_PATTERN = {"/"};
	
	@Override
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.Everyone, METHODS.GET);
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.Administrator, METHODS.GET);
	}

	public Index() throws Throwable{
		super(URL_PATTERN);
	}

	public void get() throws Throwable{
		String test = request.getParameter("test");
		String random = (String) session.getAttribute("randomString");
		putVar("site_name", "Juva");
		putVar("site_description", "还没想好要写什么好。");
		render("index.html");
	}
	
}
