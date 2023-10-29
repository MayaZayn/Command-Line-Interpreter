import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Terminal {
    private static Parser parser = new Parser();
    static List<String> inputHistory = new ArrayList<>();
    public static String history(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < inputHistory.size(); i++) {
            builder.append(i + 1).append("  ").append(inputHistory.get(i)).append("\n");
        }
        return builder.toString();
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
//            inputHistory.add(input);
//            System.out.println(history());
        }
    }
}
