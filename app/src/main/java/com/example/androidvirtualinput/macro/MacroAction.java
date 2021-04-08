package com.example.androidvirtualinput.macro;

import com.example.androidvirtualinput.network.InputAction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MacroAction extends InputAction {
    private final ArrayList<Integer> keys;
    private final ArrayList<Double> durations;
    private final ArrayList<Double> delays;
    private final ArrayList<Integer> startModes; //0 -> with previous 1 -> after previous

    private final String name;

    public MacroAction(ArrayList<Integer> keys, ArrayList<Double> durations, ArrayList<Double> delays, ArrayList<Integer> startModes, String name) {
        this.keys = keys;
        this.durations = durations;
        this.delays = delays;
        this.startModes = startModes;
        this.name = name;
    }
    //NAME:KEY|DURATION|DELAY|STARTMODE,KEY|DURATION|DELAY|STARTMODE,...
    public MacroAction(String actionString){
        keys = new ArrayList<>();
        durations = new ArrayList<>();
        delays = new ArrayList<>();
        startModes = new ArrayList<>();

        String[] splitForName = actionString.split(":");
        name = splitForName[0];

        String[] splitForKeys = splitForName[1].split(",");

        String[] splitForEachKey;
        for (String splitForKey : splitForKeys) {
            splitForEachKey = splitForKey.split("\\|");
            keys.add(Integer.parseInt(splitForEachKey[0]));
            durations.add(Double.parseDouble(splitForEachKey[1]));
            delays.add(Double.parseDouble(splitForEachKey[2]));
            startModes.add(Integer.parseInt(splitForEachKey[3]));
        }
    }
    //for sending over the socket
    //MACRO:KEY|DURATION|DELAY|STARTMODE,KEY|DURATION|DELAY|STARTMODE,...
    @Override
    public String getActionString() {
        StringBuilder result = new StringBuilder("MACRO:");

        for (int i = 0; i < keys.size(); i++){
            result.append(keys.get(i)).append("|").append(durations.get(i)).append("|").append(delays.get(i)).append("|").append(startModes.get(i));
            if(i != keys.size()){
                result.append(",");
            }
        }

        return result.toString();
    }
    //for saving locally
    @NotNull
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder(name).append(":");

        for (int i = 0; i < keys.size(); i++){
            result.append(keys.get(i)).append("|").append(durations.get(i)).append("|").append(delays.get(i)).append("|").append(startModes.get(i));
            if(i != keys.size()){
                result.append(",");
            }
        }

        return result.toString();
    }
}
