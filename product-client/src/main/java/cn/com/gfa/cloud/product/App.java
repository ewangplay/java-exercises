package cn.com.gfa.cloud.product;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
 
    public static void main(String[] args) throws Exception {
        ProductClient c = new ProductClient(HOST, PORT);

        // Read operand from the stdin.
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (;;) {
            System.out.println("Please input the operand (input 'bye' to quit):");
            String line = in.readLine();
            if (line == null) {
                System.out.println("quit...");
                break;
            }
            // If user typed the 'bye' command, close the connection and quit.
            if ("bye".equals(line.toLowerCase())) {
                // ch.closeFuture().sync();
                System.out.println("quit...");
                break;
            }

            int operand;
            try {
                operand = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                // e.printStackTrace();
                System.out.println("invalid operand, retry...");
                continue;
            }

            // Sends the operand to the server.
            int result = c.send(operand);
            System.out.println("Response result: " + result);
        }

        c.close();
    }
}
