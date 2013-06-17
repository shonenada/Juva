package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import juva.database.ModelProxy;

import example.settings;


public class FocusProxy extends ModelProxy{
	
	public FocusProxy() throws ClassNotFoundException, SQLException{
		super(settings.dbInfo);
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
		this.clearSelectFilter();
		this.addSelectFilter("uid", uid);
		ResultSet focusRs = this.select();
		return focusRs;
	}
	
	public ResultSet getFansByUid(String uid) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("dst_id", uid);
		ResultSet focusRs = this.select();
		return focusRs;
	}
	
	public int getFansCount(String uid) throws SQLException{
		this.clearSelectFilter();
	    this.addSelectFilter("dst_id", uid);
	    this.addSelectFilter("is_hidden", "0");
	    return this.count();
	}
	
	public int getFocusCount(String uid) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("uid", uid);
		this.addSelectFilter("is_hidden", "0");
		return this.count();
	}
	
	public ArrayList getFocusList(String uid)
	        throws Throwable{
		ArrayList output = new ArrayList();
		UserProxy userProxy = new UserProxy();
		ResultSet focusSet = this.getFocusByUid(uid);
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
		ArrayList output = new ArrayList();
		UserProxy userProxy = new UserProxy();
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
