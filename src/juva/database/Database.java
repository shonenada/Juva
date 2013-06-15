package juva.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import juva.Exceptions.NotValidDatabaseInformationException;


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

	private Model model;
	
	private String _type = null;
	private String _host = null;
	private String _port = null;
	private String _name = null;
	private String _user = null;
	private String _passwd = null;
	private ArrayList _arguments = new ArrayList();

	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement;
	
	private ArrayList<String[]> selectFilter = new ArrayList<String[]>();
	private boolean isDesc = false;

	public Database(Model model) throws ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		this.model = model;
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
	
	public void setDesc(){
		this.isDesc = true;
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
	
	public boolean isRecordExist() throws SQLException{
		String table = model.getTable();
		String primaryKey = model.getPrimaryKey();
		String primaryValue = model.getValue(primaryKey);
		String selectSql = ("SELECT " + primaryKey + " FROM " + table +
                           " WHERE " + primaryKey + "=" + primaryValue);
		ResultSet validSet = statement.executeQuery(selectSql) ;
		if (validSet.next()){
			return true;
		}
		return false;
	}

	public void insert() throws SQLException{

		String table = model.getTable();
		String insertSql = "INSERT INTO " + table;
		String columnsSectionSql = "(";
		String valueSectionSql = "VALUES(";
		ArrayList columns = model.getColumnList();
		
		for (int i=0;i<columns.size();++i){
			Column currentColumn = (Column) columns.get(i);
			String columnName = currentColumn.getName();
			columnsSectionSql = (columnsSectionSql + columnName + ", ");
			valueSectionSql = valueSectionSql + " ?, ";
		}
		
		columnsSectionSql = removeComma(columnsSectionSql);
		valueSectionSql = removeComma(valueSectionSql);
		
		columnsSectionSql = columnsSectionSql + ") ";
		valueSectionSql = valueSectionSql + ") " ;
		insertSql = insertSql + columnsSectionSql + valueSectionSql;
		
		preparedStatement = connection.prepareStatement(insertSql);
		preparedStatement.clearParameters();
		
		for(int i=0;i<columns.size();++i){
			Column currentColumn = (Column) columns.get(i);
			String currentValue = model.getValue(currentColumn);
			preparedStatement.setString(i+1, currentValue);
		}

		preparedStatement.executeUpdate();

	}
	
	public void update() throws SQLException{
		String table = model.getTable();
		String updateSql = "UPDATE " + table + " SET ";
		String primaryKey = model.getPrimaryKey();
		String primaryValue = model.getValue(primaryKey);
		ArrayList columns = model.getColumnList();
		
		for (int i=0;i<columns.size();++i){
			Column currentColumn = (Column) columns.get(i);
			String columnName = currentColumn.getName();
			if (primaryKey == columnName){
				continue;
			}
			updateSql = updateSql + columnName + "= ?, ";
		}
		
		updateSql = removeComma(updateSql);
		updateSql = updateSql + " WHERE " + primaryKey + " = ?";
		
		preparedStatement = connection.prepareStatement(updateSql);
		preparedStatement.clearParameters();
		
		int j = 0;
		for (int i=0;i<columns.size();++i){
			Column currentColumn = (Column) columns.get(i);
			String columnName = currentColumn.getName();
			if (primaryKey == columnName){
				continue;
			}
			String currentValue = model.getValue(currentColumn);
			preparedStatement.setString(j+1, currentValue);
			j = j + 1;
		}
		
		preparedStatement.setString(j+1, primaryValue);
		
		preparedStatement.setString(columns.size(), primaryValue);
		preparedStatement.executeUpdate();
		
	}
	
	public void addSelectFilter(String columnName, String columnValue){
		this.addSelectFilter(columnName, columnValue, "=");
	}
	
	public void addSelectFilter(String columnName,
                                 String columnValue,
                                 String operator){
		Column column = model.getColumn(columnName);
		if (model.isColumExsit(column)){
			String[] tuple = {columnName, columnValue, operator};
			selectFilter.add(tuple);
		}		
	}
	
	 public ResultSet select() throws SQLException{
		 ArrayList list = this.model.getColumnList();
		 Column[] all = new Column[list.size()];
		 for (int i=0;i<list.size(); ++i){
			 all[i] = (Column) list.get(i);
		 }
		 return this.select(all);
	 }

	public ResultSet select(Column[] columns) throws SQLException{

		String table = model.getTable();
		String selectSql = "SELECT ";
		for (int i=0; i<columns.length; ++i){
			Column column = columns[i];
			selectSql = selectSql + column.getName() + ", ";
		}
		selectSql = removeComma(selectSql);
		selectSql = selectSql + " FROM " + table + " WHERE ";
		
		for (int i=0;i<selectFilter.size();++i){
			String[] columnInfo = selectFilter.get(i);
			selectSql = selectSql + columnInfo[0] + " " + columnInfo[2] + " ? AND ";
		}

		selectSql = removeAndOrCmd(selectSql);
		
		if (isDesc){
			selectSql += " ORDER BY id DESC";
		}
		
    	preparedStatement = connection.prepareStatement(selectSql);
		preparedStatement.clearParameters();

		for (int i=0;i<selectFilter.size();++i){
			String[] columnInfo = selectFilter.get(i);
			String currentValue = columnInfo[1];
			preparedStatement.setString(i+1, currentValue);
		}
		
		ResultSet rs = preparedStatement.executeQuery();
		return rs;
	}
	
	public ResultSet querySelect(String[] columns, String sql, String[] param)
        throws SQLException{
		String table = model.getTable();
		String selectSql = "SELECT ";
		for (int i=0; i<columns.length; ++i){
			String column = columns[i];
			selectSql = selectSql + column + ", ";
		}
		selectSql = removeComma(selectSql);
		selectSql = selectSql + " FROM " + table + " WHERE ";
		selectSql += sql;
		preparedStatement = connection.prepareStatement(selectSql);
		preparedStatement.clearParameters();
		for (int i=0;i<param.length;++i){
			preparedStatement.setString(i+1, param[i]);
		}
		ResultSet rs = preparedStatement.executeQuery();
		return rs;
	}
	
	private String removeComma(String input){
		if (input.endsWith(",")){
			input = input.substring(0, input.length() - 1);
		}
		if (input.endsWith(", ")){
			input = input.substring(0, input.length() - 2);
		}
		return input;
	}
	
	public String removeAndOrCmd(String input){
		if (input.endsWith("OR")){
			input = input.substring(0, input.length() - 2);
		}
		if (input.endsWith("AND") || input.endsWith("OR ")){
			input = input.substring(0, input.length() - 3);
		}
		if (input.endsWith("AND ")){
			input = input.substring(0, input.length() - 4);
		}
		return input;
	}
	
	public void clearSelectFilter(){
		this.selectFilter.clear();
	}

	
}
