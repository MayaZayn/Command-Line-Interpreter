import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

public class Terminal {
    private static Parser parser;
    public void rm(String s) throws IOException {
        String curDirectory = new File("").getAbsolutePath();
        File file = new File(curDirectory, s);
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException noSuchFileException) {
            System.out.println("rm: cannot remove '" + s + "': No such file or directory");
        }
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
        t.rm("New Text Document.txt");
    }
}
