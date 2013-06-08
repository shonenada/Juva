package example.controller;

import java.sql.SQLException;

import example.auth.Roles;
import example.model.User;
import juva.Controller;
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
	
	public void before() throws ClassNotFoundException, SQLException{
		User temp = new User();
		this.currentUser = temp.getCurrentUser(request);
	}
	
	public void get() throws Throwable{
		
	}
	
	public void post() throws Throwable{
		String sUsername = (String) session.getAttribute("username");
		String cUsername = (String) this.getCookies("username");
		String username = sUsername != null ? sUsername : cUsername;
		
		User userModel = new User();
		User user = userModel.getByUsername(username);
		if (user == null){
			response.sendError(405, "Method Not Allow");
			return ;
		}
		
		String toid = request.getParameter("dst_id");
		String is_hidden = request.getParameter("type");
		is_hidden = (is_hidden != null) ? is_hidden : "0";
		
		if (toid == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		example.model.Focus focus = new example.model.Focus();
		focus.setValue("dst_id", toid);
		focus.setValue("uid", user.getValue("id"));
		focus.setValue("is_hidden", is_hidden);
		focus.setValue("is_trash", "0");
		focus.db.insert();
		
		Utils.Json.json("true", "操作成功");		

	}
	
	public void delete() throws Throwable{
		String sUsername = (String) session.getAttribute("username");
		String cUsername = (String) this.getCookies("username");
		String username = sUsername != null ? sUsername : cUsername;
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


		example.model.Focus focusModel = new example.model.Focus();
		example.model.Focus focus;
		focus = (example.model.Focus) focusModel.find(cid);
		if (focus == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}
		
		String uidInData = focus.getValue("uid");
		if ( !uidInData.equals(user.getValue("id"))){
			Utils.Json.json("false", "您无此权限");
			return ;
		}
		
		focus.setValue("is_trash", "1");
		focus.db.update();
		
		Utils.Json.json("true", "删除成功！");
		
	}

}
