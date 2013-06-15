package example.controller;

import javax.servlet.http.Cookie;

import example.auth.Roles;
import example.model.User;
import example.model.UserProxy;
import juva.Utils;
import juva.rbac.PermissionTable.METHODS;


public class Login extends Controller{

	public final static String[] URL_PATTERN = {"/login"};
	
	protected void initPermission() throws Throwable{
		this.permissionTable.allow(Roles.Everyone, METHODS.POST);
	}
	
	public Login() throws Throwable{
		super(URL_PATTERN);
	}
	
	public void post() throws Throwable{
		String captcha = request.getParameter("captcha").toLowerCase();
		String trueCaptcha = (String) session.getAttribute("randomString");
		trueCaptcha = trueCaptcha.toLowerCase();
		if (!captcha.equals(trueCaptcha)){
			Utils.Json.json("false", "验证码错误！");
			return ;
		}
		
		String email = request.getParameter("email");
		String passwd = request.getParameter("passwd");
		if (email == null || passwd == null){
			Utils.Json.json("false", "请输入邮箱与密码");
			return ;
		}
		
		String hashPasswd = Utils.MD5(passwd);
		User user = userProxy.getByEmailAndPasswd(email, hashPasswd);
		if (user == null){
			Utils.Json.json("false", "邮箱或密码错误");
			return ;
		}
		
		UserProxy currentUserProxy = new UserProxy(user);
		String remoteIp = request.getRemoteAddr();
		String currentTime = Utils.getCurrentTime();
		user.setValue("last_ip", remoteIp);
		user.setValue("last_log", currentTime);
		currentUserProxy.db.update();
		
		String lastIp = user.getValue("last_ip");
		String lastLog = user.getValue("last_log");
		String token = lastIp + lastLog;
		
		String hashToken = Utils.MD5(token);
        Cookie userCookie = new Cookie("email", email);
        Cookie tokenCookie = new Cookie("token", hashToken);
        response.addCookie(userCookie);
        response.addCookie(tokenCookie);
        session.setAttribute("email", email);

		Utils.Json.json("true", "登录成功！");
	}
	
}
