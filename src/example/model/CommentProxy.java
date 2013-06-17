package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import juva.database.ModelProxy;

import example.settings;


public class CommentProxy extends ModelProxy{

	public CommentProxy() throws ClassNotFoundException, SQLException{
		super();
		Comment commentModel = new Comment();
		this.setModel(commentModel);
	}
	
	public int getCount(String wid) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("aid", wid);
		return this.count();
	}
	
	public ResultSet getListByAid(String aid) throws SQLException{
		this.clearSelectFilter();
		this.addSelectFilter("aid", aid);
		return this.select();
	}
	
}
