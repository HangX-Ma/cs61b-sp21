package gitlet;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gitlet.SelfUilts.exit;
import static gitlet.SelfUilts.mkdir;
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

    /**
     * Perform a commit with specific message
     *
     * @param msg message
     */
    public void commit(String msg) {
        if (stagingArea.isEmpty()) {
            exit("No changes added to the commit.");
        }
        Map<String, String> newTrackedFilesMap = stagingArea.commit();
        stagingArea.save();
        List<String> parents = new ArrayList<>();
        parents.add(headCommit.getCommitId());

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
}
