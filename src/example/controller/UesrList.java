package example.controller;

import java.sql.ResultSet;
import java.util.ArrayList;

import juva.rbac.PermissionTable.METHODS;

import example.auth.Roles;
import example.model.User;


public class UesrList extends Controller{

    final static String URL_PATTERN = "/users";

    protected void initPermission() throws Throwable{
        this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
        this.permissionTable.allow(Roles.Administrator, METHODS.GET);
    }

    public UesrList() throws Throwable{
        super(URL_PATTERN);
    }

    public void get() throws Throwable{

        User user = (User) this.currentUser;
        String uid = user.getValue("id");
        String user_nickname = user.getValue("screen");
        int weibo_count = weiboProxy.getWeiboCount(uid);

        // null means do not add hidden selecting filter.
        int focus_count = focusProxy.getFocusCount(uid, null);
        int fans_count = focusProxy.getFansCount(uid);

        ArrayList users = userProxy.getUserList(uid);

        putVar("users", users);
        putTrueVar("is_current_user");
        putFalseVar("not_current_user");
        putVar("nickname", user_nickname);
        putVar("screenName", user_nickname);
        putVar("weibo_count", weibo_count);
        putVar("fans_count", fans_count);
        putVar("focus_count", focus_count);
        render("users.html");
    }

}