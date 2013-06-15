package example.model;

import java.sql.SQLException;

import juva.database.Column;
import juva.database.Model;


public class Comment extends Model{
	
	public Comment()
            throws ClassNotFoundException, SQLException {
		super("comments");
		Column id = new Column("id", "int");
		Column aid = new Column("aid", "int");
		Column uid = new Column("uid", "int");
		Column created = new Column("created", "timestamp");
		Column content = new Column("content", "varchar", 140);
		Column is_trash = new Column("is_trash", "tinyint", 1);
		this.addColumns(new Column[]{id, aid, uid, created,
                                      content, is_trash});
	}
	
}
