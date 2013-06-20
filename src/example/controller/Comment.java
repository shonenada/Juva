package example.controller;

import java.sql.ResultSet;
import java.util.ArrayList;

import juva.Utils;
import juva.rbac.PermissionTable.METHODS;

import example.auth.Roles;
import example.model.CommentProxy;
import example.model.User;
import example.model.UserProxy;


public class Comment extends Controller{
	
	final static String URL_PATTERN = "/comment";
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.LocalUser, METHODS.POST);
		this.permissionTable.allow(Roles.LocalUser, METHODS.DELETE);
		this.permissionTable.allow(Roles.Administrator, METHODS.GET);
		this.permissionTable.allow(Roles.Administrator, METHODS.POST);
		this.permissionTable.allow(Roles.Administrator, METHODS.DELETE);
	}
	
	public Comment() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void get() throws Throwable{
		User email = (User) this.currentUser;
		String article_id = this.request.getParameter("aid");
		
		if (article_id == null){
			Utils.Json.json("false", "数据丢失！");
		}
		
		ArrayList comments = commentProxy.getCommentByAid(article_id);
		
		Utils.Json.json(comments);
		return ;
	}
	
	public void post() throws Throwable{
		User user = (User) this.currentUser;
	
		String article_id = this.request.getParameter("aid");
		String uid = user.getValue("id");
		
		String content = this.request.getParameter("comment");
		if (article_id == null || uid == null || content == null){
			Utils.Json.json("false", "数据丢失！");
		}
		
		example.model.Comment comment = new example.model.Comment();
		comment.setValue("aid", article_id);
		comment.setValue("uid", uid);
		comment.setValue("content", content);
		comment.setValue("is_trash", "0");
		
		commentProxy.setModel(comment);
		commentProxy.insert();

		Utils.Json.json(new String[]{"true", "发布成功！", article_id});
		
	}
	
	public void delete() throws Throwable{
		User user = (User) this.currentUser;
		
		String cid = this.request.getParameter("cid");
		if (cid == null){
			Utils.Json.json("false", "数据丢失！");
			return ;
		}

		example.model.Comment comment;
		comment = (example.model.Comment) commentProxy.find(cid);
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
		commentProxy.setModel(comment);
		commentProxy.update();
		
		Utils.Json.json("true", "删除成功！");
	}
	
}
