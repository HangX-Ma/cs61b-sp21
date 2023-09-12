package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static gitlet.SelfUilts.getObjectFile;
import static gitlet.SelfUilts.saveObjectFile;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    /** The source file that will be converted to blob */
    private final File source;

    /** The content of the source file */
    private final byte[] content;

    /** SHA1 id generated from the source file content */
    private final String id;

    /** The corresponding blob type file according to SHA1 id */
    private final File file;

    public Blob(File sourceFile) {
        source = sourceFile;
        content = readContents(sourceFile);
        id = createBlobId(sourceFile);
        file = getObjectFile(id);
    }

    /** The blob file create SHA1 id only according to the source file content and size */
    public static String createBlobId(File sourceFile) {
        String filePath = sourceFile.getPath();
        long fileLen = sourceFile.length();
        byte[] fileContent = readContents(sourceFile);
        return sha1(filePath, Long.toString(fileLen), Arrays.toString(fileContent));
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

    /**
     * Get the blob content as String.
     *
     * @return Blob content
     */
    public String getContentAsString() {
        return new String(content, StandardCharsets.UTF_8);
    }

    /**
     * Write the file content back to the source file.
     */
    public void writeContentToSource() {
        writeContents(source, content);
    }

    /** save this blob instance to the file in objects folder */
    public void save() {
        saveObjectFile(file, this);
    }


    /** Get this blob file SHA1 id */
    public String getBlobId() {
        return id;
    }

    public File getFile() {
        return file;
    }
}
