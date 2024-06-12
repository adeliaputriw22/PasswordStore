// Adelia Putri Widyasari
package com.adel.Interface;

import java.util.ArrayList;
import com.adel.Entities.Folder;

public interface FolderDAO {
    public int createFolder(String foldername);

    public ArrayList<Folder> listAllFolders();
}
