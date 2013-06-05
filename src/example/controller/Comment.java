package example.controller;

import example.auth.Roles;
import example.model.User;
import juva.Controller;
import juva.Utils;
import juva.rbac.PermissionTable.METHODS;

public class Comment extends Controller{
	
	final static String URL_PATTERN = "/comment";
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.LocalUser, METHODS.POST);
		this.permissionTable.allow(Roles.LocalUser, METHODS.DELETE);
	}
	
	public Comment() throws Throwable{
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
	
		String aid = this.request.getParameter("wid");
		String uid = user.getValue("id");
		String content = this.request.getParameter("content");
		if (aid == null || uid == null || content == null){
			Utils.Json.json("false", "数据丢失！");
		}
		
		example.model.Comment comment = new example.model.Comment();
		comment.setValue("aid", aid);
		comment.setValue("uid", uid);
		comment.setValue("content", content);
		comment.setValue("is_trash", "0");
		
		comment.db.insert();

		Utils.Json.json("true", "发布成功！");
		
	}
	
	public void delete() throws Throwable{
		String sUsername = (String) session.getAttribute("username");
		String cUsername = (String) this.getCookies("username");
		String username = cUsername != null ? sUsername : cUsername;
		User userModel = new User();
		User user = userModel.getByUsername(username);
		if (user == null){
			response.sendError(405, "Method Not Allow");
			return ;
		}
		
		String cid = this.request.getParameter("cid");
		if (cid == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}


		example.model.Comment commentModel = new example.model.Comment();
		example.model.Comment comment;
		comment = (example.model.Comment) commentModel.find(cid);
		if (comment == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		String uidInData = comment.getValue("uid");
		if ( !uidInData.equals(user.getValue("id"))){
			Utils.Json.json("false", "您无此权限");
			return ;
		}
		
		comment.setValue("is_trash", "1");
		comment.db.update();
		
		Utils.Json.json("true", "删除成功！");
		
	}
	
}
