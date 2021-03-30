package com.example.androidvirtualinput;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.androidvirtualinput.network.NetworkManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //todo: get from ui
    public static final String IP = "192.168.1.240";
    public static final int PORT = 3939;

    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //todo: handle exception, this should be done in network manager
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    networkManager = new NetworkManager(PORT, IP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //this feels a bit workaroundish
    public NetworkManager getNetworkManager(){
        return networkManager;
    }
}