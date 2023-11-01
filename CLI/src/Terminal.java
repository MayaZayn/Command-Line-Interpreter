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
            t.echo();
            t.pwd();
            t.display();
            break;
        }
    }

    public void echo(){
        output = "";
        for (String arg : parser.getArgs()) {
            if(arg.equals(">") || arg.equals(">>")){
                break;
            }
            output += arg + " ";
        }
        output = output.substring(0, output.length() - 1);
    }
    public void pwd(){
        if (parser.getArgs().size() > 0){
            System.err.println("bad arguments, pwd takes no arguments.");
        }
        output = currentDir.toString();
    }

    public void display(){
        System.out.println(output);
    }
}
