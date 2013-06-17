package juva.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;


public class Database {
	
	class Argument{
		private String _name;
		private String _value;
		
		public Argument(String name, String value){
			this._name = name;
			this._value = value;
		}

		public String getName(){
			return this._name;
		}

		public String getValue(){
			return this._value;
		}
	}

	private String _type = null;
	private String _host = null;
	private String _port = null;
	private String _name = null;
	private String _user = null;
	private String _passwd = null;
	private ArrayList _arguments = new ArrayList();

	Connection connection = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;

	public Database() throws ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
	}

	public void setType(String type){
		this._type = type;
	}

	public void setHost(String host){
		this._host = host;
	}

	public void setPort(String port){
		this._port = port;
	}

	public void setName(String name){
		this._name = name;
	}

	public void setUser(String user){
		this._user = user;
	}

	public void setPasswd(String passwd){
		this._passwd = passwd;
	}

	public void setInfo(Map<String, String> info){
		this.setType(info.get("type"));
		this.setHost(info.get("host"));
		this.setPort(info.get("port"));
		this.setName(info.get("name"));
		this.setUser(info.get("user"));
		this.setPasswd(info.get("passwd"));
	}
	
	public void addArgument(String argName, String value){
		Argument argument = new Argument(argName, value);
		this._arguments.add(argument);
	}
	
	public void connect()
	    throws SQLException{
		if (connection == null){
			boolean isValid = this.valid();
			if (isValid){
				String jdbc = ("jdbc:" + _type + "://" + _host + ":" + _port +
						       "/" + _name);
				int argumentNum = this._arguments.size();
				if (argumentNum > 0){
					jdbc = jdbc + "?";
					for (int i=0;i<argumentNum;++i){
						Argument arg = (Argument) this._arguments.get(i);
						String argName = arg.getName();
						String argValue = arg.getValue();
						jdbc = jdbc + argName + "=" + argValue + "&";
					}
				}
				if (jdbc.endsWith("&")){
					int jdbcLength = jdbc.length();
					jdbc.subSequence(0, jdbcLength - 1);
				}
				connection = DriverManager.getConnection(jdbc, _user, _passwd);
				statement = connection.createStatement();
			}
		}
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public void closeConnection() throws SQLException{
		if (this.connection != null){
			this.connection.close();			
		}
	}

	public boolean valid(){
		boolean typeIsNull = (this._type == null);
		boolean hostIsNull = (this._host == null);
		boolean portIsNull = (this._port == null);
		boolean nameIsNull = (this._name == null);
		boolean userIsNull = (this._user == null);
		boolean passwdIsNull = (this._passwd == null);
		boolean validIsPass = (!typeIsNull && !hostIsNull && !portIsNull && 
				                !nameIsNull && !userIsNull && !passwdIsNull);
		return validIsPass;
	}
	
}
