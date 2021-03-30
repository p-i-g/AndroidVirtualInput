package com.example.androidvirtualinput.network;

public abstract class InputAction {
    //string that can be printed to socket
    //format: ACTION:PARAM1,PARAM2,...
    public abstract String getActionString();
}
