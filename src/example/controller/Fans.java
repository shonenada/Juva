package example.controller;

import java.util.ArrayList;

import example.auth.Roles;
import example.model.FocusProxy;
import example.model.User;
import example.model.UserProxy;
import juva.Utils;
import juva.rbac.PermissionTable.METHODS;

public class Fans extends Controller{
	
	final static String URL_PATTERN = "/fans";
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.Administrator, METHODS.GET);
	}
	
	public Fans() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void get() throws Throwable{
		
		User user = (User) this.currentUser;
		String nickname = user.getValue("screen");
		String r_param = request.getParameter("user");
		User queryUser = (r_param != null ?
		                  userProxy.getByScreen(r_param) : user);
		String screen = queryUser.getValue("screen");
		String hidden = (user.getValue("id").equals(queryUser.getValue("id")) ?
						 null : "0");
		
		String queryUserId = queryUser.getValue("id");
		
		ArrayList focusList = focusProxy.getFansList(queryUserId);
	
		int weibo_count = weiboProxy.getWeiboCount(queryUserId);
		int focus_count = focusProxy.getFocusCount(queryUserId, hidden);
		int fans_count = focusProxy.getFansCount(queryUserId);
		
		putVar("nickname", nickname);
		putVar("screenName", screen);
		putVar("focusList", focusList);
		putVar("weibo_count", weibo_count);
		putVar("fans_count", fans_count);
		putVar("focus_count", focus_count);
		putTrueVar("fans");
		
		render("focus_or_fans.html");
	} 

}
