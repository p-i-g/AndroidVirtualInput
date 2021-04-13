package com.example.androidvirtualinput.macro;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.androidvirtualinput.R;
import com.example.androidvirtualinput.ui.MainActivity;

public class MacroButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener {
    private MacroAction buttonAction;

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
        setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.button_background));
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getContext()).getNetworkManager().printAction(buttonAction);
    }
}
