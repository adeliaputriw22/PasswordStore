// Adelia Putri Widyasari
package com.adel.Models;

import java.sql.*;
import java.util.ArrayList;
import com.adel.Entities.Folder;
import com.adel.Interface.FolderDAO;

public class ModelFolder implements FolderDAO {
    private Connection conn;
    private String query;

    public ModelFolder(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int createFolder(String foldername) {
        int id = 0;
        query = "INSERT INTO folder VALUES (null, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, foldername);
            stmt.executeUpdate();
            id = stmt.getGeneratedKeys().getInt(1);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Folder sudah ada! Silahkan buat folder dengan nama lain.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public ArrayList<Folder> listAllFolders() {
        String sql = "SELECT * FROM folder";
        ArrayList<Folder> folders = new ArrayList<>();

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Folder folder = new Folder(
                        rs.getInt("id"),
                        rs.getString("name"));
                folders.add(folder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return folders;
    }

}
