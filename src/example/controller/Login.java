package example.controller;

import javax.servlet.http.Cookie;

import example.auth.Roles;
import example.model.User;
import juva.Controller;
import juva.Utils;
import juva.rbac.PermissionTable.METHODS;

public class Login extends Controller{

	public final static String[] URL_PATTERN = {"/login"};
	
	private void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.Everyone, METHODS.POST);
	}
	
	public Login() throws Throwable{
		super(URL_PATTERN);
		initPermission();
	}
	
	public void post() throws Throwable{
		String username = request.getParameter("username");
		String passwd = request.getParameter("passwd");
		if (username == null || passwd == null){
			Utils.Json.json("false", "请输入帐号与密码");
			return ;
		}
		
		User userModel = new User();
		String hashPasswd = Utils.MD5(passwd);
		User user = userModel.getByUsernameAndPasswd(username, hashPasswd);
		if (user == null){
			Utils.Json.json("false", "帐号或密码错误");
			return ;
		}
		
		String remoteIp = request.getRemoteAddr();
		String currentTime = Utils.getCurrentTime();
		user.setValue("last_ip", remoteIp);
		user.setValue("last_log", currentTime);
		user.db.update();
		
		String lastIp = user.getValue("last_ip");
		String lastLog = user.getValue("last_log");
		String token = lastIp + lastLog;
		
		String hashToken = Utils.MD5(token);
        Cookie userCookie = new Cookie("username", username);
        Cookie tokenCookie = new Cookie("token", hashToken);
        response.addCookie(userCookie);
        response.addCookie(tokenCookie);
        session.setAttribute("username", username);

		Utils.Json.json("true", "登录成功！");
	}
	
}
