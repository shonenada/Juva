package juva.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import juva.database.Database;

public class ModelProxy {

	Map<String, String> _info = new HashMap<String, String>();
	private Model model;
	public Database db;
	
	public ModelProxy(Map<String, String> info)
            throws SQLException, ClassNotFoundException{
		this._info = info;
		this.db = new Database(model);
		this.db.setInfo(this._info);
		this.db.connect();
	}
	
	public void setModel(Model model)
	        throws ClassNotFoundException, SQLException {
		this.model = model;
		this.db = new Database(model);
		this.db.setInfo(this._info);
		this.db.connect();
	}

	public Database getDb(){
		return db;
	}
	
	public void connect() throws SQLException{
		this.db.connect();
	}
	
	public void close() throws SQLException{
		this.db.closeConnection();
	}
	
	public Model find(String id) throws Throwable{
		Model output = this.model.getClass().newInstance();
		output._table = this.model._table;
		output.columns = this.model.columns;
		this.db.addSelectFilter("id", id);
		ArrayList columns = output.getColumnList();
		ResultSet first = this.db.select();
		if (first.next()){
			for (int i=0;i<columns.size();++i){
				Column column = (Column )columns.get(i);
				String columnName = column.getName();
				String columnValue = first.getString(columnName);
				output.setValue(columnName, columnValue);
			}
		}
		this.db.clearSelectFilter();
		return output;
	}

	public int count() throws SQLException{
		int total = 0;
		ResultSet rs = this.db.select();
		while (rs.next()){
			total++;
		}
		return total;
	}

}
