import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Terminal {
    private static Parser parser = new Parser();
    private static String output;

    final static String setPlainText = "\033[0;0m";
    final static String setBoldText = "\033[0;1m";

    public void chooseCommandAction() throws IOException {
        switch (parser.getCommandName()){
            case "mkdir":
                mkdir();
                break;
            case "ls":
                ls();
                break;
            case "touch":
                touch();
                break;
            case "cp":
                cp();
                break;
        }
    }

    public void ls() {
        if (parser.getCommandOptions().get(0).equals("-r")) {
            File file = new File(System.getProperty("user.dir"));
            String[] paths = file.list();
            Arrays.sort(paths, Collections.reverseOrder());
            for (String a : paths) {
                System.out.println(a);
            }
        }
    }

    public void touch() {
        File file = new File(parser.getArgs().get(0));

        try {
            file.createNewFile();
        }
        catch (IOException e) {
            output = setBoldText + "touch" + setPlainText +
                    ": can't create file " + parser.getArgs().get(0)
                    + setBoldText +
                    ": File already exist at this location" + setPlainText;
            display();
        }
    }

    public void mkdir(){
        for(String arg : parser.getArgs()){

            File file = new File(arg);

            if(!file.mkdirs()){
                output = setBoldText + "mkdir" + setPlainText +
                        ": can't create directory " + arg
                        + setBoldText +
                        ": No such file or directory" + setPlainText;
                display();
            }
        }
    }
    public void cp() throws IOException {
        File file1 = new File(parser.getArgs().get(0));
        File file2 = new File(parser.getArgs().get(1));

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
             in = new FileInputStream(file1);
             out = new FileOutputStream(file2);

            int line;
            while((line = in.read()) != -1){
                    out.write(line);
            }
        }
        catch (IOException e) {
            output = setBoldText + "mkdir" + setPlainText +
                    ": can't copy file "
                    + setBoldText +
                    ": No such files exists" + setPlainText;
            display();
        }
        finally {
            if(in != null) {
                in.close();
            }

            if(out != null){
                out.close();
            }
        }


    }
    public static void display(){
        System.out.println(output);
    }

    public static void run() throws IOException {
        Scanner s = new Scanner(System.in);
        while (true) {
            String input = s.nextLine();
            input = input.strip();
            if (!input.isEmpty()) {
                parser.parse(input);
            }
            Terminal terminal = new Terminal();
            terminal.chooseCommandAction();
            break;
        }
    }

    public static void main(String[] args) throws IOException {
        run();
//        File file = new File("\"H:\\ME71\\3rd Year\\1st Term\\OS\\Lab_1_virtual_os_linux\\aa\"");
//        System.out.println(file.isAbsolute());
//        String ex = "hello\"";
//        System.out.println(ex.endsWith("\"\""));
//        String ex = "H:\\ME71\\3rd Year\\1st Term\\OS\\Lab_1_virtual_os_linux\\aa" +
//                "C:\\ME71\\3rd Year\\1st Term\\OS\\Lab_1_virtual_os_linux\\aa";
//        String del = "[H:C:]+";
//        String[] a = ex.split(del);
//        for(String b : a){
//            System.out.println(b);
//        }

    }

//mkdir "H:\ME71\3rd Year\1st Term\OS\Lab_1_virtual_os_linux\aa" "H:\ME71\3rd Year\1st Term"
}
