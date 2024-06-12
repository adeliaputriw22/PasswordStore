// Adelia Putri Widyasari
package com.adel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.adel.Entities.Folder;
import com.adel.Entities.PasswordStore;
import com.adel.Entities.UserData;
import com.adel.Helper.DBConnect;
import com.adel.Helper.DBSetup;
import com.adel.Interface.FolderDAO;
import com.adel.Interface.PasswordStoreDAO;
import com.adel.Interface.UserDAO;
import com.adel.Models.ModelFolder;
import com.adel.Models.ModelPasswordStore;
import com.adel.Models.ModelUser;

public class Main {
    public static void main(String[] args) {
        DBSetup.migrate();

        try (Connection conn = DBConnect.connect()) {
            UserDAO userDAO = new ModelUser(conn);
            PasswordStoreDAO passwordStoreDAO = new ModelPasswordStore(conn);
            FolderDAO folderDAO = new ModelFolder(conn);

            // Define and Register Users //
            UserData[] dt_users = {
                    new UserData("Adelia Putri", "ngopidulu72", "Adelia Putri"),
                    new UserData("Lutfi Saputro", "makandlbarukm", "Lutfi Saputro")
            };

            for (UserData user : dt_users) {
                user.id = userDAO.register(user.username, user.getPassword(), user.fullname);
            }

            // Define and Create Folders //
            Folder[] dt_folders = {
                    new Folder("uncategorized"),
                    new Folder("Social Media"),
                    new Folder("Email"),
                    new Folder("M-Banking")
            };

            for (Folder folder : dt_folders) {
                folder.id = folderDAO.createFolder(folder.name);
            }

            // Define and Add Passwords //
            PasswordStore[][] passwordsToAdd = {
                    {
                            new PasswordStore("Gmail", "adeliaputriwidyasariii@gmail.com", "0702asikbenul", 1, dt_folders[2]),
                            new PasswordStore("Instagram", "punyadell", "janlupbobo", 2, dt_folders[1]),
                            new PasswordStore("Discord", "adeliaputriw22", "diskortasik1", 2, dt_folders[1]),
                            new PasswordStore("Bank BNI", "adeliaputrii", "pwmbankingbni", 2, dt_folders[3]),
                            new PasswordStore("SubwaySurf", "adel11", "guddaimoca", 2, dt_folders[0])
                    },
                    {
                            new PasswordStore("Instagram", "lslutfi10", "igke10lutfi", 2, dt_folders[1]),
                            new PasswordStore("Gmail", "lutfiputro10@gmail.com", "pwgmail10", 1, dt_folders[2]),
                            new PasswordStore("Dota", "lsputro10", "dotaggpro", 3, dt_folders[0]),
                            new PasswordStore("Bank Mandiri", "lutfiiis", "livinmantap", 2, dt_folders[3]),
                            new PasswordStore("Instagram", "lutfiiputro", "pwigke15ls", 2, dt_folders[1])
                    }
            };

            for (int i = 0; i < dt_users.length; i++) {
                UserData user = dt_users[i];
                for (PasswordStore pass : passwordsToAdd[i]) {
                    pass.id = passwordStoreDAO.addPassword(pass, user);
                }
            }

            // List passwords for the first user
            for (UserData user : dt_users) {
                if (user.id != -1) {
                    ArrayList<PasswordStore> passwords = passwordStoreDAO.listPassword(user);
                    System.out.println("========================================");
                    System.out.println("Username: " + user.username);
                    System.out.println("========================================");
                    for (PasswordStore pass : passwords) {
                        System.out.println("Account: " + pass.name + ", Username: " + pass.username +
                                ", Category: " + pass.getCategory() + ", Score: " + pass.getScore() + ", Folder: "
                                + pass.folder.name);
                    }
                    System.out.println("========================================");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
