import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

public class Terminal {
    private static Parser parser;
    // rmdir *
    public void removeDirectories(String s) throws IOException {
        File f = new File(s);
        if (f.isDirectory()) {
            String[] files = f.list();
            for (String file : files)
                removeDirectories(String.valueOf(new File(f, file)));
            try {
                Files.delete(f.toPath());
            } catch (NoSuchFileException noSuchFileException) {
                System.out.println("rmdir: cannot remove '" + s + "': No such directory");
            } catch (DirectoryNotEmptyException directoryNotEmptyException) {
                System.out.println("rmdir: cannot remove '" + s + "': Directory not empty");
            }
        }
        // Handle the case if the folder doesn't exist
    }
    // rmdir pathname (relative or absolute)
    public void removeDirectory(String s) throws IOException {
        String curDirectory = new File("").getAbsolutePath();
        File file = new File(s);
        if (!file.isAbsolute())
            file = new File(curDirectory, s);
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException noSuchFileException) {
            System.out.println("rmdir: cannot remove '" + s + "': No such file or directory");
        } catch (DirectoryNotEmptyException directoryNotEmptyException) {
            System.out.println("rmdir: cannot remove '" + s + "': Directory not empty");
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
        // removeDirectories test
        t.removeDirectories("New folder");
        // removeDirectory tests
        t.removeDirectories("New folder");
        t.removeDirectories("C:\\Users\\Maya Ayman\\Desktop\\OS_1st_Assignment\\Command-Line-Interpreter\\CLI\\New folder");
    }
}
