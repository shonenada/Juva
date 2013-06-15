package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		this.db.clearSelectFilter();
		this.db.addSelectFilter("uid", src_id);
		this.db.addSelectFilter("dst_id", dst_id);
		ResultSet focusRs = this.db.select();
		if (focusRs.next()){
			return (Focus) this.find(focusRs.getString("id"));
		}
		return null;
	}

	
	public ResultSet getByUid(String uid) throws SQLException{
		this.db.clearSelectFilter();
		this.db.addSelectFilter("uid", uid);
		ResultSet focusRs = this.db.select();
		return focusRs;		
	}
	
	
	public int getFansCount(String uid) throws SQLException{
		this.db.clearSelectFilter();
	    this.db.addSelectFilter("dst_id", uid);
	    this.db.addSelectFilter("is_hidden", "0");
	    return this.count();
	}
	
	public int getFocusCount(String uid) throws SQLException{
		this.db.clearSelectFilter();
		this.db.addSelectFilter("uid", uid);
		this.db.addSelectFilter("is_hidden", "0");
		return this.count();
	}
	
}
