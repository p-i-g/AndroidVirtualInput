package com.example.androidvirtualinput.macro;

import com.example.androidvirtualinput.network.InputAction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MacroAction extends InputAction {
    private ArrayList<Integer> keys;

    private String name;

    public MacroAction(ArrayList<Integer> keys, String name) {
        this.keys = keys;
        this.name = name;
    }
    //NAME:KEY1,KEY2,KEY3...
    public MacroAction(String actionString){
        keys = new ArrayList<>();

        String[] splitForName = actionString.split(":");
        name = splitForName[0];

        String[] splitForKeys = splitForName[1].split(",");

        for (String splitForKey : splitForKeys) {
            keys.add(Integer.parseInt(splitForKey));
        }
    }
    //literally the same as the constructor
    public void changeAction(ArrayList<Integer> keys, String name) {
        this.keys = keys;
        this.name = name;
    }
    //for sending over the socket
    //MACRO:KEY1,KEY2,KEY3...
    @Override
    public String getActionString() {
        StringBuilder result = new StringBuilder("MACRO:");

        for (int i = 0; i < keys.size(); i++){
            result.append(keys.get(i));
            if(i < keys.size() - 1){
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
            result.append(keys.get(i));
            if(i < keys.size() - 1){
                result.append(",");
            }
        }

        return result.toString();
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }
}
