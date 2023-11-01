import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Terminal {
    private String output = "";
    private Path currentDir = Paths.get("").toAbsolutePath();
    static List<String> inputHistory = new ArrayList<>();
    final static String setPlainText = "\033[0;0m";
    final static String setBoldText = "\033[0;1m";
    private static Parser parser = new Parser();
    public void chooseCommandAction() throws IOException {
        switch (parser.getCommandName()){
            case "mkdir":
                mkdir();
                break;
            case "ls":
                ls();
                break;
            case "touch":
                touch();
                break;
            case "cp":
                if (Objects.equals(parser.getCommandOptions().get(0), "-r"))
                    copyDirectory(new File(parser.getArgs().get(0)), new File(parser.getArgs().get(0)));
                else
                    cp();
                break;
            case "pwd":
                pwd();
                break;
            case "cd":
                cd();
                break;
            case "rmdir":
                if (Objects.equals(parser.getArgs().get(0), "*"))
                    removeDirectories(currentDir.toString());
                else
                    removeDirectory();
                break;
            case "echo":
                echo();
                break;
            case "cat":
                cat();
                break;
            case "rm":
                rm();
                break;
            case "history":
                history();
                break;
        }
    }
    public void cd() {
        if (parser.getArgs().isEmpty()){ // cd
            currentDir = currentDir.resolve(currentDir.getRoot());
        } else {
            String path = "";
            for (String arg : parser.getArgs()) {
                path += arg + " ";
            }
            path = path.substring(0, path.length() - 1);

            Path newPath;
            try {
                newPath = Paths.get(path);
                if (!newPath.isAbsolute()){
                    path = currentDir + "\\" + path;
                    newPath = Paths.get(path);
                }
                if(Files.exists(newPath)){
                    currentDir = currentDir.resolve(newPath);
                } else {
                    System.err.println("bad arguments, invalid path given.");
                }
            } catch (InvalidPathException ex) {
                System.err.println("bad arguments, invalid path given.");
            }
        }
    }
    public void echo() {
        output = "";
        for (String arg : parser.getArgs()) {
            if(arg.equals(">") || arg.equals(">>")){
                break;
            }
            output += arg + " ";
        }
        output = output.substring(0, output.length() - 1);
    }
    public void pwd() {
        output = currentDir.toString();
    }
    public void display() {
        System.out.println(output);
    }
    public void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }
            String[] files = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);
                copyDirectory(srcFile, destFile);
            }
        } else {
            try {
                Files.copy(source.toPath(), destination.toPath());
            } catch (FileAlreadyExistsException e) {}
        }
    }
    // rmdir *
    public void removeDirectories(String s) throws IOException {
        File f = new File(s);
        if (f.isDirectory()) {
            String[] files = f.list();
            for (String file : files)
                removeDirectories(String.valueOf(new File(f, file)));
            try {
                Files.delete(f.toPath());
            } catch (NoSuchFileException noSuchFileException) {
                output = "rmdir: cannot remove '" + s + "': No such directory";
            } catch (DirectoryNotEmptyException directoryNotEmptyException) {
                output = "rmdir: cannot remove '" + s + "': Directory not empty";
            }
        }
        // Handle the case if the folder doesn't exist
    }
    // rmdir pathname (relative or absolute)
    public void removeDirectory() throws IOException {
        String s = parser.getArgs().get(0);
        String curDirectory = new File("").getAbsolutePath();
        File file = new File(s);
        if (!file.isAbsolute())
            file = new File(curDirectory, s);
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException noSuchFileException) {
            output = "rmdir: cannot remove '" + s + "': No such file or directory";
        } catch (DirectoryNotEmptyException directoryNotEmptyException) {
            output = "rmdir: cannot remove '" + s + "': Directory not empty";
        }
    }
    public void rm() throws IOException {
        String curDirectory = new File("").getAbsolutePath();
        File file = new File(curDirectory, parser.getArgs().get(0));
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException noSuchFileException) {
            output = "rm: cannot remove '" + parser.getArgs().get(0) + "': No such file or directory";
        }
    }
    public void history(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < inputHistory.size(); i++) {
            builder.append(i + 1).append("  ").append(inputHistory.get(i)).append("\n");
        }
        output = builder.toString();
    }
    public void cat() {
        //handle paths with empty space
        //Reset arguments list in parser
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parser.getArgs().size(); i++) {
            String filename= parser.getArgs().get(i);
            File file = new File(filename);
            try {
                Scanner fileSc = new Scanner(file);
                while(fileSc.hasNextLine()){
                    result.append(fileSc.nextLine()).append("\n");
                }
            } catch (FileNotFoundException fileNotFoundException){
                output = "File not Found";
                return;
            }
        }
        output = result.toString();
    }
    public void ls() {
        File file = new File(System.getProperty("user.dir"));
        String[] paths = file.list();
        if (!parser.getCommandOptions().isEmpty() && parser.getCommandOptions().get(0).equals("-r"))  {
            Arrays.sort(paths, Collections.reverseOrder());
        }
        for (String a : paths) {
            output += a + " ";
        }
    }
    public void touch() {
        File file = new File(parser.getArgs().get(0));

        try {
            file.createNewFile();
        }
        catch (IOException e) {
            output = setBoldText + "touch" + setPlainText +
                    ": can't create file " + parser.getArgs().get(0)
                    + setBoldText +
                    ": File already exist at this location" + setPlainText;
            display();
        }
    }
    public void mkdir(){
        for(String arg : parser.getArgs()){

            File file = new File(arg);

            if(!file.mkdirs()){
                output = setBoldText + "mkdir" + setPlainText +
                        ": can't create directory " + arg
                        + setBoldText +
                        ": No such file or directory" + setPlainText;
                display();
            }
        }
    }
    public void cp() throws IOException {
        File file1 = new File(parser.getArgs().get(0));
        File file2 = new File(parser.getArgs().get(1));

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(file1);
            out = new FileOutputStream(file2);

            int line;
            while((line = in.read()) != -1){
                out.write(line);
            }
        }
        catch (IOException e) {
            output = setBoldText + "mkdir" + setPlainText +
                    ": can't copy file "
                    + setBoldText +
                    ": No such files exists" + setPlainText;
            display();
        }
        finally {
            if(in != null) {
                in.close();
            }

            if(out != null){
                out.close();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        Terminal t = new Terminal();

        while (true) {
            String input = s.nextLine();
            input = input.strip();
            if (!input.isEmpty()) {
                parser.parse(input);
            }
            t.chooseCommandAction();
            t.display();
            inputHistory.add(input);
//            break;
        }
    }

}
