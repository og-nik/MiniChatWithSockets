package MiniChatWithSockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ChatClient {

    BufferedReader reader;
    PrintWriter writer;
    String clientName = null;
    Socket sock;


    public void go(){

        setUpNetworkng();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String line = keyboard.readLine();
                String output = clientName + ": " + line;
                writer.println(output);
                writer.flush();
                if(line.equalsIgnoreCase("exit"))
                    sock.close();
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void setUpNetworkng(){

        try {
            sock = new Socket("localhost", 2007);

            InputStreamReader in = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(in);

            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("Networking is established");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public class IncomingReader implements Runnable{

        public void run(){

            String mes;

            try {
                while ((mes = reader.readLine()) != null) {
                    System.out.println(mes);
                }
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }

    public void giveName(String name) {
        clientName = name;
    }

    public static void main(String[] args) {

        ChatClient client = new ChatClient();
        if(args.length != 1) {
            System.out.println("Please enter your name (login)!");
            return;
        }
        client.giveName(args[0]);

        client.go();
    }
}

