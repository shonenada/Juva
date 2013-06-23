package example.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import juva.Utils;
import juva.database.ModelProxy;

import example.settings;


public class CommentProxy extends ModelProxy{

    public CommentProxy() throws ClassNotFoundException, SQLException{
        super();
        Comment commentModel = new Comment();
        this.setModel(commentModel);
    }

    public int getCount(String aid) throws SQLException{
        this.clearSelectFilter();
        this.addSelectFilter("aid", aid);
        this.addSelectFilter("is_trash", "0");
        return this.count();
    }

    public ResultSet getListByAid(String aid) throws SQLException{
        this.clearSelectFilter();
        this.addSelectFilter("aid", aid);
        this.addSelectFilter("is_trash", "0");
        return this.select();
    }

    public ArrayList getCommentByAid(String aid) throws Throwable{
        UserProxy userProxy = new UserProxy();
        userProxy.setDatabase(this.db);

        ResultSet rs = this.getListByAid(aid);
        ArrayList output = new ArrayList();
        while(rs.next()){
            ArrayList temp = new ArrayList();

            String uid = rs.getString("uid");
            User this_user = (User) userProxy.find(uid);

            String user_name = this_user.getValue("screen");
            String sendTime = Utils.fixTime(rs.getString("created"));
            String comment = rs.getString("content");

            temp.add(user_name);
            temp.add(sendTime);
            temp.add(comment);            

            output.add(temp);
        }
        Collections.reverse(output);
        return output;
    }

}