package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import example.settings;

import juva.database.Model;
import juva.database.Column;

public class User extends Model{

	public User() throws ClassNotFoundException, SQLException {
		super("accounts", settings.dbInfo);
		Column id = new Column("id", "int", 0, null, true);
		Column user = new Column("user", "varchar", 50, null);
		Column passwd = new Column("passwd", "varchar", 128, null);
		Column screen = new Column("screen", "varchar", 16, null);
		Column created = new Column("created", "timestamp");
		Column reg_ip = new Column("reg_ip", "varchar", 25);
		Column last_log = new Column("last_log", "Datetime", 25);
		Column last_ip = new Column("last_ip", "varchar", 25);
		Column is_trash = new Column("is_trash", "tinyint", 1, "0");
		this.addColumns(new Column[] {id, user, passwd, screen, created, 
				                       reg_ip, last_log, last_ip, is_trash});
	}
	
	public User(ResultSet rs) throws ClassNotFoundException, SQLException{
		this();
		this.initModelByResultSet(rs);
	}
	
	public User getByUsernameAndPasswd(String username, String passwd)
	    	throws SQLException, ClassNotFoundException{
		this.db.addSelectFilter("user", username);
		this.db.addSelectFilter("passwd", passwd);
		ResultSet rs = this.db.select();
		if (rs.next()){
			User user = new User(rs);
			return user;
		}else{
			return null;
		}
	}
	
	public User getByUsername(String username)
	        throws SQLException, ClassNotFoundException{
		
		this.db.clearSelectFilter();
		
		this.db.addSelectFilter("user", username);
		ResultSet rs = this.db.select();
		if (rs.next()){
			User user = new User(rs);
			return user;
		}else{
			return null;
		}
	}
	
	public boolean isScreenExist(String screen)
			throws SQLException, ClassNotFoundException{
		this.db.clearSelectFilter();
		this.db.addSelectFilter("screen", screen);
		ResultSet rs = this.db.select();
		if (rs.next()){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isUsernameExist(String username)
    	throws SQLException, ClassNotFoundException{
		User user = this.getByUsername(username);
		if (user == null){
			return false;
		}
		else{
			return true;
		}
	}

}
