package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import juva.Controller;
import juva.database.Database;
import juva.database.Model;
import juva.database.ModelProxy;

import example.settings;


public class UserProxy extends ModelProxy {

    public UserProxy() throws ClassNotFoundException, SQLException{
        super();
        User userModel = new User();
        this.setModel(userModel);
    }

    public UserProxy(Model model) throws ClassNotFoundException, SQLException{
        super();
        this.setModel(model);
    }

    public User getByEmailAndPasswd (String email, String passwd)
            throws SQLException, ClassNotFoundException{
        this.clearSelectFilter();
        this.addSelectFilter("email", email);
        this.addSelectFilter("passwd", passwd);
        ResultSet rs = this.select();
        if (rs.next()){
            User user = new User(rs);
            return user;
        }else{
            return null;
        }
    }

    public User getByScreen(String screen)
            throws SQLException, ClassNotFoundException{
        this.clearSelectFilter();
        this.addSelectFilter("screen", screen);
        ResultSet rs = this.select();
        if (rs.next()){
            User user = new User(rs);
            return user;
        }else{
            return null;
        }
    }

    public User getByEmail(String email)
    		throws SQLException, ClassNotFoundException{
        this.clearSelectFilter();
        this.addSelectFilter("email", email);
        ResultSet rs = this.select();
        if (rs.next()){
            User user = new User(rs);
            return user;
        }else{
            return null;
        }
    }

    public boolean isScreenExist(String screen)
            throws SQLException, ClassNotFoundException{
        this.clearSelectFilter();
        this.addSelectFilter("screen", screen);
        ResultSet rs = this.select();
        if (rs.next()){
            return true;
        }else{
            return false;
        }
    }

    public ResultSet getUserSet(String uid) throws SQLException{
        this.clearSelectFilter();
        this.addSelectFilter("is_trash", "0");
        this.addSelectFilter("id", uid, "<>");
        this.setDesc();
        ResultSet rs = this.select();
        this.setAsc();
        return rs;
    }

    public ArrayList getUserList(String uid)
            throws Throwable {
        ArrayList users = new ArrayList();
        FocusProxy focusProxy = new FocusProxy();
        focusProxy.setDatabase(this.db);
        ResultSet rs = this.getUserSet(uid);
        while (rs.next()){
            ArrayList row = new ArrayList();
            String this_id = rs.getString("id");
            Focus focus_rs = focusProxy.getByIDs(uid, this_id);
            String screen = rs.getString("screen");
            String focusHtml = "";
            if (focus_rs == null){
                focusHtml = ("<span class='focus-btns'><a href='#" + screen + 
                             "' class='focus-btn'>(关注)</a>/<a href='#" +
                             screen + "' class='h-focus-btn'>" +
                             "(悄悄关注)</a></span>");
            }
            row.add(screen);
            row.add(focusHtml);
            users.add(row);
        }
        return users;
    }

    public boolean isEmailExist(String email)
        throws SQLException, ClassNotFoundException{
        User user = this.getByEmail(email);
        if (user == null){
            return false;
        }
        else{
            return true;
        }
    }

    public juva.rbac.User getCurrentUser(HttpServletRequest request)
            throws SQLException, ClassNotFoundException {
        User user = null;
        HttpSession session = request.getSession(true);
        String s_email = (String) session.getAttribute("email");
        String c_email = (String) Controller.getCookies("email", request);
        String email = s_email != null ? s_email : c_email;
        if (email != null){
            user = this.getByEmail(email);
        }
        return user;
    }

}