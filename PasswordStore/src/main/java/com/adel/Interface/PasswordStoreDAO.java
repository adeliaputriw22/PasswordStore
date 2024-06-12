// Adelia Putri Widyasari
package com.adel.Interface;

import java.util.ArrayList;
import com.adel.Entities.PasswordStore;
import com.adel.Entities.UserData;

public interface PasswordStoreDAO {
    public int addPassword(PasswordStore newPassword, UserData user);

    public ArrayList<PasswordStore> listPassword(UserData user);

    public ArrayList<PasswordStore> findPassword(String name, UserData user);

    public int updatePass(PasswordStore changedPass);

    public int deletePass(PasswordStore deletedPass);
}
