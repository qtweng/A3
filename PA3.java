/**
 * Main class for assignment, deals with user interaction and command line
 * arguments
 * <p>
 * A discrete event simulation for a production line
 *
 * @author QiTao Weng
 */

public class PA3 {

    public static void main(String[] args) {
        if (args.length == 3) {
            // parse input
            int m = Integer.parseInt(args[0]);
            int n = Integer.parseInt(args[1]);
            int qMax = Integer.parseInt(args[2]);
            Factory factory = new Factory(m, n, qMax);
            factory.run();
            factory.printInfo();
        } else {
            System.out.println("Incorrect number of parameters.");
        }
    }
}