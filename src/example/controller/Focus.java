package example.controller;

import java.util.ArrayList;

import example.auth.Roles;
import example.model.FocusProxy;
import example.model.User;
import example.model.UserProxy;
import juva.Utils;
import juva.rbac.PermissionTable.METHODS;

public class Focus extends Controller{
	
	final static String URL_PATTERN = "/focus";
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.LocalUser, METHODS.POST);
		this.permissionTable.allow(Roles.LocalUser, METHODS.DELETE);
	}
	
	public Focus() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void get() throws Throwable{
		User user = (User) this.currentUser;
		String nickname = user.getValue("screen");
		String r_param = request.getParameter("user");
		String screen = r_param != null ? r_param : user.getValue("screen");
		
		User queryUser = userProxy.getByScreen(screen);
		String queryUserId = queryUser.getValue("id");
		ArrayList focusList = focusProxy.getFocusList(queryUserId);
	
		int weibo_count = weiboProxy.getWeiboCount(queryUserId);
		int focus_count = focusProxy.getFocusCount(queryUserId);
		int fans_count = focusProxy.getFansCount(queryUserId);
		
		putVar("nickname", nickname);
		putVar("screenName", screen);
		putVar("focusList", focusList);
		putVar("weibo_count", weibo_count);
		putVar("fans_count", fans_count);
		putVar("focus_count", focus_count);
		putTrueVar("focus");
		
		render("focus_or_fans.html");
	} 
	
	public void post() throws Throwable{
		
		String screen = null;
		String dst_id = null;
		String user_id = null;
		String is_hidden = null;

		String email = this.getEmail();
		
		UserProxy userProxy = new UserProxy();
		User user = userProxy.getByEmail(email);
		User dst_user = null;
		
		if (user == null){
			response.sendError(405, "Method Not Allow");
			return ;
		}
		
		user_id = user.getValue("id");
		screen = request.getParameter("dst_user");
		is_hidden = (request.getParameter("type") == "1") ? "1" : "0";
		
		if (screen == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		dst_user = userProxy.getByScreen(screen);
		
		if (dst_user == null){
			Utils.Json.json("false", "没有该用户");
			return ;
		}
		
		dst_id = dst_user.getValue("id");
		
		FocusProxy focusProxy = new FocusProxy();
		example.model.Focus old_focus = focusProxy.getByIDs(user_id, dst_id);
		if (old_focus != null && old_focus.getValue("is_trash").equals("0")){
			Utils.Json.json("false", "您已关注该用户!");
			return ;
		}
		example.model.Focus focus = new example.model.Focus();
		if (old_focus != null){
			focus = old_focus;
			focus.setValue("is_trash", "0");
		}else{
			focus.setValue("dst_id", dst_id);
			focus.setValue("uid", user.getValue("id"));
			focus.setValue("is_hidden", is_hidden);
			focus.setValue("is_trash", "0");
		}
		focusProxy.setModel(focus);
		focusProxy.insert();

		Utils.Json.json("true", "操作成功");		
	}
	
	public void delete() throws Throwable{
		String email = this.getEmail();
		UserProxy userProxy = new UserProxy();
		String uidInData = null;
		User user = userProxy.getByEmail(email);
		String user_id = user.getValue("id");
		String focusId = this.request.getParameter("focusId");
		
		if (focusId == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}

		FocusProxy focusProxy = new FocusProxy();
		example.model.Focus focus = (example.model.Focus)
                                         focusProxy.find(focusId);

		if (focus == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		uidInData = focus.getValue("uid");
		
		if ( !uidInData.equals(user_id)){
			Utils.Json.json("false", "您无此权限");
			return ;
		}
		
		focusProxy.setModel(focus);
		focus.setValue("is_trash", "1");
		focusProxy.update();
		
		Utils.Json.json("true", "删除成功！");
	}

}
