package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.readObject;
import static gitlet.Utils.sha1;
import static gitlet.SelfUilts.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author HangX-Ma
 */
public class Commit implements Serializable {
    /** The created date */
    private final Date date;

    /** The message of this Commit. */
    private final String message;

    /** SHA1 id for this commit */
    private final String id;

    /** The parent commits SHA1 id */
    private final List<String> parents;

    /** The corresponding commit type file according to SHA1 id */
    private final File file;

    /** The tracked files */
    private final Map<String/* Path */, String/* SHA1 id */> tracked;

    /**
     * initial commit
     */
    public Commit() {
        date = new Date(0); // allocate time since "the epoch"
        this.message = "initial commit";
        this.parents = new ArrayList<>();
        this.tracked = new HashMap<>();
        this.id = createCommitId();
        this.file = getObjectFile(id);
    }

    public Commit(String message, List<String>parents, Map<String, String> tracked) {
        date = new Date();
        this.message = message;
        this.parents = parents;
        this.tracked = tracked;
        this.id = createCommitId();
        this.file = getObjectFile(id);
    }

    /** Create a new SHA1 id for current commit */
    private String createCommitId() {
        return sha1(tracked.toString(), parents.toString(), getTimestamp(), message);
    }

    /** Get timestamp according to the saved date */
    private String getTimestamp() {
        // Thu Jan 1 00:00:00 1970 +0000
        DateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy z", Locale.ENGLISH);
        return dateFormat.format(this.date);
    }

    /** Get this commit SHA1 id */
    public String getCommitId() {
        return id;
    }

    /** Save the object file */
    public void save() {
        saveObjectFile(file, this);
    }

    /**
     * Get Commit instance from object file
     *
     * @param id SHA1 id
     * @return Commit instance
     */
    public static Commit FromFile(String id) {
        return readObject(getObjectFile(id), Commit.class);
    }

    /** Get tracked file map */
    public Map<String, String> getTracked() {
        return tracked;
    }
}
