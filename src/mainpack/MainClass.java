package mainpack;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainClass {
    public static void main(String[] args) throws IOException {
       // args = new String[]{"Lem.txt"};
        if(args.length == 0) {
            System.out.println("Specify the file");
            return;
        }

        Counter counter = new Counter();
        for (String arg : args) {
            try {
                counter.processFile(arg);
            } catch (IOException ex) {
                System.out.println("Error reading file " + arg + ":");
                System.out.println(ex.getMessage());
                System.out.println("Finishing.");
                return;
            }
        }

        try {
            counter.saveReports();
        } catch (FileNotFoundException ex) {
            System.out.println("Error saving reports:");
            System.out.println(ex.getMessage());
            System.out.println("Finishing.");
            return;
        }

        System.out.println("Done.");
    }
}
