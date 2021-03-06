package juva.database;

public class Column {
	
	private boolean _isPrimaryKey;
	private String _name;
	private String _type;
	private int _length;
	private String _defaultValue;

	public Column(String name, String type){
		this(name, type, 0, null, false);
	}
	
	public Column(String name, String type, int length){
		this(name, type, length, null, false);
	}
	
	public Column(String name, String type, int length, String val){
		this(name, type, length, val, false);
	}
	
	public Column(String name, String type, int length, String val,
                   boolean isPKey){
		this._isPrimaryKey = isPKey;
		this._name = name;
		this._type = type;
		this._length = length;
		this._defaultValue = val;
	}
	
	public boolean isString(){
		boolean isString = (this._type == "String");
		return isString;
	}
	
	public boolean isInteger(){
		boolean isInteger = (this._type == "Integer");
		return isInteger;
	}
	
	public String getName(){
		return this._name;
	}
	
	public boolean isPrimaryKey(){
		return this._isPrimaryKey;
	}
	
}
