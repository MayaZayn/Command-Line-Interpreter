import java.util.ArrayList;

public class Parser {
    private static String commandName;
    private static ArrayList<String> commandOptions;
    private static ArrayList<String> args;

    static {
        commandOptions = new ArrayList<String>();
        args = new ArrayList<String>();
    }

    private static enum commands {
        echo, pwd, cd, ls, mkdir, rmdir, touch, cp, rm, cat, wc, history, exit
    };

    public boolean parse(String input) { // ls -l
        String[] instruction = input.split(" ");
        commandName = instruction[0];

        try {
            commands.valueOf(commandName).ordinal();
        } catch(IllegalArgumentException i) {
            return false;
        }

        for (int i = 1; i < instruction.length; i++) {
            if (instruction[i].charAt(0) == '-') {
                commandOptions.add(instruction[i]);
            } else {
                args.add(instruction[i]);
            }
        }
        pathChecker();
        return checkArgs() && checkOptions();
    }

    public static void pathChecker() {
        if(args.isEmpty()){
            return;
        }

        ArrayList<String> newArgs = new ArrayList<>();

        for (int i = 0; i < args.size(); ++i) {
            if (args.get(i).charAt(0) == '"') {
                String pathArg = args.get(i);

                if (!args.get(i).endsWith("\"")) {
                    for (int j = i + 1; j < args.size(); ++j) {
                        pathArg += " " + args.get(j);

                        if (args.get(j).endsWith("\"") || args.get(j).startsWith("\"")) {
                            i = j;
                            break;
                        }
                    }
                    pathArg = pathArg.substring(1, pathArg.length()-1);
                    newArgs.add(pathArg);
                }
            }
            else {
                newArgs.add(args.get(i));
            }
        }
        args = newArgs;
    }

    public boolean checkArgs() {
        switch (commandName) {
            case "pwd","ls","history":
                return args.isEmpty();
            case "echo","rmdir","touch","rm":
                return args.size() == 1;
            case "cd":
                return args.isEmpty() || args.size() == 1;
            case "mkdir":
                return !args.isEmpty();
            case "cp":
                return args.size() == 2;
            case "cat":
                return args.size() == 1 || args.size() == 2;
        }
        return false;
    }

    public boolean checkOptions() {
        switch (commandName) {
            case "ls","cp":
                return commandOptions.get(0).equals("-r") && commandOptions.size() == 1;
            default:
               return commandOptions.isEmpty();
        }
    }
    public String getCommandName() {
        return commandName;
    }
    public ArrayList<String> getArgs() {
        return args;
    }
    public ArrayList<String> getCommandOptions() {
        return commandOptions;
    }
}
