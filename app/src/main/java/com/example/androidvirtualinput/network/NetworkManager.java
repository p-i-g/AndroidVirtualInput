package com.example.androidvirtualinput.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//for managing the socket and output streams
public class NetworkManager {
    private PrintWriter out; //for printing to socket output stream
    private Scanner in;
    private Socket socket;
    private static final int TIMEOUT = 5000;

    ExecutorService executor;

    //this should  be executed in a new thread
    public NetworkManager(int port, String ip) throws IOException {
        //initialized the executor
        executor = Executors.newFixedThreadPool(1);
        //initializes the socket along with the output stream
        InetSocketAddress serverAddress = new InetSocketAddress(ip, port);
        Socket socket = new Socket();
        socket.connect(serverAddress, TIMEOUT);
        socket.setSoTimeout(TIMEOUT);
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        in = new Scanner(socket.getInputStream());
        //handshake
        //I don't know the protocol for this.
        //This works I guess
        out.println("Android Input Device");
        out.flush();
        if(!in.nextLine().equals("Android Input Driver")) {
            in.close();
            out.close();
            socket.close();
            throw new IOException("Handshake failed");
        }
    }

    //closes the socket properly and shutdowns the executor
    public void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();


        executor.shutdown();
    }

    //prints action to socket
    public void printAction(InputAction action) {
        Runnable task = () -> {
            out.println(action.getActionString());
            out.flush();
        };

        executor.execute(task);
    }
}
