package example.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import example.model.User;
import juva.Controller;
import juva.Utils;

public class Register extends Controller{
	
	public static String[] URL_PATTERN = {"/reg"};
	
	public Register(){
		super(URL_PATTERN);
	}
	
	public void post() throws Throwable{
		String username = request.getParameter("username");
		String passwd = request.getParameter("passwd");
		String screen = request.getParameter("screen");
		
		if (username == null || passwd == null){
			Utils.Json.json("false", "请输入帐号与密码");
			return ;
		}
		
		if (screen == null){
			Utils.Json.json("false", "请输入您的昵称");
			return ;
		}

		User userModel = new User();
		
		boolean isUserExist = userModel.isUsernameExist(username);
		if (isUserExist){
			Utils.Json.json("false", "该用户名已存在，请换另一个。");
			return ;
		}
		
		boolean isScreenExist = userModel.isScreenExist(screen);
		if (isScreenExist){
			Utils.Json.json("false", "该昵称已存在，请换另一个。");
			return ;
		}
		
		String remoteIp = request.getRemoteAddr();
		String currentTime = Utils.getCurrentTime();
		String hashPasswd = Utils.MD5(passwd);
		
		User user = new User();
		user.setValue("user", username);
		user.setValue("passwd", hashPasswd);
		user.setValue("screen", screen);
		user.setValue("reg_ip", remoteIp);
		user.setValue("last_log", currentTime);
		user.setValue("last_ip", remoteIp);
		user.setValue("is_trash", "0");

		user.db.insert();
		
		Utils.Json.json("true", "注册成功！");
		
	}
}
