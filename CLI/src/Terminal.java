import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Terminal {
    private String output = "";
    private Path currentDir = Paths.get(System.getProperty("user.home") + "/Desktop");
    static List<String> inputHistory = new ArrayList<>();
    final static String setPlainText = "\033[0;0m";
    final static String setBoldText = "\033[0;1m";
    final static String CYAN = "\u001B[36m";
    final static String RED = "\u001B[31m";
    final static String YELLOW = "\u001B[33m";
    final static String RESET = "\u001B[0m";

    private static Parser parser = new Parser();
    public void chooseCommandAction() throws IOException {
        switch (parser.getCommandName()) {
            case "mkdir" -> mkdir();
            case "ls" -> ls();
            case "touch" -> touch();
            case "cp" -> {
                if (!parser.getCommandOptions().isEmpty() &&
                        Objects.equals(parser.getCommandOptions().get(0), "-r")) {
                    File source, destination;
                    String toAdd;
                    String temp = parser.getArgs().get(0).toString();

                    if (!new File(parser.getArgs().get(0)).isAbsolute()) {
                        source = new File(currentDir + "\\" + parser.getArgs().get(0));
                    } else {
                        source = new File(parser.getArgs().get(0));
                    }

                    if (temp.lastIndexOf("\\") == -1) {
                        toAdd = "\\" + temp;
                    } else {
                        toAdd = temp.substring(temp.lastIndexOf("\\"), temp.length());
                    }

                    if (!new File(parser.getArgs().get(1)).isAbsolute()) {
                        destination = new File(currentDir + "\\" +
                                parser.getArgs().get(1)
                                + toAdd);
                    } else {
                        destination = new File(parser.getArgs().get(1)
                                + toAdd);
                    }
                    copyDirectory(source, destination);
                } else
                    cp();
            }
            case "pwd" -> pwd();
            case "cd" -> cd();
            case "rmdir" -> {
                if (Objects.equals(parser.getArgs().get(0), "*"))
                    removeDirectories(currentDir.toString(), currentDir.toString());
                else
                    removeDirectory();
            }
            case "echo" -> echo();
            case "cat" -> cat();
            case "rm" -> rm();
            case "history" -> history();
            case "exit" -> System.exit(0);
        }
    }
    public void cd() {
        if (parser.getArgs().isEmpty()){ // cd
            currentDir = currentDir.resolve(currentDir.getRoot());
        }
        else {
            String path = "";
            for (String arg : parser.getArgs()) {
                path += arg + " ";
            }
            path = path.substring(0, path.length() - 1);

            Path newPath;

            if (path.equals("..")) {
                newPath = Paths.get(currentDir.toString()).getParent();
                currentDir = currentDir.resolve(newPath);
            } else {
                try {
                    newPath = Paths.get(path);
                    if (!newPath.isAbsolute()) {
                        path = currentDir + "\\" + path;
                        newPath = Paths.get(path);
                    }
                    if (Files.exists(newPath)) {
                        currentDir = currentDir.resolve(newPath);
                    } else {
                        output = setBoldText + RED + "Bad arguments, Invalid path given."
                                + setPlainText + RESET;
                    }
                } catch (InvalidPathException ex) {
                    output = setBoldText + RED + "Bad arguments, Invalid path given."
                            + setPlainText + RESET;
                }
            }
        }
    }
    public void echo() {
        output = "";
        for (String arg : parser.getArgs()) {
            if(arg.equals(">") || arg.equals(">>")){
                break;
            }
            output += setBoldText + CYAN + arg + " "
            + setPlainText + RESET;
        }
//        if(!output.isEmpty()){
//            output = output.substring(0, output.length() - 1);
//        }
    }
    public void pwd() {
        output = setBoldText + "\u001B[34m" + currentDir.toString()
                + setPlainText + RESET;
    }
    public void display() {
        System.out.println(output);
    }


    /*
    Case 1 source absolute , destination not absolute  RIGHT
    need to take last folder name of source and put it in the destination name
    src: H:\ME71\3rd Year\1st Term\OS\Assignment\Command-Line-Interpreter\gg
    dest: bb,   new dest: bb\gg.

    Case 2 source absolute, destination absolute RIGHT
    need to take last source name and put it in the destination name
    src: H:\ME71\3rd Year\1st Term\OS\Assignment\Command-Line-Interpreter\gg
    dest: H:\ME71\3rd Year\1st Term\OS\Assignment\Command-Line-Interpreter\hh
    new dest: H:\ME71\3rd Year\1st Term\OS\Assignment\Command-Line-Interpreter\hh\gg.

    Case 3 source not absolute, destination absolute   RIGHT
    src: aa
    dest: H:\ME71\3rd Year\1st Term\OS\Assignment\Command-Line-Interpreter\gg
    new dest: H:\ME71\3rd Year\1st Term\OS\Assignment\Command-Line-Interpreter\gg\aa;

    Case 4 source not absolute, destination not absolute  RIGHT
    src: aa
    dest: bb
    new dest: bb\aa;
     */

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
            try {
                Files.copy(source.toPath(), destination.toPath());
            } catch (FileAlreadyExistsException e) {}
        } else { // ALERT needed or not ???!!!!!!
            try {
                Files.copy(source.toPath(), destination.toPath());
            } catch (FileAlreadyExistsException e) {}
        }
    }
    // rmdir *
    /*
    solve problem that main folder where deleted.
     */
    public void removeDirectories(String s, String root) throws IOException {
        File f = new File(s);
        if (f.isDirectory()) {
            String[] files = f.list();
            for (String file : files)
                removeDirectories(String.valueOf(new File(f, file)), root);
            try {
                if(!f.toString().equals(root)) {
                    Files.delete(f.toPath());
                }
            } catch (NoSuchFileException noSuchFileException) {
                output = setBoldText + YELLOW + "rmdir" + setPlainText +
                        ": cannot remove '" + s + "': " +
                        setBoldText + RED +"No such directory" + setPlainText;
            } catch (DirectoryNotEmptyException directoryNotEmptyException) {
                output = setBoldText + YELLOW + "rmdir" + setPlainText +
                        ": cannot remove '" + s + "': " +
                        setBoldText + RED +"Directory not empty" + setPlainText;
            }
        }
        // Handle the case if the folder doesn't exist   IT MUST BE EXISTED>
    }

    // rmdir pathname (relative or absolute)
    public void removeDirectory() throws IOException {
        String s = parser.getArgs().get(0);
        File file = new File(s);
        if (!file.isAbsolute())
            file = new File(currentDir.toString(), s);
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException noSuchFileException) {
            output = setBoldText + YELLOW + "rmdir" + setPlainText +
                    ": cannot remove '" + s + "': " +
                    setBoldText + RED +"No such directory" + setPlainText;
        } catch (DirectoryNotEmptyException directoryNotEmptyException) {
            output = setBoldText + YELLOW + "rmdir" + setPlainText +
                    ": cannot remove '" + s + "': " +
                    setBoldText + RED +"Directory not empty" + setPlainText;
        }
    }
    public void rm() throws IOException {
        File file = new File(currentDir.toString(), parser.getArgs().get(0));
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException noSuchFileException) {
            output = setBoldText + "rm" + setPlainText +
                    ": cannot remove '" + parser.getArgs().get(0) + "': " +
                    setBoldText +  "No such file or directory" + setPlainText;
        }
    }
    public void history(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < inputHistory.size(); i++) {
            builder.append(setBoldText + (i + 1) + setPlainText).append("  ")
                    .append(setBoldText + YELLOW + inputHistory.get(i))
                    .append(setPlainText + RESET + "\n");
        }
        output = builder.toString();
        if(output.isEmpty()){
            output = setBoldText + YELLOW + "History: " + RESET +
                     setBoldText + "Empty no commands' found" + setPlainText;
        }
    }
    public void cat() {
        //handle paths with empty space
        //Reset arguments list in parser
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parser.getArgs().size(); i++) {
            String filename= parser.getArgs().get(i);

            if(!new File(filename).isAbsolute()){
                filename = currentDir + "\\" + filename;
            }
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
        File file = new File(currentDir.toString());
        String[] paths = file.list();
        if (!parser.getCommandOptions().isEmpty() && parser.getCommandOptions().get(0).equals("-r"))  {
            Arrays.sort(paths, Collections.reverseOrder());
        }
        for (String a : paths) {
            output += setBoldText + CYAN + a + " " + setPlainText + RESET;
        }
    }
    public void touch() {
        File file = new File(currentDir + "\\" + parser.getArgs().get(0));

        try {
            if(!file.createNewFile()){
                output = setBoldText + YELLOW +"touch" + setPlainText +
                        ": can't create file.\n" + parser.getArgs().get(0) + ": "
                        + setBoldText + RED +
                        "File already exist at this location" + setPlainText + RESET;
            }
        }
        catch (IOException e) {
            output = setBoldText + YELLOW +"touch" + setPlainText +
                    ": can't create file.\n" + parser.getArgs().get(0) + ": "
                    + setBoldText + RED +
                    "Invalid Input" + setPlainText + RESET;
        }
    }
    public void mkdir(){
        for(String arg : parser.getArgs()){

            File file = new File(currentDir + "\\" + arg);

            if(!file.mkdirs()){
                output = setBoldText + YELLOW + "mkdir" + setPlainText +
                        ": can't create directory\n" + arg + ": "
                        + setBoldText + RED +
                        "File Exists" + setPlainText;
            }
        }
    }
    public void cp() throws IOException {
        File file1;
        File file2;

        if(!new File(parser.getArgs().get(0)).isAbsolute()){
            file1 = new File(currentDir + "\\" + parser.getArgs().get(0));
        }
        else{
            file1 = new File(parser.getArgs().get(0));
        }

        if(!new File(parser.getArgs().get(1)).isAbsolute()){
            file2 = new File(currentDir + "\\" + parser.getArgs().get(1));
        }
        else{
            file2 = new File(parser.getArgs().get(1));
        }
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
            parser.parserReset();
            t.output = "";
            System.out.print(setBoldText + "LocalHost:" + "# " + setPlainText);
            String input = s.nextLine();
            input = input.strip();
            if (!input.isEmpty()) {
                parser.parse(input);
            }
            t.chooseCommandAction();
            t.display();
            inputHistory.add(input);
        }
    }

}
