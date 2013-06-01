package juva.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import juva.Exceptions.NotValidDatabaseInformationException;


public class Model {
	
	protected String _table;
	protected Database db;

	ArrayList columns = new ArrayList();
	Map<String, String> _info = new HashMap<String, String>();
	Map<Column, String> data = new HashMap<Column, String>();
	
	public Model(String table, Map<String, String> info)
	    throws ClassNotFoundException, SQLException {
		this._table = table;
		this._info = info;
		this.db = new Database(this);
		this.db.setInfo(info);
		this.db.connect();
	}
	
	public Model get(String id)
	    throws SQLException, ClassNotFoundException,
	            InstantiationException, IllegalAccessException{
		Model output = this.getClass().newInstance();
		output._table = this._table;
		output._info = this._info;
		output.columns = this.columns;
		output.db.addSelectFilter("id", id);
		ArrayList columns = output.getColumnList();
		ResultSet first = output.db.select();
		if (first.next()){
			for (int i=0;i<columns.size();++i){
				Column column = (Column )columns.get(i);
				String columnName = column.getName();
				String columnValue = first.getString(columnName);
				output.setValue(columnName, columnValue);
			}
		}
		output.db.clearSelectFilter();
		return output;
	}

	public String getTable(){
		return this._table;
	}

	public Database getDb(){
		return db;
	}
	
	public Map<Column, String> getData(){
		return data;
	}

	public Column getColumn(String name){
		for(int i=0;i<this.columns.size();++i){
			Column currentColumn = (Column) this.columns.get(i);
			String columnName = currentColumn.getName();
			if (name.equals(columnName)){
				return currentColumn;
			}
		}
		return null;
	}

	public String getPrimaryKey(){
		for(int i=0;i<this.columns.size();++i){
			Column currentColumn = (Column) this.columns.get(i);
			boolean isPrimaryKey = currentColumn.isPrimaryKey();
			if (isPrimaryKey){
				String columnName = currentColumn.getName();
				return columnName;
			}
		}
		return null;
	}
	
	public ArrayList getColumnList(){
		return this.columns;
	}
	
	public void addColumns(Column[] columns){
		for (int i=0;i<columns.length;++i){
			this.addColumn(columns[i]);
		}
	}
	
	public void addColumn(Column column){
		boolean columnNotExist = (!this.isColumExsit(column));
		if (columnNotExist){
			columns.add(column);
		}
	}
	
	public void setValue(Column column, String value){
		boolean columnExist = this.isColumExsit(column);
		if (columnExist){
			data.put(column, value);
		}
	}
	
	public void setValue(String columnName, String value){
		Column column = this.getColumn(columnName);
		if (column != null){
			this.setValue(column, value);
		}
	}
	
	public String getValue(Column column){
		String value = data.get(column);
		return value;
	}
	
	public String getValue(String name){
		Column queryingColumn = this.getColumn(name);
		return getValue(queryingColumn);
	}
	
	public boolean isColumExsit(Column column){
		for (int i=0;i<this.columns.size();++i){
			Column innerColumn = (Column) this.columns.get(i);
			if (innerColumn == column){
				return true;
			}
		}
		return false;
	}
	
	public boolean isColumnSet(Column column){
		Set<Column> columnSet = data.keySet();
	    Iterator iterator = columnSet.iterator();
	    while (iterator.hasNext()) {
	    	Column currentColumn = (Column) iterator.next();
	        if (currentColumn == column){
	            return true;
	        }
	    }
	    return false;
	}

}
