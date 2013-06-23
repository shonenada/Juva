package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import juva.database.Column;
import juva.database.Model;


public class Weibo extends Model{

    public Weibo() throws ClassNotFoundException, SQLException{
        super("weibos");
        Column id = new Column("id", "int", 0, null, true);
        Column uid = new Column("uid", "int");
        Column created = new Column("created", "timestamp");
        Column content = new Column("content", "varchar", 140);
        Column aid = new Column("aid", "int");
        Column is_repost = new Column("is_repost", "int");
        Column is_trash = new Column("is_trash", "tinyint", 1, "false");
        this.addColumns(new Column[] {id, uid, created, content, aid,
                                      is_repost, is_trash});
    }

}