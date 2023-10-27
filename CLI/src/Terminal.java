import java.io.File;
import java.util.*;

public class Terminal {
    private static Parser parser = new Parser();
    private static String output;

    final static String setPlainText = "\033[0;0m";
    final static String setBoldText = "\033[0;1m";

    public void chooseCommandAction() {
        switch (parser.getCommandName()){
            case "mkdir":
                mkdir();
                break;
            case "ls":
                ls();
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

    public static void display(){
        System.out.println(output);
    }

    public static void run(){
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

    public static void main(String[] args) {
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
