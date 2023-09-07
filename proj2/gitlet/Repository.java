package gitlet;

import java.io.File;

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
    public static final String DEAULT_BRANCH = "master";

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** The HEAD file: current activated branch, refs/heads/<name> */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    /** The HEAD file content: ref prefix */
    public static final String HEAD_REF_PREFIX = "refs: refs/heads/";

    /** objects directory: record the committed files */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");

    /** refs directory */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    /** heads directory: record the git branches */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");

    /**
     * Initialize a repository at the current working directory.
     *
     * <pre>
     * .gitlet
     * ├── HEAD
     * ├── objects
     * └── refs
     *     └── heads
     * </pre>
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            exit("A Gitlet version-control system already exists in the current directory.");
        }
        mkdir(GITLET_DIR);
        mkdir(REFS_DIR);
        mkdir(OBJECTS_DIR);
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
        updateBranchHeadFileCommitId(DEAULT_BRANCH, initialCommit.getCommitId());
    }

    private static void updateBranchHeadFileCommitId(String branch, String commitId) {
        File branchHeadFile = join(HEADS_DIR, branch);
        writeObject(branchHeadFile, commitId);
    }
}
