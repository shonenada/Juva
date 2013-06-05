package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import example.settings;
import example.auth.Roles;

import juva.Controller;
import juva.database.Model;
import juva.database.Column;
import juva.rbac.Role;

public class User extends Model implements juva.rbac.User{
	
	// Identity null for everyone
	// Identity 0 for localuser
	// Identity 5 for administator

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
		Column identity = new Column("identity", "tinyint", 1, "0");
		Column is_trash = new Column("is_trash", "tinyint", 1, "0");
		this.addColumns(new Column[] {id, user, passwd, screen,
									   created, reg_ip, last_log, last_ip,
									   identity, is_trash});
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

	@Override
	public juva.rbac.User getCurrentUser(HttpServletRequest request)
	        throws SQLException, ClassNotFoundException {
		User user = null;
		HttpSession session = request.getSession(true);
		String sUsername = (String) session.getAttribute("username");
		String cUsername = (String) Controller.getCookies("username", request);
		String username = sUsername != null ? sUsername : cUsername;
		if (username != null){
			user = this.getByUsername(username);
		}
	    return user;
	}

	@Override
	public String getIdentity() {
		return this.getValue("identity");
	}

	@Override
	public Role getRole() {
		Role role = Roles.Everyone;
		String identity = this.getIdentity();
		if (identity == null)
			role = Roles.Everyone;
		if ( identity != null && !identity.equals("5"))
			role = Roles.LocalUser;
		return role;
	}

}
