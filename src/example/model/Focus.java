package example.model;

import java.sql.SQLException;

import juva.database.Column;
import juva.database.Model;


public class Focus extends Model{
	
	public Focus ()
            throws ClassNotFoundException, SQLException{
		super("focus");
		Column id = new Column("id", "int");
		Column uid = new Column("uid", "int");
		Column dst_id = new Column("dst_id", "int");
		Column created = new Column("created", "timestamp");
		Column is_hidden = new Column("is_hidden", "tinyint");
		Column is_trash = new Column("is_trash", "tinyint");
		this.addColumns(new Column[]{id, uid, dst_id, created,
                                      is_hidden, is_trash});
	}
	
}
