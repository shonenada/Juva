package example.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import juva.Controller;
import juva.rbac.PermissionTable.METHODS;

import example.auth.Roles;
import example.model.User;
import example.model.Weibo;
import example.model.Focus;
import example.model.Comment;

public class Main extends Controller{
	
	final static String URL_PATTERN = "/main";
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
		this.permissionTable.allow(Roles.Administrator, METHODS.GET);
	}
	
	public Main() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void before() throws ClassNotFoundException, SQLException{
		User temp = new User();
		this.currentUser = temp.getCurrentUser(request);
	}
	
	public void get() throws Throwable{
		User currentUser = (User) this.currentUser;
		String uid = currentUser.getValue("id");
		String nickname = currentUser.getValue("screen");
		putVar("nickname", nickname);

		User userModel = new User();
		Weibo weiboModel = new Weibo();
		Focus focusModel = new Focus();
		Comment commentModel = new Comment();
		
		weiboModel.db.clearSelectFilter();
		weiboModel.db.addSelectFilter("uid", uid);
		ResultSet allWeibo = weiboModel.db.select();
		int weibo_count = 0;
		while(allWeibo.next()){
			weibo_count = weibo_count + 1;
		}
		putVar("weibo_count", weibo_count);
//		 BUG!!!!!!!!!!!
		if ( weibo_count > 0){
			putTrueVar("has_weibo");
		}else{
			putFalseVar("has_weibo");
		}

		ArrayList<String> myFocus = new ArrayList<String>();

		focusModel.db.clearSelectFilter();
		focusModel.db.addSelectFilter("uid", uid);
		ResultSet allFocus = focusModel.db.select();
		int focus_count = 0;
		while(allFocus.next()){
			focus_count = focus_count + 1;
//			ArrayList<String> temp = new ArrayList<String>();
//			temp.add(allFocus.getString("id"));
//			temp.add(allFocus.getString("screen"));
			myFocus.add(allFocus.getString("id"));
		}
		putVar("focus_count", focus_count);
		
		focusModel.db.clearSelectFilter();
		focusModel.db.addSelectFilter("dst_id", uid);
		ResultSet allFans = focusModel.db.select();
		int fans_count = 0;
		while(allFans.next()){
			fans_count = fans_count + 1;
		}
		putVar("fans_count", fans_count);

//		ArrayList weiboList = new ArrayList();
//		for (int i=0;i<myFocus.size();++i){
//			ArrayList<String> temp = myFocus.get(i);
//			String focusId = temp.get(0); 		// id
//			String focusName = temp.get(1);			// screen
//			weiboModel.db.clearSelectFilter();
//			weiboModel.db.addSelectFilter("uid", focusId);
//			ResultSet focusWeibo = weiboModel.db.select();
//			while (focusWeibo.next()){
//				ArrayList<String> tempWeibo = new ArrayList<String>();
//				String content = focusWeibo.getString("content"); 
//				if (focusWeibo.getString("is_repost") == "1"){
//					weiboModel.db.clearSelectFilter();
//					String aid = focusWeibo.getString("aid");
//					weiboModel.db.addSelectFilter("id", aid);
//					ResultSet rs = weiboModel.db.select();
//					rs.first();
//					content += repost(rs.getString("content"));
//				}
//				tempWeibo.add(focusName);
//				tempWeibo.add(content);
//				tempWeibo.add(focusWeibo.getString("created"));
//				weiboList.add(tempWeibo);
//			}
//		}
//		putVar("weibos", weiboList);
		if (myFocus.size()>0){
			String[] friendList = new String[myFocus.size()];
			for (int i=0;i<myFocus.size();++i){
				friendList[i] = myFocus.get(i);
			}
			ResultSet rs = weiboModel.friendWeibo(friendList);
			ArrayList<ArrayList> weiboList = new ArrayList<ArrayList>();
			while(rs.next()){
				ArrayList<String> temp = new ArrayList<String>();
				String fid = rs.getString("uid");
				String created = rs.getString("created");
				User friend = (User) userModel.find(fid);
				if (friend != null){
					temp.add(friend.getValue("screen"));
					temp.add(created.substring(0, created.length()-2));
					temp.add(rs.getString("content"));
					weiboList.add(temp);
				}
			}
			putVar("weibos", weiboList);
		}
		render("main.html");
	}
	
	
	private String repost(String repost){
		String output;
		output = "<div class='repost'>" + repost + "</div>";
		return output;
	}

}
