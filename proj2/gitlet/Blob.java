package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

import static gitlet.SelfUilts.getObjectFile;
import static gitlet.SelfUilts.saveObjectFile;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    /** The source file that will be converted to blob */
    private static File source;

    /** The content of the source file */
    private static byte[] content;

    /** SHA1 id generated from the source file content */
    private static String id;

    /** The corresponding blob type file according to SHA1 id */
    private static File file;

    public Blob(File sourceFile) {
        source = sourceFile;
        content = readContents(sourceFile);
        id = createBlobId(sourceFile);
        file = getObjectFile(id);
    }

    /** The blob file create SHA1 id only according to the source file content and size */
    private String createBlobId(File sourceFile) {
        String filePath = sourceFile.getPath();
        long fileLen = sourceFile.length();
        byte[] fileContent = readContents(sourceFile);
        return sha1(filePath, Long.toString(fileLen), Arrays.toString(fileContent));
    }

    /** Get this blob file SHA1 id */
    public String getBlobId() {
        return id;
    }

    /**
     * Get a Blob instance from the file with the SHA1 id.
     *
     * @param id SHA1 id
     * @return Blob instance
     */
    public static Blob fromFile(String id) {
        return readObject(getObjectFile(id), Blob.class);
    }

    /** save this blob instance to the file in objects folder */
    public void save() {
        saveObjectFile(file, this);
    }

    public File getFile() {
        return file;
    }
}
