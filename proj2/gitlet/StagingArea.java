package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gitlet.SelfUilts.rm;
import static gitlet.Utils.readObject;
import static gitlet.Utils.writeObject;

public class StagingArea implements Serializable {
    /** The added files */
    private final Map<String/*Path*/, String/* SHA1 id */> added = new HashMap<>();

    /** The removed files */
    private final Set<String/* Path */> removed = new HashSet<>();

    /** The tracked files */
    private transient Map<String/* Path */, String/* SHA1 id */> tracked;

    /** Set staging area from INDEX file */
    public static StagingArea fromFile() {
        return readObject(Repository.INDEX, StagingArea.class);
    }

    /** Set tracked files Map */
    public void setTracked(Map<String, String> trackedFiles) {
        tracked = trackedFiles;
    }

    /** Get the added files Map */
    public Map<String, String> getAddedFiles() {
        return added;
    }

    /** Get the added files Set */
    public Set<String> getRemoved() {
        return removed;
    }

    /** Save this instance to the file INDEX */
    public void save() {
        writeObject(Repository.INDEX, this);
    }

    /**
     * Check staging area status
     * @return true if empty, vice versa
     */
    public boolean isEmpty() {
        return added.isEmpty() && removed.isEmpty();
    }

    /**
     * Perform a  commit
     *
     * @return tracked files Map
     */
    public Map<String, String> commit() {
         tracked.putAll(added);
         for (String filePath : removed) {
             tracked.remove(filePath);
         }
         added.clear();
         removed.clear();

         return tracked;
    }


    /**
     * Add file to the staging area
     *
     * @param file file to be added
     * @return if the staging area changes or not
     */
    public boolean add(File file) {
        String filePath = file.getPath();

        Blob blob = new Blob(file);
        String blobId = blob.getBlobId();

        String trackedBlobId = tracked.get(filePath);
        if (trackedBlobId != null) {
            if (trackedBlobId.equals(blobId)) {
                if (added.remove(filePath) != null) {
                    return true;
                }
                return removed.remove(filePath);
            }
        }

        String prevAddedBlobId = added.put(filePath, blobId);
        if (prevAddedBlobId != null && prevAddedBlobId.equals(blobId)) {
            return false;
        }

        // create a new object file after perform the add command
        if (!blob.getFile().exists()) {
            blob.save();
        }
        return true;
    }

    /**
     * Remove the file
     *
     * @param file the file needs to be removed
     * @return if the staging area changes or not
     */
    public boolean remove(File file) {
        String filePath = file.getPath();

        String addedBlobId = added.remove(filePath);
        if (addedBlobId != null) {
            return true;
        }

        if (tracked.get(filePath) != null) {
            if (file.exists()) {
                rm(file);
            }
            return removed.add(filePath);
        }
        return false;
    }

}
