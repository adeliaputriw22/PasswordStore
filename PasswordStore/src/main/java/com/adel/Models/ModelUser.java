// Adelia Putri Widyasari
package com.adel.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.adel.Entities.UserData;
import com.adel.Interface.UserDAO;

public class ModelUser implements UserDAO {

    private Connection conn;

    public ModelUser(Connection con) {
        this.conn = con;
    }

    @Override
    public int register(String username, String password, String fullname) {
        int id = 0;
        UserData newUser = new UserData(username, password, fullname);
        String query = "insert into userdata values (null, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUser.username);
            stmt.setString(2, newUser.getPassword());
            stmt.setString(3, newUser.fullname);
            stmt.executeUpdate();
            id = stmt.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                System.out.println("Username " + newUser.username + " already registered.");
                System.out.println("========================================");
                id = -1; // Menandakan bahwa pendaftaran gagal karena username sudah ada
            } else {
                e.printStackTrace();
            }
        }
        return id;
    }

    @Override
    public UserData login(String username, String password) {
        UserData user = null;
        String query = "select * from userdata where username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (!result.isAfterLast()) {
                result.next();
                if (UserData.checkPassword(password, result.getString("password"))) {
                    user = new UserData(result.getInt("id"),
                            result.getString("username"),
                            result.getString("password"),
                            result.getString("fullname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public int update(int id, String fullname) {
        String query = "update userdata set fullname = ? where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fullname);
            stmt.setInt(2, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(int id, String password, String fullname) {
        String query = "update userdata set fullname = ?, password = ? where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            UserData newUser = new UserData("", password, fullname);
            stmt.setString(1, newUser.fullname);
            stmt.setString(2, newUser.getPassword());
            stmt.setInt(3, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(int id) {
        String query = "delete from userdata where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
