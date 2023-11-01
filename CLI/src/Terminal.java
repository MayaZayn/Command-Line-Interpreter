import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Terminal {
    private String output;
    private Path currentDir = Paths.get("").toAbsolutePath();
    private static Parser parser = new Parser();
    public void chooseCommandAction() {

    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            String input = s.nextLine();
            input = input.strip();
            if (!input.isEmpty()) {
                parser.parse(input);
            }
            Terminal t = new Terminal();
            t.cd();
            //t.pwd();

            t.display();
            break;
        }
    }

    public void cd() {
        if (parser.getArgs().isEmpty()){ // cd
            currentDir = currentDir.resolve(currentDir.getRoot());
        }else{
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
                }else {
                    System.err.println("bad arguments, invalid path given.");
                }
            }catch (InvalidPathException ex) {
                System.err.println("bad arguments, invalid path given.");
            }
        }


    }

    public void display(){
        System.out.println(output);
    }
}
