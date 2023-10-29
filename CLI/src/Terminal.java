import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Scanner;

public class Terminal {
    private static Parser parser;
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
        } else {
            try {
                Files.copy(source.toPath(), destination.toPath());
            } catch (FileAlreadyExistsException e) {}
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
        // cp -r test
        t.copyDirectory(new File("D:\\source"), new File("D:\\dest"));
    }
}
