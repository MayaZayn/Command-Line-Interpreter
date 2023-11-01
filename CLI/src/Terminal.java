import java.util.Scanner;

public class Terminal {
    private String output;
    private String currentDir;
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
            t.display();
            break;
        }
    }

    public void cd(){

    public void pwd(){
        // test if error بيضرب
        // import java.nio.file.Paths;
        if (parser.getArgs().size() > 0){
            System.err.println("bad arguments, pwd takes no arguments.");
        }
        currentDir = "";
        currentDir = Paths.get("").toAbsolutePath().toString();
        output = currentDir;
    }


    public void display(){
        System.out.println(output);
    }
}
