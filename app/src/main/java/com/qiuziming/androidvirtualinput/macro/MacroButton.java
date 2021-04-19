package com.qiuziming.androidvirtualinput.macro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentActivity;

import com.qiuziming.androidvirtualinput.R;
import com.qiuziming.androidvirtualinput.ui.MainActivity;

import java.util.ArrayList;

public class MacroButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener, EditButtonDialog.DialogClickListener {
    private MacroAction buttonAction;
    private String[] keyMapping;

    private String buttonId;

    public MacroButton(@NonNull Context context) {
        super(context);
        initialize();
    }

    public MacroButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MacroButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void initialize(){
        //get the mapping array
        keyMapping = getResources().getStringArray(R.array.keys);
        //make the button round
        setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.button_background));
        setOnClickListener(this);
        //show dialog
        setOnLongClickListener(v -> {
            if(getContext() instanceof FragmentActivity) {
                EditButtonDialog dialog = new EditButtonDialog();
                //set the initial values
                dialog.name = getText();
                ArrayList<Integer> keyCodes = buttonAction.getKeys();
                ArrayList<String> keys = new ArrayList<>();
                for(int i : keyCodes){
                    keys.add(keyMapping[i]);
                }
                dialog.initialKeys = keys;
                dialog.setListener(this);
                dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "edit button settings");
            }
            return true;
        });
        //make it look decent
        setSingleLine(true);
        setTextColor(getResources().getColor(R.color.white, getContext().getTheme()));
    }
    //the only reason why this class exists
    @Override
    public void onClick(View v) {
        ((MainActivity) getContext()).getNetworkManager().printAction(buttonAction);
    }
    //the dialog listeners
    @Override
    public void onPositiveClick(EditButtonDialog dialog) {
        ArrayList<Integer> keys = new ArrayList<>();
        String name;

        name = dialog.nameEditText.getText().toString();
        setText(name);
        //getting the keys
        String key;
        for(int i = 0; i < dialog.keyInputs.size(); i++){
            key = dialog.keyInputs.get(i).getText().toString();
            //iterating through the mapping
            for(int j = 0; j < keyMapping.length; j++){
                if(keyMapping[j].equals(key) && !key.equals("null")){
                    keys.add(j);
                }
            }
        }

        buttonAction.changeAction(keys, name);
        //store setting
        SharedPreferences.Editor editor = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString(buttonId, buttonAction.toString());
        editor.apply();

        //set the fragment to fullscreen
        ((View) getParent()).setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    //this does nothing
    @Override
    public void onNegativeClick(EditButtonDialog dialog){

    }

    //set the button id in activity
    public void setButtonId(String buttonId){
        this.buttonId = buttonId;
        String actionString = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).getString(buttonId, "Undo:17,90");
        buttonAction = new MacroAction(actionString);
        setText(buttonAction.getName());
    }
}
