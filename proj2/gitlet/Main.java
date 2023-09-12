package gitlet;

import static gitlet.SelfUilts.exit;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author HangX-Ma
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // handle the case that the input args are empty
        if (args.length == 0) {
            exit("Please enter a command.");
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init" -> {
                argsLengthCheck(args, 1);
                Repository.init();
            }
            case "add" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String fileName = args[1];
                new Repository().add(fileName);
            }
            case "commit" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String commitMsg = args[1];
                if (commitMsg.isEmpty()) {
                    exit("Please enter a commit message.");
                }
                new Repository().commit(commitMsg);
            }
            case "rm" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String fileName = args[1];
                new Repository().remove(fileName);
            }
            case "log" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 1);
                new Repository().log();
            }
            case "global-log" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 1);
                new Repository().globalLog();
            }
            case "find" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String message = args[1];
                if (message.length() == 0) {
                    exit("Found no commit with that message.");
                }
                new Repository().find(message);
            }
            case "status" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 1);
                new Repository().status();
            }
            case "checkout" -> {
                Repository.workspaceCheck();
                Repository repository = new Repository();
                switch (args.length) {
                    case 3 -> {
                        if (!args[1].equals("--")) {
                            exit("Incorrect operands.");
                        }
                            String fileName = args[2];
                            repository.checkout(fileName);
                        }
                    case 4 -> {
                        if (!args[2].equals("--")) {
                            exit("Incorrect operands.");
                        }
                        String commitId = args[1];
                        String fileName = args[3];
                        repository.checkout(commitId, fileName);
                    }
                    case 2 -> {
                        String branchName = args[1];
                        repository.checkoutBranch(branchName);
                    }
                    default -> exit("Incorrect operands.");
                }
            }
            case "branch" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String branchName = args[1];
                new Repository().branch(branchName);
            }
            case "rm-branch" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String branchName = args[1];
                new Repository().rmBranch(branchName);
            }
            case "reset" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String commitId = args[1];
                new Repository().reset(commitId);
            }
            case "merge" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String branchName = args[1];
                new Repository().merge(branchName);
            }
            default -> exit("No command with that name exits.");
        }
    }

    public static void argsLengthCheck(String[] args, int n) {
        if (args.length != n) {
            exit("Incorrect operands.");
        }
    }
}
