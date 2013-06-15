package example.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import example.auth.Roles;
import juva.Utils;
import juva.rbac.PermissionTable.METHODS;
import example.model.FocusProxy;
import example.model.User;
import example.model.UserProxy;
import example.model.WeiboProxy;


public class Weibo extends Controller{

	final static String URL_PATTERN = "/weibo";
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.LocalUser, METHODS.POST);
		this.permissionTable.allow(Roles.LocalUser, METHODS.DELETE);
	}
	
	public Weibo() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void get() throws Throwable{
		
		User user = (User) this.currentUser;
		String uid = user.getValue("id");
		String nickname = user.getValue("screen");
		
		String screenName = request.getParameter("user");
		User queryUser = userProxy.getByScreen(screenName);
		
	    boolean userIsNull = (queryUser == null);
	    if (userIsNull){
	    	out.println("用户没有找到！");
	    	return;
	    }
	    
	    String queryUserId = queryUser.getValue("id");
	    putVar("screenName", screenName);

	    weiboProxy.db.addSelectFilter("uid", queryUserId);
	    int weibo_count = weiboProxy.count();
	    
	    ArrayList weiboList = weiboProxy.getWeiboList(queryUserId);
	    
	    int fans_count = focusProxy.getFansCount(queryUserId);
	    int focus_count = focusProxy.getFocusCount(queryUserId);
	    
	    example.model.Focus isFocus = focusProxy.getByIDs(uid, queryUserId);
	    if (isFocus != null || uid.equals(queryUserId)){
	    	putFalseVar("not_focus");
	    }
	    if (weiboList.size() >0){
			putTrueVar("has_weibo");
		}else{
			putFalseVar("has_weibo");
		}
		putFalseVar("is_current_user");
		putTrueVar("not_current_user");
	    putVar("weibo_count", weibo_count);
		putVar("weibos", weiboList);
		putVar("nickname", nickname);
	    putVar("focus_count", focus_count);
	    putVar("fans_count", fans_count);
		
	    render("main.html");
	}
	
	public void post() throws Throwable{
		
		User user = (User) this.currentUser;
		
		String weiboContent = this.request.getParameter("weibo");
		if (weiboContent != null && weiboContent.length() < 1){
			Utils.Json.json("false", "请输入微博内容");
			return ;
		}
		if (weiboContent != null && weiboContent.length() > 140){
			Utils.Json.json("false", "微博最大长度为140个字符.");
			return ;
		}
		
		example.model.Weibo weibo = new example.model.Weibo();
		weibo.setValue("uid", user.getValue("id"));
		weibo.setValue("content", weiboContent);
		weibo.setValue("aid", "0");
		weibo.setValue("is_repost", "0");
		weibo.setValue("is_trash", "0");
		WeiboProxy weiboProxy = new WeiboProxy(weibo);
		weiboProxy.db.insert();
		
		Utils.Json.json("true", "发布成功！");
		
	}
	
	public void delete() throws Throwable{
		User user = (User) this.currentUser;

		String wid = this.request.getParameter("wid");
		if (wid == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		WeiboProxy weiboProxy = new WeiboProxy();
		example.model.Weibo weibo = (example.model.Weibo) weiboProxy.find(wid);
		
		if (weibo == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		if ( !weibo.getValue("uid").equals(user.getValue("id"))){
			Utils.Json.json("false", "您无操作权限");
			return ;
		}
		
		weiboProxy.setModel(weibo);
		weibo.setValue("is_trash", "1");
		weiboProxy.db.update();
		
		Utils.Json.json("true", "删除成功!");
		
	}

}
