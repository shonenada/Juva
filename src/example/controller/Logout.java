package example.controller;

import java.sql.SQLException;

import javax.servlet.http.Cookie;

import juva.rbac.PermissionTable.METHODS;

import example.settings;
import example.auth.Roles;
import example.model.User;


public class Logout extends Controller{

    final static String URL_PATTERN = "/logout";

    public void initPermission(){
        this.permissionTable.allow(Roles.LocalUser, METHODS.GET);
        this.permissionTable.allow(Roles.Administrator, METHODS.GET);
    }

    public Logout() throws Throwable{
        super(URL_PATTERN);
    }

    public void get() throws Throwable{
        this.session.removeAttribute("email");
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(int i=0;i <cookies.length; ++i){
                if (cookies[i].getName().equals("email")){
                    cookies[i].setMaxAge(0);
                    response.addCookie(cookies[i]);
                }
                if (cookies[i].getName().equals("token")){
                    cookies[i].setMaxAge(0);
                    response.addCookie(cookies[i]);
                }
            }
        }
        response.sendRedirect(settings.URL_PREFIX + "/");
    }

}