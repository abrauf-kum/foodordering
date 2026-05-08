package foodordering.dao;

import foodordering.model.User;
import java.sql.*;

public class UserDAO extends BaseDAO{

    @Override
    protected String getTableName(){
        return "users";
    }

    public User login(String email, String password){
        String sql="SELECT * FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement ps=getConnection().prepareStatement(sql)){
            ps.setString(1,email);
            ps.setString(2,password);

            ResultSet rs=ps.executeQuery();

            if (rs.next()) return mapRow(rs);

        } catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean register(User u){
        String sql="INSERT INTO users (name, email, password, phone, address, role)"+
                   " VALUES (?, ?, ?, ?, ?, 'user')";

        try (PreparedStatement ps=getConnection().prepareStatement(sql)){
            ps.setString(1,u.getName());
            ps.setString(2,u.getEmail());
            ps.setString(3,u.getPassword());
            ps.setString(4,u.getPhone());
            ps.setString(5,u.getAddress());

            return ps.executeUpdate() >0;

        } catch(SQLIntegrityConstraintViolationException e){
            return false;

        } catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean emailExists(String email){
        String sql="SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (PreparedStatement ps=getConnection().prepareStatement(sql)){
            ps.setString(1,email);

            return ps.executeQuery().next();

        } catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    private User mapRow(ResultSet rs) throws SQLException{
        return new User(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("role")
        );
    }
}