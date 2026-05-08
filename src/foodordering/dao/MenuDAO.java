package foodordering.dao;

import foodordering.model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO extends BaseDAO{

    @Override
    protected String getTableName(){ return "item"; }

    public List<MenuItem> getAllItems(){
        return query(
            "SELECT m.*, r.name AS rname FROM item m " +
            "JOIN restaurants r ON m.restaurant_id = r.restaurant_id " +
            "ORDER BY r.name, m.category, m.item_name",
            null
        );
    }

    public List<MenuItem> getByCategory(String category){
        return query(
            "SELECT m.*, r.name AS rname FROM item m " +
            "JOIN restaurants r ON m.restaurant_id = r.restaurant_id " +
            "WHERE m.category = ? ORDER BY m.item_name",
            category
        );
    }

    public List<String> getCategories(){
        List<String> list=new ArrayList<>();
        list.add("All");

        try(Statement st=getConnection().createStatement();
            ResultSet rs=st.executeQuery(
                "SELECT DISTINCT category FROM item ORDER BY category")){

            while(rs.next()) list.add(rs.getString(1));

        }catch(SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    private List<MenuItem> query(String sql,String param){
        List<MenuItem> list=new ArrayList<>();

        try(PreparedStatement ps=getConnection().prepareStatement(sql)){

            if(param!=null) ps.setString(1,param);

            ResultSet rs=ps.executeQuery();

            while(rs.next()) list.add(mapRow(rs));

        }catch(SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    private MenuItem mapRow(ResultSet rs) throws SQLException{
        return new MenuItem(
            rs.getInt("item_id"),
            rs.getInt("restaurant_id"),
            rs.getString("rname"),
            rs.getString("item_name"),
            rs.getDouble("price"),
            rs.getString("category"),
            rs.getString("description")
        );
    }
}