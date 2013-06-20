package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import juva.Utils;
import juva.database.Model;
import juva.database.ModelProxy;

import example.settings;
import example.auth.Roles;


public class WeiboProxy extends ModelProxy{
	
	public WeiboProxy() throws ClassNotFoundException, SQLException{
		super();
		Weibo weiboModel = new Weibo();
		this.setModel(weiboModel);
	}
	
	public WeiboProxy(Model model)
            throws ClassNotFoundException, SQLException{
		super();
		this.setModel(model);
	}
	
	public ResultSet friendWeibo(String[] friendList) throws SQLException{
		return friendWeibo(friendList, 30, 1);
	}
	
	public ResultSet friendWeibo(String[] friendList, int limit, int page)
	        throws SQLException{
		String selectSql = " (";
		for (int i=0;i<friendList.length;++i){
			selectSql += "uid = ? OR "; 
		}
		selectSql = this.removeAndOrCmd(selectSql);
		selectSql += ") AND is_trash = 0 ORDER BY id DESC";
		selectSql += "  LIMIT " + limit + " OFFSET " + limit * (page - 1);
		String[] columns = {"id", "content", "uid", "created",
				            "is_repost", "aid"};
		return this.querySelect(columns, selectSql, friendList);
	}
	
	public ResultSet getAllWeiboByUid(String uid) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("uid", uid);
		this.addSelectFilter("is_trash", "0");
		return this.select();
	}
	
	public int getRepostCount(String wid) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("aid", wid);
		this.addSelectFilter("is_repost", "1");
		this.addSelectFilter("is_trash", "0");
		return this.count();
	}
	
	public int getWeiboCount(String uid) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("uid", uid);
		this.addSelectFilter("is_trash", "0");
		return this.count();
	}
	
	public ArrayList getWeiboList(String uid, String current_id)
            throws Throwable {
		UserProxy userProxy = new UserProxy();
    	CommentProxy commentProxy = new CommentProxy();
    	
    	userProxy.setDatabase(this.db);
    	commentProxy.setDatabase(this.db);
    	
		ResultSet weiboRs = this.getAllWeiboByUid(uid);
		User queryUser = (User) userProxy.find(uid);
		User currentUser = (User) userProxy.find(current_id);
		String screenName = queryUser.getValue("screen");
	    ArrayList<ArrayList<String> > weiboList =
            new ArrayList<ArrayList<String> >();

    	// not elegant!
    	String del_html = "";
    	
	    while (weiboRs.next()){
	    	String wid = weiboRs.getString("id");
	    	ArrayList<String> row = new ArrayList<String>();
	    	
	    	String aid = weiboRs.getString("id");
	    	
	    	int repost_total = this.getRepostCount(wid);
	    	int comment_total = commentProxy.getCount(wid);
	    	
	    	if (uid.equals(current_id) ||
                currentUser.getRole() == Roles.Administrator){
	    		del_html = "<a href='###' id='del-btn-" + aid +
	    		           "' class='del-btn'>删除</a>";
	    	}
	    	String sendTime = Utils.fixTime(weiboRs.getString("created"));
	    	String content = getParent(aid, weiboRs.getString("content"));
	    	
			row.add(aid);
	    	row.add(screenName);
	    	row.add(sendTime);
	    	row.add(content);
	    	row.add(repost_total+"");
			row.add(comment_total+"");
			row.add(del_html);
			
			weiboList.add(row);
	    }
	     Collections.reverse(weiboList);
	    return weiboList;
	}
	
	public ArrayList getFocusWeiboList(String uid, String hidden)
    		throws Throwable{
		UserProxy userProxy = new UserProxy();
		CommentProxy commentProxy = new CommentProxy();
		FocusProxy focusProxy = new FocusProxy();
		
		userProxy.setDatabase(this.db);
		commentProxy.setDatabase(this.db);
		focusProxy.setDatabase(this.db);

		User currentUser = (User) userProxy.find(uid);
		
		ResultSet allFocus = focusProxy.getFocusByUid(uid, hidden);
		
		int focusCount = focusProxy.getFocusCount(uid, hidden);
		
		String myFocus[] = new String[focusCount + 1];
		while(allFocus.next()){
			myFocus[focusCount--] = allFocus.getString("dst_id");
		}
		myFocus[0] = uid;
		
		ResultSet rs = this.friendWeibo(myFocus);
		ArrayList<ArrayList> weiboList = new ArrayList<ArrayList>();

		while(rs.next()){
			ArrayList<String> row = new ArrayList<String>();
			String fid = rs.getString("uid");
			String created = Utils.fixTime(rs.getString("created"));
			User friend = (User) userProxy.find(fid);
			if (friend != null){
				String wid = rs.getString("id");
				int repost_total = this.getRepostCount(wid);
				int comment_total = commentProxy.getCount(wid);
				
				String aid = rs.getString("id");
		    	
		    	// not elegant!
		    	String del_html = "";
		    	if (rs.getString("uid").equals(uid) ||
		    		currentUser.getRole() == Roles.Administrator){
		    		del_html = "<a href='###' id='del-btn-" + aid +
 		                       "' class='del-btn'>删除</a>";
		    	}
		    	String content = getParent(aid, rs.getString("content"));
		    	
				row.add(aid);
				row.add(friend.getValue("screen"));
				row.add(created);
				row.add(content);
				row.add(repost_total+"");
				row.add(comment_total+"");
				row.add(del_html);
				
				weiboList.add(row);
			}
		}
		return weiboList;
	}
	
	private String getParent(String aid, String input) throws Throwable{
		String content = input;
    	Weibo current = (Weibo) this.find(aid);
    	UserProxy userProxy = new UserProxy();
    	userProxy.setDatabase(this.db);
    	if (current.getValue("is_repost").equals("1")){
    		Weibo parent = (Weibo) this.find(current.getValue("aid"));
    		User sendUser = (User) userProxy.find(parent.getValue("uid"));
    		String u_name = sendUser.getValue("screen");
    		content += ("<div class='repost-weibo'><a href='" +
    				    settings.URL_PREFIX + "/weibo?user=" + u_name +
    				    "'>@" + u_name + "</a>: " +
                        parent.getValue("content"));
    		if (parent.getValue("is_repost").equals("1")){
    			content = getParent(parent.getValue("id"), content);
    		}
    		content += "</div>";
    	}
    	return content;
	}
	
}
