package gitlet;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

import static gitlet.SelfUilts.*;
import static gitlet.Utils.*;


/**
 *  You can check this <a href="https://zhuanlan.zhihu.com/p/106243588">site</a> to
 *  learn the '.git' directory structure and meanings.
 *
 *  @author HangX-Ma
 */
public class Repository {
    /** The default branch name. */
    private static final String DEAULT_BRANCH = "master";

    /** The current working directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");

    /** The HEAD file: current activated branch, refs/heads/<name> */
    private static final File HEAD = join(GITLET_DIR, "HEAD");

    /** The HEAD file content: ref prefix */
    private static final String HEAD_REF_PREFIX = "refs: refs/heads/";

    /** The index file: track the files in .gitlet working directory */
    public static final File INDEX = join(GITLET_DIR, "index");

    /** objects directory: record the committed files */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    /** refs directory */
    private static final File REFS_DIR = join(GITLET_DIR, "refs");

    /** heads directory: record the git branches */
    private static final File HEADS_DIR = join(REFS_DIR, "heads");

    /** The files in current workspace folder */
    private static File[] currentFiles;

    /** The current branch */
    private static String currentBranch;

    /** The commit which the HEAD points to */
    private static Commit headCommit;

    /** The staging area instance */
    private static StagingArea stagingArea;

    public static void workspaceCheck() {
        if (!(GITLET_DIR.exists() && GITLET_DIR.isDirectory())) {
            exit("Not in an initialized Gitlet directory.");
        }
    }

    public Repository() {
        // init current files list
        currentFiles = CWD.listFiles(File::isFile);

        // init current branch
        String headFileContent = readContentsAsString(HEAD);
        currentBranch = headFileContent.replace(HEAD_REF_PREFIX, "");

        // init head commit
        headCommit = getBranchHeadCommit(currentBranch);

        // init staging area
        stagingArea = INDEX.exists()
                ? StagingArea.fromFile()
                : new StagingArea();
        stagingArea.setTracked(headCommit.getTracked());
    }

    /**
     * Initialize a repository at the current working directory.
     *
     * <pre>
     * .gitlet
     *  -- objects
     *  -- refs
     *   -- heads -> [branch name]
     *  -- [HEAD]
     *  -- [Index]
     * </pre>
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            exit("A Gitlet version-control system already exists in the current directory.");
        }
        mkdir(GITLET_DIR);
        mkdir(OBJECTS_DIR);
        mkdir(REFS_DIR);
        mkdir(HEADS_DIR);
        setCurrentBranch(DEAULT_BRANCH);
        initCommit();
    }

    /**
     * Change HEAD file content to set the current branch
     * @param branch which branch needs to be set currently
     */
    public static void setCurrentBranch(String branch) {
        writeContents(HEAD, HEAD_REF_PREFIX + branch);
    }

    public static void initCommit() {
        Commit initialCommit = new Commit();
        initialCommit.save();
        setBranchHeadCommit(DEAULT_BRANCH, initialCommit.getCommitId());
    }

    /**
     * Set branch head.
     *
     * @param branch branch name
     * @param commitId Commit SHA1 id
     */
    private static void setBranchHeadCommit(String branch, String commitId) {
        File branchHeadFile = getBranchHeadFile(branch);
        setBranchHeadCommit(branchHeadFile, commitId);
    }


    private static void setBranchHeadCommit(File branchHeadFile, String commitId) {
        writeContents(branchHeadFile, commitId);
    }

    /**
     * Add file to the staging area
     *
     * @param fileName file name
     */
    public void add(String fileName) {
        File file = getFileFromCWD(fileName);
        if (!file.exists()) {
            exit("File does not exist.");
        }
        if (stagingArea.add(file)) {
            stagingArea.save();
        }

    }

    /**
     * Get files from current working directory
     *
     * @param fileName file name
     * @return File instance
     */
    private static File getFileFromCWD(String fileName) {
        return Paths.get(fileName).isAbsolute()
                ? new File(fileName)
                : join(CWD, fileName);
    }

    /**
     * Get head commit of this branch
     *
     * @param branch branch name
     * @return Commit instance
     */
    private static Commit getBranchHeadCommit(String branch) {
        File branchHeadFile = getBranchHeadFile(branch);
        return getBranchHeadCommit(branchHeadFile);
    }

    private static Commit getBranchHeadCommit(File branchHeadFile) {
        String headCommitId = readContentsAsString(branchHeadFile);
        return Commit.fromFile(headCommitId);
    }

    /**
     * Get branch file
     *
     * @param branch branch name
     * @return the branch file
     */
    private static File getBranchHeadFile(String branch) {
        return join(HEADS_DIR, branch);
    }

    /** commit wrapper */
    public void commit(String msg) {
        commit(msg, null);
    }

    /**
     * Perform a commit with specific message
     *
     * @param msg message
     */
    public void commit(String msg, String secondParent) {
        if (stagingArea.isEmpty()) {
            exit("No changes added to the commit.");
        }
        Map<String, String> newTrackedFilesMap = stagingArea.commit();
        stagingArea.save();
        List<String> parents = new ArrayList<>();
        parents.add(headCommit.getCommitId());
        if (secondParent != null) {
            parents.add(secondParent);
        }
        Commit newCommit = new Commit(msg, parents, newTrackedFilesMap);
        newCommit.save();
        setBranchHeadCommit(currentBranch, newCommit.getCommitId());
    }

    /**
     * Remove file
     *
     * @param fileName file name
     */
    public void remove(String fileName) {
        File file = getFileFromCWD(fileName);
        if (stagingArea.remove(file)) {
            stagingArea.save();
        } else {
            exit("No reason to remove the file.");
        }
    }

    /** Print gitlet log */
    public void log() {
        StringBuilder logBuilder = new StringBuilder();
        Commit currentCommit = headCommit;
        while (true) {
            logBuilder.append(currentCommit.getLog()).append("\n");
            List<String> parentCommitIds = currentCommit.getParents();
            if (parentCommitIds.size() == 0) {
                break;
            }
            currentCommit = Commit.fromFile(parentCommitIds.get(0));
        }
        System.out.println(logBuilder);
    }

    /** print global log */
    public void globalLog() {
        StringBuilder globalLogBuilder = new StringBuilder();
        forEachCommit(commit -> globalLogBuilder.append(commit.getLog()).append("\n"));
        System.out.print(globalLogBuilder);
    }

    /** print the commit ids for given message */
    public void find(String msg) {
        StringBuilder findBuilder = new StringBuilder();
        forEachCommit(commit -> {
            if (commit.getMessage().equals(msg)) {
                findBuilder.append(commit.getCommitId()).append("\n");
            }
        });
        if (findBuilder.length() == 0) {
            exit("Found no commit with that message.");
        }
        System.out.print(findBuilder);
    }

    /**
     * Get all commit for commit Consumer
     * @param commitConsumer consumer for Commit class type
     */
    private static void forEachCommit(Consumer<Commit> commitConsumer) {
        Set<String> commitsSet = new HashSet<>();
        Queue<Commit> commitsQueue = new ArrayDeque<>();

        File[] branchFiles = HEADS_DIR.listFiles();
        Arrays.sort(branchFiles, Comparator.comparing(File::getName));

        for (File branchFile : branchFiles) {
           String headCommitId = readContentsAsString(branchFile);
           if (commitsSet.contains(headCommitId)) {
               continue;
           }
           commitsSet.add(headCommitId);
           Commit headCommit = Commit.fromFile(headCommitId);
           commitsQueue.add(headCommit);
        }

        while (true) {
            Commit nextCommit = commitsQueue.poll();
            commitConsumer.accept(nextCommit);
            List<String> parentCommitIds = nextCommit.getParents();
            if (parentCommitIds.isEmpty()) {
                break;
            }
            for (String parentCommitId : parentCommitIds) {
                if (commitsSet.contains(parentCommitId)) {
                    continue;
                }
                commitsSet.add(parentCommitId);
                Commit parentCommit = Commit.fromFile(parentCommitId);
                commitsQueue.add(parentCommit);
            }
        }
    }

    /** print current gitlet status */
    public void status() {
        StringBuilder statusBuilder = new StringBuilder();

        // branches
        statusBuilder.append("=== Branches ===").append("\n");
        statusBuilder.append("*").append(currentBranch).append("\n");
        String[] branchNames = HEADS_DIR.list((dir, name) -> !name.equals(currentBranch));
        if (branchNames != null) {
            Arrays.sort(branchNames);
            for (String branch : branchNames) {
                statusBuilder.append(branch).append("\n");
            }
        }
        statusBuilder.append("\n");

        Map<String, String> addedFiles = stagingArea.getAddedFiles();
        Set<String> removedFiles = stagingArea.getRemoved();

        // staged
        statusBuilder.append("=== Staged Files ===").append("\n");
        appendFilesInOrder(statusBuilder, addedFiles.keySet());
        statusBuilder.append("\n");

        // removed
        statusBuilder.append("=== Removed Files ===").append("\n");
        appendFilesInOrder(statusBuilder, removedFiles);
        statusBuilder.append("\n");

        // modifications not staged for commit
        statusBuilder.append("=== Modifications Not Staged For Commit ===").append("\n");

        List<String> modifiedNotStagedFiles = new ArrayList<>();
        Set<String> deletedNotStagedFiles = new HashSet<>();

        Map<String, String> currentFilesMap = getCurrentFilesMap();
        Map<String, String> trackedFilesMap = headCommit.getTracked();

        // add the staged files
        trackedFilesMap.putAll(addedFiles);

        // remove the staged removed files
        for (String filePath : removedFiles) {
            trackedFilesMap.remove(removedFiles);
        }

        for (Map.Entry<String, String> entry : trackedFilesMap.entrySet()) {
            String filePath = entry.getKey();
            String blobId = entry.getValue();

            String currentFileBlobId = currentFilesMap.get(filePath);
            if (currentFileBlobId != null) {
                // the content has changed but not staged
                if (!currentFileBlobId.equals(blobId)) {
                    modifiedNotStagedFiles.add(filePath);
                }
                currentFilesMap.remove(filePath);
            } else {
                modifiedNotStagedFiles.add(filePath);
                deletedNotStagedFiles.add(filePath);
            }
        }
        modifiedNotStagedFiles.sort(String::compareTo);

        for (String filePath : modifiedNotStagedFiles) {
            String fileName = Paths.get(filePath).getFileName().toString();
            statusBuilder.append(fileName);
            if (deletedNotStagedFiles.contains(fileName)) {
                statusBuilder.append(" (deleted)").append("\n");
            } else {
                statusBuilder.append(" (modified)").append("\n");
            }
        }
        statusBuilder.append("\n");

        // untracked files
        statusBuilder.append("=== Untracked Files ===").append("\n");
        appendFilesInOrder(statusBuilder, currentFilesMap.keySet());
        statusBuilder.append("\n");

        System.out.println(statusBuilder);
    }

    /**
     * A wrapper of appendFilesInOrder, change the interface to suit the key collection
     * @param stringBuilder stringBuilder isntance
     * @param keys key set
     */
    private static void appendFilesInOrder(StringBuilder stringBuilder, Collection<String> keys) {
        List<String> filePathList = new ArrayList<>(keys);
        appendFilesInOrder(stringBuilder, filePathList);
    }

    /**
     * Get file names in lexicographic order and build them in list using StringBuilder
     * @param stringBuilder StringBuilder instance
     * @param filePathList file path list
     */
    private static void appendFilesInOrder(StringBuilder stringBuilder, List<String> filePathList) {
        filePathList.sort(String::compareTo); // sort by lexicographic
        for (String filePath : filePathList) {
            String fileName = Paths.get(filePath).getFileName().toString();
            stringBuilder.append(fileName).append("\n");
        }
    }

    /**
     * Get a Map of file paths and SHA1 ids
     * @return map with file path as keys and SHA1 id as value
     */
    private Map<String, String> getCurrentFilesMap() {
        // update current file list
        currentFiles = CWD.listFiles(File::isFile);
        Map<String, String> filesMap = new HashMap<>();
        for (File file : currentFiles) {
            String filePath = file.getPath();
            String blobId = Blob.createBlobId(file);
            filesMap.put(filePath, blobId);
        }
        return filesMap;
    }

    /** Checkout file from HEAD commit */
    public void checkout(String fileName) {
        String filePath = getFileFromCWD(fileName).getPath();
        if(!headCommit.restoreChecked(filePath)) {
            exit("File does not exist in that commit.");
        }
    }

    /** Checkout file from the specific commit id */
    public void checkout(String commitId, String fileName) {
        String filePath = getFileFromCWD(fileName).getPath();
        commitId = getCompleteCommitId(commitId);
        if (!Commit.fromFile(commitId).restoreChecked(filePath)) {
            exit("File does not exist in that commit.");
        }
    }


    /** Get a complete commit id that fit the UID length */
    private static String getCompleteCommitId(String commitId) {
        if (commitId.length() < UID_LENGTH) {
            if (commitId.length() < 4) {
                exit("Commit id should be longer than 4 characters.");
            }
            String objectDirName = getObjectDirName(commitId);
            File objectDir = join(OBJECTS_DIR, objectDirName);
            if (!objectDir.exists()) {
                exit("No commit with that id exists.");
            }

            boolean ifFound = false;
            String objectFileNamePrefix = getObjectFileName(commitId);
            for (File objectFile : objectDir.listFiles()) {
                String objectFileName = objectFile.getName();
                if (objectFileName.startsWith(objectFileNamePrefix) && isFileInstanceOf(objectFile, Commit.class)) {
                    if (ifFound) {
                        exit("More than 1 commit has the same id prefix.");
                    }
                    commitId = objectDirName + objectFileName;
                    ifFound = true;
                }
            }
            if (!ifFound) {
                exit("No commit with that id exists.");
            }
        } else {
            if (!getObjectFile(commitId).exists()) {
                exit("No commit with that id exists.");
            }
        }
        return commitId;
    }

    /** Checkout to branch */
    public void checkoutBranch(String branch) {
        File targetBranchFile = getBranchHeadFile(branch);
        if (!targetBranchFile.exists()) {
            exit("No such branch exists.");
        }
        if (branch.equals(currentBranch)) {
            exit("No need to checkout the current branch.");
        }
        Commit targetBranchHeadCommit = getBranchHeadCommit(targetBranchFile);
        checkUncheckedFiles(targetBranchHeadCommit);
        checkoutCommit(targetBranchHeadCommit);
        setCurrentBranch(branch);
    }

    /** Exit with message if target commit would overwrite the untracked files. */
    private void checkUncheckedFiles(Commit targetCommit) {
        Map<String, String> currentFilesMap = getCurrentFilesMap();
        Map<String, String> trackedFilesMap = headCommit.getTracked();
        Map<String, String> addedFilesMap = stagingArea.getAddedFiles();
        Set<String> removedFilesSet = stagingArea.getRemoved();

        List<String> untrackedFilesList = new ArrayList<>();

        for (String filePath : currentFilesMap.keySet()) {
            if (trackedFilesMap.containsKey(filePath)) {
                if (removedFilesSet.contains(filePath)) {
                    untrackedFilesList.add(filePath);
                }
            } else {
                if (!addedFilesMap.containsKey(filePath)) {
                    untrackedFilesList.add(filePath);
                }
            }
        }

        Map<String, String> targetCommitTrackedFilesMap = targetCommit.getTracked();

        for (String filePath : untrackedFilesList) {
            String blobId = currentFilesMap.get(filePath);
            String targetBlobId = targetCommitTrackedFilesMap.get(filePath);
            if (!blobId.equals(targetBlobId)) {
                exit("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }
    }


    /**
     * Checkout to specific commit.
     *
     * @param targetCommit Commit instance
     */
    private void checkoutCommit(Commit targetCommit) {
        stagingArea.clear();
        stagingArea.save();
        for (File file : currentFiles) {
            rm(file);
        }
        targetCommit.restoreAllTracked();
    }

    /** Create a new branch if it not exists */
    public void branch(String newBranchName) {
        File newBranchHeadFile = getBranchHeadFile(newBranchName);
        if (newBranchHeadFile.exists()) {
            exit("A branch with that name already exists.");
        }
        setBranchHeadCommit(newBranchHeadFile, headCommit.getCommitId());
    }

    /** remove a existed branch */
    public void rmBranch(String targetBranchName) {
        File targetBranchHeadFile = getBranchHeadFile(targetBranchName);
        if (!targetBranchHeadFile.exists()) {
            exit("A branch with that name does not exist.");
        }
        if (currentBranch.equals(targetBranchName)) {
            exit("Cannot remove the current branch.");
        }
        rm(targetBranchHeadFile);
    }

    /** reset to specific commit id */
    public void reset(String commitId) {
        commitId = getCompleteCommitId(commitId);
        Commit targetCommit = Commit.fromFile(commitId);
        checkUncheckedFiles(targetCommit);
        checkoutCommit(targetCommit);
        setBranchHeadCommit(currentBranch, commitId);
    }

    /**
     * Merge branch.
     *
     * @param targetBranchName Name of the target branch
     */
    public void merge(String targetBranchName) {
        File targetBranchFile = getBranchHeadFile(targetBranchName);
        if (!targetBranchFile.exists()) {
            exit("A branch with that name does not exist.");
        }
        if (targetBranchName.equals(currentBranch)) {
            exit("Cannot merge a branch with itself.");
        }
        if (!stagingArea.isEmpty()) {
            exit("You have uncommitted changes.");
        }

        Commit targetBranchHeadCommit = getBranchHeadCommit(targetBranchFile);
        checkUncheckedFiles(targetBranchHeadCommit);

        Commit latestAncestorCommit = getLatestAncestorCommit(targetBranchHeadCommit, headCommit);
        String latestAncestorCommitId = latestAncestorCommit.getCommitId();

        if (latestAncestorCommitId.equals(targetBranchHeadCommit.getCommitId())) {
            exit("Given branch is an ancestor of the current branch.");
        }

        if (latestAncestorCommitId.equals(headCommit.getCommitId())) {
            checkoutCommit(targetBranchHeadCommit);
            setCurrentBranch(targetBranchName);
            exit("Current branch fast-forwarded.");
        }

        boolean ifConflict = false;

        Map<String, String> headCommitTrackedFilesMap = headCommit.getTracked();
        Map<String, String> targetBranchHeadCommitTrackedFilesMap = targetBranchHeadCommit.getTracked();
        Map<String, String> latestAncestorCommitTrackedFilesMap = latestAncestorCommit.getTracked();

        for (Map.Entry<String, String> entry : latestAncestorCommitTrackedFilesMap.entrySet()) {
            String filePath = entry.getKey();
            String blobId = entry.getValue();
            File file = new File(filePath);

            String headCommitBlobId = headCommitTrackedFilesMap.get(filePath);
            String targetBranchHeadCommitBlobId = targetBranchHeadCommitTrackedFilesMap.get(filePath);

            if (targetBranchHeadCommitBlobId != null) {
                if (!targetBranchHeadCommitBlobId.equals(blobId)) { // modified in target branch
                    if (headCommitBlobId != null) {
                        if (headCommitBlobId.equals(blobId)) { // not modified in current branch
                            // case 1
                            Blob.fromFile(targetBranchHeadCommitBlobId).writeContentToSource();
                            stagingArea.add(file);
                        } else { // modified in current branch
                            if (!headCommitBlobId.equals(targetBranchHeadCommitBlobId)) {
                                // case 8, both branch modified the same file to different content, leading to confict.
                                ifConflict = true;
                                String conflictContent = getConflictContent(headCommitBlobId, targetBranchHeadCommitBlobId);
                                writeContents(file, conflictContent);
                                stagingArea.add(file);
                            }
                            // else modified identically
                            // case 3, do nothing
                        }
                    } else { // deleted in current branch
                        // case 3
                        ifConflict = true;
                        String conflictContent = getConflictContent(null, targetBranchHeadCommitBlobId);
                        writeContents(file, conflictContent);
                        stagingArea.add(file);
                    }
                }
                // else not modified in the target branch
                // case 2 and case 7
            } else { // deleted in the target branch
                if (headCommitBlobId != null) {
                    if (headCommitBlobId.equals(blobId)) { // not modified in current branch
                        // case 6, remove it
                        stagingArea.remove(file);
                    } else { // modified in current branch
                        // case 8
                        ifConflict = true;
                        String conflictContent = getConflictContent(headCommitBlobId, null);
                        writeContents(file, conflictContent);
                        stagingArea.add(file);
                    }
                } // else deleted in both branch
                // case 3
            }

            headCommitTrackedFilesMap.remove(filePath);
            targetBranchHeadCommitTrackedFilesMap.remove(filePath);
        }

        for (Map.Entry<String, String> entry : targetBranchHeadCommitTrackedFilesMap.entrySet()) {
            String filePath = entry.getKey();
            String blobId = entry.getValue();
            File file = new File(filePath);

            String headCommitBlobId = headCommitTrackedFilesMap.get(filePath);

            if (headCommitBlobId != null) { // added in both branches
                if (!headCommitBlobId.equals(blobId)) { // modified in different ways
                    // case 8
                    ifConflict = true;
                    String conflictContent = getConflictContent(headCommitBlobId, blobId);
                    writeContents(file, conflictContent);
                    stagingArea.add(file);
                } // else modified in the same ways
                // case 3
            } else { // only added in the target branch
                // case 5
                Blob.fromFile(blobId).writeContentToSource();
                stagingArea.add(file);
            }
        }

        String newCommitMessage = "Merged" + " " + targetBranchName + " " + "into" + " " + currentBranch + ".";
        commit(newCommitMessage, targetBranchHeadCommit.getCommitId());

        if (ifConflict) {
            message("Encountered a merge conflict.");
        }
    }

    /**
     * Get commit A and commit B latest common ancestor commit
     * @param commitA commit A in one branch
     * @param commitB commit B in another branch
     * @return latest ancestor commit
     */
    private Commit getLatestAncestorCommit(Commit commitA, Commit commitB) {
        Comparator<Commit> commitComparator = Comparator.comparing(Commit::getDate).reversed();
        PriorityQueue<Commit> commitPriorityQueue = new PriorityQueue<>(commitComparator);
        commitPriorityQueue.add(commitA);
        commitPriorityQueue.add(commitB);
        Set<String> checkedCommitId = new HashSet<>();

        while (true) {
            Commit latestCommit = commitPriorityQueue.poll();
            String firstParentId = latestCommit.getParents().get(0);
            Commit firstParentCommit = Commit.fromFile(firstParentId);
            if (checkedCommitId.contains(firstParentId)) {
                return firstParentCommit;
            }
            commitPriorityQueue.add(firstParentCommit);
            checkedCommitId.add(firstParentId);
        }
    }

    private String getConflictContent(String currentBranchCommitBlobId, String targetBranchCommitBlobId) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("<<<<<<< HEAD").append("\n");
        if (currentBranchCommitBlobId != null) {
            Blob currentBranchCommitBlod = Blob.fromFile(currentBranchCommitBlobId);
            contentBuilder.append(currentBranchCommitBlod.getContentAsString());
        }
        contentBuilder.append("=======").append("\n");
        if (targetBranchCommitBlobId != null) {
            Blob targetBranchCommitBlod = Blob.fromFile(targetBranchCommitBlobId);
            contentBuilder.append(targetBranchCommitBlod.getContentAsString());
        }
        contentBuilder.append(">>>>>>>").append("\n");
        return contentBuilder.toString();
    }
}

