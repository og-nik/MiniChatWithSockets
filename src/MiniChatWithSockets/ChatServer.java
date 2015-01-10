package MiniChatWithSockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;


public class ChatServer {

    ArrayList clientStreams;
    int clientId = 0;

    public class ClientHandler implements Runnable{

        Socket s;
        BufferedReader reader;

        public ClientHandler(Socket clientSocket){
            try {
                s = clientSocket;
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                reader = new BufferedReader(in);
            }catch (IOException e){
                System.out.println(e);
            }
        }

        public void run(){

            String mes;

            try {
                while ((mes = reader.readLine()) != null) {
                    System.out.println(mes);
                    tellEveryone(mes);
                }
            }catch (IOException e){
                System.out.println(e);
            }
        }
    }

    public void tellEveryone(String mes){

        Iterator it = clientStreams.iterator();
        while(it.hasNext()){
            try{
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(mes);
                writer.flush();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void go(){
        clientStreams = new ArrayList();

        try {
            ServerSocket serv = new ServerSocket(2007);
            System.out.println("Waiting for a client...");
            System.out.println();
            while(true){
                Socket clientSocket = serv.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                clientId++;
                System.out.println("Got a Client " + clientId);
            }


        }catch (IOException e){
            System.out.println(e);
        }
    }


    public static void main(String[] args) {

        new ChatServer().go();
    }
}
