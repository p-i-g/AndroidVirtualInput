package com.example.androidvirtualinput.macro;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentActivity;

import com.example.androidvirtualinput.R;
import com.example.androidvirtualinput.ui.MainActivity;

import java.util.ArrayList;

public class MacroButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener, EditButtonDialog.DialogClickListener {
    private MacroAction buttonAction;
    private String[] keyMapping;

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
        //make the button round
        setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.button_background));
        setOnClickListener(this);
        //show dialog
        setOnLongClickListener(v -> {
            if(getContext() instanceof FragmentActivity) {
                EditButtonDialog dialog = new EditButtonDialog();
                dialog.setListener(this);
                dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "edit settings");
            }
            return true;
        });
        //get the mapping array
        keyMapping = getResources().getStringArray(R.array.keys);
        setSingleLine(true);
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
            key = dialog.keyInputs.get(i).toString();
            //iterating through the mapping
            for(int j = 0; j < keyMapping.length; j++){
                if(keyMapping[j].equals(key) && !key.equals("null")){
                    keys.add(j);
                }
            }
        }

        buttonAction.changeAction(keys, name);
        //todo file stuff
    }
    //this does nothing
    @Override
    public void onNegativeClick(EditButtonDialog dialog){

    }
}
