package br.com.file.analytic.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DirUtils {

    public static void createDirIfNotExists(String dir) {
        File file = new File(dir);
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    public static Boolean moveFile(String origin, String destination) throws IOException {

        Path temp = Files.move(Paths.get(origin), Paths.get(destination));

        if(temp != null) return true;

        return false;

    }

    public static Boolean copyFile(String origin, String destination) throws IOException {

        Path temp = Files.copy(Paths.get(origin), Paths.get(destination));

        if(temp != null) return true;

        return false;

    }

    public static Boolean deleteFile(String file) throws IOException {
        return Files.deleteIfExists(Paths.get(file));
    }

}
