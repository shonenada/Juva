package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import juva.database.ModelProxy;

import example.settings;


public class FocusProxy extends ModelProxy{
	
	public FocusProxy() throws ClassNotFoundException, SQLException{
		super();
		Focus focusModel = new Focus();
		this.setModel(focusModel);
	}
	
	public Focus getByIDs(String src_id, String dst_id)
	        throws Throwable{
		this.clearSelectFilter();
		this.addSelectFilter("uid", src_id);
		this.addSelectFilter("dst_id", dst_id);
		ResultSet focusRs = this.select();
		if (focusRs.next()){
			return (Focus) this.find(focusRs.getString("id"));
		}
		return null;
	}
	
	public ResultSet getFocusByUid(String uid) throws SQLException{
		return getFocusByUid(uid, "0");
	}
	
	public ResultSet getFansByUid(String uid) 
    throws SQLException{
		return getFansByUid(uid, "0");
	}
	
	public int getFansCount(String uid) throws SQLException{
		return getFansCount(uid, "0");
	}
	
	public int getFocusCount(String uid) throws SQLException{
		return getFocusCount(uid, "0");
	}
	
	public ResultSet getFocusByUid(String uid, String hidden) 
	        throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("uid", uid);
		if (hidden != null)
			this.addSelectFilter("is_hidden", hidden);
		ResultSet focusRs = this.select();
		return focusRs;
	}
	
	public ResultSet getFansByUid(String uid, String hidden) 
	        throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("dst_id", uid);
		if (hidden != null)
			this.addSelectFilter("is_hidden", hidden);
		ResultSet focusRs = this.select();
		return focusRs;
	}
	
	public int getFansCount(String uid, String hidden) throws SQLException{
		this.clearSelectFilter();
	    this.addSelectFilter("dst_id", uid);
	    if (hidden != null)
	    	this.addSelectFilter("is_hidden", hidden);
	    return this.count();
	}
	
	public int getFocusCount(String uid, String hidden) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("uid", uid);
		if (hidden != null)
			this.addSelectFilter("is_hidden", hidden);
		return this.count();
	}
	
	public ArrayList getFocusList(String uid, String hidden)
	        throws Throwable{
		UserProxy userProxy = new UserProxy();
		userProxy.setDatabase(this.db);
		
		ArrayList output = new ArrayList();
		ResultSet focusSet = this.getFocusByUid(uid, hidden);
		while (focusSet.next()){
			ArrayList row = new ArrayList();
			User tempUser = (User) userProxy.find(focusSet.getString("dst_id"));
			String tempUserId = tempUser.getValue("id");
			String tempUserName = tempUser.getValue("screen");
			row.add(tempUserId);
			row.add(tempUserName);
			output.add(row);
		}
		
		return output;
	}
	
	public ArrayList getFansList(String uid)
    		throws Throwable{
		UserProxy userProxy = new UserProxy();
		userProxy.setDatabase(this.db);
		
		ArrayList output = new ArrayList();
		ResultSet focusSet = this.getFansByUid(uid);
		while (focusSet.next()){
			ArrayList row = new ArrayList();
			User tempUser = (User) userProxy.find(focusSet.getString("uid"));
			String tempUserId = tempUser.getValue("id");
			String tempUserName = tempUser.getValue("screen");
			row.add(tempUserId);
			row.add(tempUserName);
			output.add(row);
		}
		return output;
	}
}
