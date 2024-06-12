// Adelia Putri Widyasari
package com.adel.Helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSetup {
    public static void migrate() {
        String[] sql = {
                """
                                                CREATE TABLE if not exists folder (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT);""",
                """
                                                CREATE TABLE if not exists userdata (id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                username TEXT, password TEXT, fullname TEXT);""",
                """
                                                CREATE UNIQUE INDEX if not exists user_username_IDX ON userdata (username);""",
                """
                                                CREATE TABLE if not exists passwordstore (
                                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                name TEXT, username TEXT, password TEXT, hashkey TEXT,
                                                score REAL, category INTEGER, user_id INTEGER, folder_id INTEGER,
                                                FOREIGN KEY (user_id) REFERENCES userdata(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                                FOREIGN KEY (folder_id) REFERENCES folder(id) ON DELETE SET NULL ON UPDATE SET NULL);"""
        };
        try (Connection conn = DBConnect.connect();
             Statement stmt = conn.createStatement()) {
            for (String query : sql)
                stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
