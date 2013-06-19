package example.controller;

import juva.Utils;
import juva.rbac.PermissionTable.METHODS;

import example.auth.Roles;
import example.controller.Controller;
import example.model.User;
import example.model.WeiboProxy;

public class Repost extends Controller{

	final static String URL_PATTERN = "/repost";
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.LocalUser, METHODS.POST);
		this.permissionTable.allow(Roles.LocalUser, METHODS.DELETE);
	}
	
	public Repost() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void post() throws Throwable{
		User current = (User) this.currentUser;
		String aid = request.getParameter("aid");
		String repost = request.getParameter("repost");
		
		if (repost != null && repost.length() < 1){
			repost = "转发微博";
		}
		if (repost != null && repost.length() > 140){
			Utils.Json.json("false", "微博最大长度为140个字符.");
			return ;
		}
		
		example.model.Weibo weibo = new example.model.Weibo();
		weibo.setValue("uid", current.getValue("id"));
		weibo.setValue("content", repost);
		weibo.setValue("aid", aid);
		weibo.setValue("is_repost", "1");
		weibo.setValue("is_trash", "0");
		WeiboProxy insert_weiboProxy = new WeiboProxy(weibo);
		insert_weiboProxy.setDatabase(database);
		insert_weiboProxy.insert();
		
		Utils.Json.json("true", "转发成功！");
		
	}
}
