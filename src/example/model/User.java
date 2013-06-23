package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import example.auth.Roles;

import juva.database.Model;
import juva.database.Column;
import juva.rbac.Role;


public class User extends Model implements juva.rbac.User{
    
    // Identity null for everyone
    // Identity 0 for localuser
    // Identity 5 for administator

    public User() throws ClassNotFoundException, SQLException {
        super("accounts");
        Column id = new Column("id", "int", 0, null, true);
        Column user = new Column("email", "varchar", 50, null);
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

    public String getIdentity() {
        return this.getValue("identity");
    }

    public Role getRole() {
        Role role = Roles.Everyone;
        String identity = this.getIdentity();
        if (identity == null)
            role = Roles.Everyone;
        if ( identity != null && !identity.equals("5"))
            role = Roles.LocalUser;
        if ( identity != null && identity.equals("5"))
            role = Roles.Administrator;
        return role;
    }
}