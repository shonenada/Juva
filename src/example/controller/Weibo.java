package example.controller;

import example.auth.Roles;
import juva.Controller;
import juva.Utils;
import juva.rbac.PermissionTable.METHODS;
import example.model.User;


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
	
	public void post() throws Throwable{
		String sUsername = (String) session.getAttribute("username");
		String cUsername = (String) this.getCookies("username");
		String username = cUsername != null ? sUsername : cUsername;
		User userModel = new User();
		User user = userModel.getByUsername(username);
		if (user == null){
			response.sendError(405, "Method Not Allow");
		}
		
		String weiboContent = this.request.getParameter("weibo");
		if (weiboContent.length() < 1){
			Utils.Json.json("false", "请输入微博内容");
			return ;
		}
		if (weiboContent.length() > 140){
			Utils.Json.json("false", "微博最大长度为140个字符.");
			return ;
		}
		
		example.model.Weibo weibo = new example.model.Weibo();
		weibo.setValue("uid", user.getValue("id"));
		weibo.setValue("content", weiboContent);
		weibo.setValue("aid", "0");
		weibo.setValue("is_repost", "0");
		weibo.setValue("is_trash", "0");
		weibo.db.insert();
		
		Utils.Json.json("false", "发布成功！");
		
	}
	
	public void delete() throws Throwable{
		String sUsername = (String) session.getAttribute("username");
		String cUsername = (String) this.getCookies("username");
		String username = cUsername != null ? sUsername : cUsername;
		User userModel = new User();
		User user = userModel.getByUsername(username);
		if (user == null){
			response.sendError(405, "Method Not Allow");
		}

		String wid = this.request.getParameter("wid");
		if (wid == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		example.model.Weibo weiboModel = new example.model.Weibo();
		example.model.Weibo weibo = (example.model.Weibo) weiboModel.find(wid);
		
		if (weibo == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		if ( !weibo.getValue("uid").equals(user.getValue("id"))){
			Utils.Json.json("false", "您无操作权限");
			return ;
		}
		
	
		weibo.setValue("is_trash", "1");
		weibo.db.update();
		
		Utils.Json.json("true", "删除成功!");
		
	}

}
