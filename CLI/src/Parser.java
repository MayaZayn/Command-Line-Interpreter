import java.util.ArrayList;

public class Parser {
    private String commandName;
    private ArrayList<String> commandOptions;
    private ArrayList<String> args;

    {
        commandOptions = new ArrayList<>();
        args = new ArrayList<>();
    }

    private enum commands {
        echo, pwd, cd, ls, mkdir, rmdir, touch, cp, rm, cat, wc, history, exit
    }

    public boolean parse(String input) {
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
    public void pathChecker() {
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
            } else {
                newArgs.add(args.get(i));
            }
        }
        args = newArgs;
    }
    public void parserReset(){
        commandName = "";
        args.clear();
        commandOptions.clear();
    }
    public boolean checkArgs() {
        return switch (commandName) {
            case "pwd", "ls", "history" -> args.isEmpty();
            case "echo", "rmdir", "touch", "rm" -> args.size() == 1;
            case "cd" -> args.isEmpty() || args.size() == 1;
            case "mkdir" -> !args.isEmpty();
            case "cp" -> args.size() == 2;
            case "cat" -> args.size() == 1 || args.size() == 2;
            default -> false;
        };
    }
    public boolean checkOptions() {
        return switch (commandName) {
            case "ls", "cp" -> commandOptions.get(0).equals("-r") && commandOptions.size() == 1;
            default -> commandOptions.isEmpty();
        };
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
