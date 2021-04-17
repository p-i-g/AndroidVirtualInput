package com.example.androidvirtualinput.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.androidvirtualinput.R;
import com.example.androidvirtualinput.network.NetworkManager;

import java.io.IOException;
import java.net.UnknownHostException;

//for connecting to the server
public class ConnectFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        EditText editTextIp = view.findViewById(R.id.editTextIp);
        EditText editTextPort = view.findViewById(R.id.editTextPort);
        //get from shared preferences
        SharedPreferences preferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editTextIp.setText(preferences.getString("IP", "192.168."));
        editTextPort.setText(preferences.getString("Port", "3939"));
        //the only reason why this class exists
        view.findViewById(R.id.connectButton).setOnClickListener(view1 -> {
            int port = Integer.parseInt(editTextPort.getText().toString());//throws number format exception, checks for port
            String ip = editTextIp.getText().toString();
            //save settings
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("IP", ip);
            editor.putString("Port", editTextPort.getText().toString());
            editor.apply();
            //initialize network
            new Thread(() -> {
                try{
                    NetworkManager networkManager = new NetworkManager(port, ip);//throws unknown host, io exception
                    //this is bad
                    ((MainActivity) requireActivity()).setNetworkManager(networkManager);
                    requireActivity().runOnUiThread(() -> Navigation.findNavController(view).navigate(R.id.action_connectFragment_to_canvasFragment));
                }catch (NumberFormatException numberFormatException){
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Invalid Port Number", Toast.LENGTH_LONG).show());
                    numberFormatException.printStackTrace();
                }catch (UnknownHostException unknownHostException){
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Invalid IP Address", Toast.LENGTH_LONG).show());
                    unknownHostException.printStackTrace();
                }catch (IOException ioException){
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Failed to Connect to Server", Toast.LENGTH_LONG).show());
                    ioException.printStackTrace();
                }
            }).start();
        });
        return view;
    }
}