import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class Terminal {
    private static Parser parser;
    public void removeDirectory(File f) throws IOException {
        if (f.isDirectory()) {
            String[] files = f.list();
            for (String file : files) {
                removeDirectory(new File(f, file));
            }
        }
        Files.deleteIfExists(f.toPath());
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
        // removeDirectory test
        t.removeDirectory(new File("D:\\dest\\1\\New folder"));
    }
}
