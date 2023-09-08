package gitlet;

import java.util.ResourceBundle;

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
                Repository.config();
            }
            case "add" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String fileName = args[1];
                Repository.config();
                Repository.add(fileName);
            }
            case "commit" -> {
                Repository.workspaceCheck();
                argsLengthCheck(args, 2);
                String commitMsg = args[1];
                if (commitMsg.isEmpty()) {
                    exit("Please enter a commit message.");
                }
                Repository.config();
                Repository.commit(commitMsg);
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
