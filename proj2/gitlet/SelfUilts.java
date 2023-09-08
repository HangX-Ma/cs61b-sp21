package gitlet;
import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/**
 * Utilities modified by myself
 *
 * @author HangX-Ma
 */
public class SelfUilts {
    /**
     * System exit call with status code 0 and print message with args
     * @param msg print message
     * @param args message args
     */
    public static void exit(String msg, Object... args) {
        message(msg, args);
        System.exit(0);
    }

    public static void rm(File file) {
        if (!file.delete()) {
            throw error(String.format("rm: failed to delete %s", file.getPath()));
        }
    }

    /**
     * Create directory if it not exists
     *
     * @param dir directory
     */
    public static void mkdir(File dir) {
        if (!dir.mkdir()) {
            throw error(String.format("mkdir: failed to create %s", dir.getPath()));
        }
    }
    /**
     * Save the serializable object to the file path.
     * Create a parent directory if not exists.
     *
     * @param file File instance
     * @param obj  Serializable object
     */
    public static void saveObjectFile(File file, Serializable obj) {
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            mkdir(parentDir);
        }
        writeObject(file, obj);
    }

    /** Get object 'File' instance according to the commit hash id*/
    public static File getObjectFile(String id) {
        String dirName = getObjectDirName(id);
        String fileName = getObjectFileName(id);
        return join(Repository.OBJECTS_DIR, dirName, fileName);
    }

    /**
     * Get the folder name in 'objects' dir: cut off the first two number of hash id
     * as the folder name, which can reduce the folder number to less than 255.
     * ex. hash id 4cf44f1e3fe4fb7f8aa42138c324f63f5ac85828: folder name 'fc', file name 'f44...'
     *
     * @param id commit hash id
     */
    private static String getObjectDirName(String id) {
        return id.substring(0, 2);
    }

    /**
     * Get the object file name in objects/<folder>/
     *
     * @param id commit hash id
     * @return one object name in one specific object folder
     */
    private static String getObjectFileName(String id) {
        return id.substring(2);
    }

}
