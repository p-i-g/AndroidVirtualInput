package com.example.androidvirtualinput.network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//for managing the socket and output streams
public class NetworkManager {
    private final PrintWriter out; //for printing to socket output stream

    ExecutorService executor;

    public NetworkManager(int port, String ip) throws IOException {
        //initializes the socket along with the output stream
        InetAddress serverAddress = InetAddress.getByName(ip);
        Socket socket = new Socket(serverAddress, port);
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        //todo: handshake
        //initialized the executor
        executor = Executors.newSingleThreadExecutor();
    }
    //closes the socket properly and shutdowns the executor
    public void closeConnection(){
        out.close();

        executor.shutdown();
    }
    //prints action to socket
    public void printAction(InputAction action){
        Runnable task = () -> {
            out.println(action.getActionString());
            out.flush();
        };

        executor.execute(task);
    }
}
