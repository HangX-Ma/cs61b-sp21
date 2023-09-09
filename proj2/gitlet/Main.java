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
            // TODO: FILL THE REST IN
            default -> exit("No command with that name exits.");
        }
    }

    public static void argsLengthCheck(String[] args, int n) {
        if (args.length != n) {
            exit("Incorrect operands.");
        }
    }
}
