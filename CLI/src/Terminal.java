import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class Terminal {
    private static Parser parser;
    public void rm(File file) throws IOException {
        Files.deleteIfExists(file.toPath());
    }
    public void chooseCommandAction() {

    }
    public static void main(String[] args) throws IOException {
//        Scanner s = new Scanner(System.in);
//        while (true) {
//            String input = s.nextLine();
//            input = input.strip();
//            if (!input.isEmpty()) {
//                parser.parse(input);
//            }
//        }
        Terminal t = new Terminal();
        // rm test
        t.rm(new File("D:\\dest\\1\\New Microsoft Excel Worksheet.xlsx"));
    }
}
