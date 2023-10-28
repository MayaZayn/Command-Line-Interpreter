import java.util.Scanner;

public class Terminal {
    private String output = "";
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

    public void echo(){
        // to be added:
        // -e option --> enables the interpretation of backslash escapes
        // * --> echo * = ls
        // -n --> ???

        // to be handled:
        // backslash in input without " "
        // input with " " --> remove " "
        for (String arg : parser.getArgs()) {
            output += arg + " ";
        }
    }

    public void display(){
        System.out.println(output);
    }
}
