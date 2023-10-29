import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Terminal {
    private static Parser parser = new Parser();
    public static String cat() {
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
            }catch (FileNotFoundException fileNotFoundException){
                return "File not Found";
            }
        }
        return result.toString();
    }
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
//            System.out.println(cat());
//            parser.resetArgs();
        }
    }
}
