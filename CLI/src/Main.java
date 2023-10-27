public class Main {
    public static void main(String[] args) {
        Parser p = new Parser();
        String s = "ls >> file3";
        System.out.println(p.parse(s));
    }
}