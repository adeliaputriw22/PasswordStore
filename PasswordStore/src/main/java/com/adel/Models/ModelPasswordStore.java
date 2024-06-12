// Adelia Putri Widyasari
package com.adel.Models;

import java.sql.*;
import java.util.ArrayList;
import com.adel.Entities.Folder;
import com.adel.Entities.PasswordStore;
import com.adel.Entities.UserData;
import com.adel.Interface.PasswordStoreDAO;

public class ModelPasswordStore implements PasswordStoreDAO {
    private Connection conn;
    private String sql;

    public ModelPasswordStore(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int addPassword(PasswordStore newPassword, UserData user) {
        int id = 0;
        sql = "INSERT INTO passwordstore VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword.name);
            pstmt.setString(2, newPassword.username);
            pstmt.setString(3, newPassword.getEncPassword());
            pstmt.setString(4, newPassword.hashkey);
            pstmt.setDouble(5, newPassword.getScore());
            pstmt.setInt(6, newPassword.getCategoryCode());
            pstmt.setInt(7, user.id);
            pstmt.setInt(8, newPassword.folder.id);
            pstmt.executeUpdate();
            id = pstmt.getGeneratedKeys().getInt(1);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public ArrayList<PasswordStore> listPassword(UserData user) {
        String sql = """
                SELECT ps.id, ps.name AS title, ps.username, ps.category, ps.score, ps.hashkey,
                f.name AS folder_name,
                u.username AS usernameAccount
                FROM passwordstore ps
                JOIN folder f ON ps.folder_id = f.id
                JOIN userdata u ON ps.user_id = u.id
                WHERE ps.user_id = ?
                """;
        ArrayList<PasswordStore> passwordStores = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PasswordStore passwordStore = new PasswordStore(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("username"),
                        rs.getString("usernameAccount"),
                        rs.getString("hashkey"),
                        rs.getDouble("score"),
                        rs.getInt("category"),
                        new Folder(rs.getString("folder_name")));
                passwordStores.add(passwordStore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordStores;
    }

    @Override
    public ArrayList<PasswordStore> findPassword(String name, UserData user) {
        String sql = """
                SELECT * FROM passwordstore ps
                JOIN folder f ON ps.folder_id = f.id
                WHERE ps.user_id = ? AND ps.name = ?
                """;
        ArrayList<PasswordStore> passwordStores = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, user.id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                PasswordStore passwordStore = new PasswordStore(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("hashkey"),
                        rs.getDouble("score"),
                        rs.getInt("category"),
                        new Folder(rs.getString("folder_name")));
                passwordStores.add(passwordStore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwordStores;
    }

    @Override
    public int updatePass(PasswordStore changedPass) {
        String sql = "UPDATE passwordstore SET name = ?, username = ?, password = ?, hashkey = ?, score = ?, category = ?, folder_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, changedPass.name);
            pstmt.setString(2, changedPass.username);
            pstmt.setString(3, changedPass.getEncPassword());
            pstmt.setString(4, changedPass.hashkey);
            pstmt.setDouble(5, changedPass.getScore());
            pstmt.setInt(6, changedPass.getCategoryCode());
            pstmt.setInt(7, changedPass.folder.id);
            pstmt.setInt(8, changedPass.id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deletePass(PasswordStore deletedPass) {
        String sql = "DELETE FROM passwordstore WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deletedPass.id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
