package xyz.intent.pt.tracker.edit.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.FileUtils.listFiles;

public class FileUtils {
    private static List<File> getFilePathList(String path, String[] regex) {
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            fileList = (List<File>) listFiles(file, regex, false);
        }
        return fileList;
    }

    public static List<File> getTorrentFilePathList(String path) {
        return getFilePathList(path, new String[]{"torrent"});
    }
}
