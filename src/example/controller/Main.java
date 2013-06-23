package example.controller;

import java.sql.ResultSet;
import java.util.ArrayList;

import juva.rbac.PermissionTable.METHODS;

import example.auth.Roles;
import example.model.FocusProxy;
import example.model.User;
import example.model.WeiboProxy;


public class Main extends Controller{
    
    final static String URL_PATTERN = "/main";

    protected void initPermission() throws Throwable{
        this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
        this.permissionTable.allow(Roles.Administrator, METHODS.GET);
    }

    public Main() throws Throwable{
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

        ArrayList weiboList = weiboProxy.getFocusWeiboList(uid, null);

        if (weiboList.size()>0){
            putTrueVar("has_weibo");
        }else{
            putFalseVar("has_weibo");
        }

        putVar("weibos", weiboList);
        putTrueVar("is_current_user");
        putFalseVar("not_current_user");
        putVar("nickname", user_nickname);
        putVar("screenName", user_nickname);
        putVar("weibo_count", weibo_count);
        putVar("fans_count", fans_count);
        putVar("focus_count", focus_count);

        render("main.html");
    }

    private String repost(String repost){
        String output;
        output = "<div class='repost'>" + repost + "</div>";
        return output;
    }

}