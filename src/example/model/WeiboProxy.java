package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import juva.database.Model;
import juva.database.ModelProxy;

import example.settings;


public class WeiboProxy extends ModelProxy{
	
	public WeiboProxy() throws ClassNotFoundException, SQLException{
		super(settings.dbInfo);
		Weibo weiboModel = new Weibo();
		this.setModel(weiboModel);
	}
	
	public WeiboProxy(Model model)
            throws ClassNotFoundException, SQLException{
		super(settings.dbInfo);
		this.setModel(model);
	}

	
	public ResultSet friendWeibo(String[] friendList)
	        throws SQLException{
		String selectSql = " ";
		for (int i=0;i<friendList.length;++i){
			selectSql += "uid = ? OR "; 
		}
		selectSql = this.db.removeAndOrCmd(selectSql);
		selectSql += " AND is_trash = 0 ORDER BY id DESC";
		String[] columns = {"id", "content", "uid", "created",
				            "is_repost", "aid"};
		return this.db.querySelect(columns, selectSql, friendList);
	}
	
	public ResultSet getAllWeiboByUid(String uid) throws SQLException{
		this.db.clearSelectFilter();
		this.db.addSelectFilter("uid", uid);
		return this.db.select();
	}
	
	public int getRepostCount(String wid) throws SQLException{
		this.db.clearSelectFilter();
		this.db.addSelectFilter("aid", wid);
		this.db.addSelectFilter("is_repost", "1");
		return this.count();
	}
	
	public int getWeiboCount(String uid) throws SQLException{
		this.db.clearSelectFilter();
		this.db.addSelectFilter("uid", uid);
		return this.count();
	}
	
	public ArrayList getWeiboList(String uid)
            throws Throwable {
		UserProxy userProxy = new UserProxy();
		ResultSet weiboRs = this.getAllWeiboByUid(uid);
		User queryUser = (User) userProxy.find(uid);
		String screenName = queryUser.getValue("screen");
	    ArrayList<ArrayList> weiboList = new ArrayList<ArrayList>();
	    while (weiboRs.next()){
	    	String wid = weiboRs.getString("id");
	    	int repost_total = this.getRepostCount(wid);
	    	ArrayList row = new ArrayList();
	    	CommentProxy commentProxy = new CommentProxy();
	    	int comment_total = commentProxy.getCount(wid);
	    	
			row.add(weiboRs.getString("id"));
	    	row.add(screenName);
	    	row.add(weiboRs.getString("created"));
	    	row.add(weiboRs.getString("content"));
	    	row.add(repost_total+"");
			row.add(comment_total+"");
			weiboList.add(row);
	    }
	    // Collections.reverse(weiboList);
	    return weiboList;
	}
	
	public ArrayList getFocusWeiboList(String uid)
    		throws Throwable{
		UserProxy userProxy = new UserProxy();
		CommentProxy commentProxy = new CommentProxy();

		FocusProxy focusProxy = new FocusProxy();
		ResultSet allFocus = focusProxy.getByUid(uid);
		
		int focusCount = focusProxy.getFocusCount(uid);
		String myFocus[] = new String[focusCount + 1];
		while(allFocus.next()){
			myFocus[focusCount--] = allFocus.getString("id");
		}
		myFocus[0] = uid;
		
		ResultSet rs = this.friendWeibo(myFocus);
		ArrayList<ArrayList> weiboList = new ArrayList<ArrayList>();

		while(rs.next()){
			ArrayList<String> row = new ArrayList<String>();
			String fid = rs.getString("uid");
			String created = rs.getString("created");
			User friend = (User) userProxy.find(fid);
			if (friend != null){
				String wid = rs.getString("id");
				int repost_total = this.getRepostCount(wid);
				int comment_total = commentProxy.getCount(wid);
				row.add(rs.getString("id"));
				row.add(friend.getValue("screen"));
				row.add(created.substring(0, created.length()-2));
				row.add(rs.getString("content"));
				row.add(repost_total+"");
				row.add(comment_total+"");			
				
				weiboList.add(row);
			}
		}
		commentProxy.close();
		return weiboList;
	}
	
}
