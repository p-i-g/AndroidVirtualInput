package com.qiuziming.androidvirtualinput.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentActivity;

import com.qiuziming.androidvirtualinput.R;
import com.qiuziming.androidvirtualinput.macro.MacroAction;
import com.qiuziming.androidvirtualinput.ui.MainActivity;

import java.util.ArrayList;

//I probably shouldn't extend constraint layout
//Basically constraint layout but with on touch and on hover overridden
//also a bunch of gestures
public class CanvasView extends ConstraintLayout implements EditCanvasDialog.DialogClickListener {
    private boolean palmRejection = true;
    //for gestures
    private GestureDetectorCompat gestureDetector;
    //gesture actions
    private MacroAction flingAction;
    private MacroAction doubleTapAction;
    private MacroAction singleTapAction;

    private String[] keyMapping;

    public CanvasView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CanvasView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }
    //literally faux constructor
    private void initialize(){
        CanvasGestureListener listener = new CanvasGestureListener(this);
        gestureDetector = new GestureDetectorCompat(getContext(), listener);

        gestureDetector.setOnDoubleTapListener(listener);

        //settings stuff
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        flingAction = new MacroAction(preferences.getString("Fling", "Undo:17,90"));
        doubleTapAction = new MacroAction(preferences.getString("Double Tap", "Undo:17,90"));
        singleTapAction = new MacroAction(preferences.getString("Single Tap", "Undo:17,90"));

        keyMapping = getResources().getStringArray(R.array.keys);
    }
    //helper method for generating dialog
    private void setInitialValues(MacroAction macroAction, ArrayList<String> initialKeys){
            //set the initial values
            ArrayList<Integer> keyCodes = macroAction.getKeys();
            initialKeys.clear();
            for(int i : keyCodes){
                initialKeys.add(keyMapping[i]);
            }
    }
    //for touch corresponds to InContact in the windows thing
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if(palmRejection) {
            if (!(motionEvent.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS)) {
                if (this.gestureDetector.onTouchEvent(motionEvent)) {
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }
        }
        double posX = motionEvent.getX() / getWidth();
        double posY = motionEvent.getY() / getHeight();
        double pressure = motionEvent.getPressure();
        ((MainActivity) getContext()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.DOWN, pressure));//this cast is bad
        return true;
    }
    //for hover, corresponds to InContact not set in the windows thing
    @Override
    public boolean onHoverEvent(MotionEvent motionEvent){
        float posX = motionEvent.getX() / getWidth();
        float posY = motionEvent.getY() / getHeight();
        double pressure = motionEvent.getPressure();
        ((MainActivity) getContext()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.HOVER, pressure));//this cast is bad
        return true;
    }

    @Override
    public void onPositiveClick(EditCanvasDialog dialog) {
        //change macro for each gesture
        changeMacro(dialog.singleTapInputs, singleTapAction, "Single Tap");
        changeMacro(dialog.doubleTapInputs, doubleTapAction, "Double Tap");
        changeMacro(dialog.flingInputs, flingAction, "Fling");
        //change palm rejection
        palmRejection = dialog.button.isChecked();

        //set the fragment to fullscreen
        ((View) getParent()).setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    //helper method for changing macro
    public void changeMacro(ArrayList<EditText> keyInputs, MacroAction macroAction, String macroId){
        ArrayList<Integer> keys = new ArrayList<>();
        //getting the keys
        String key;
        for(int i = 0; i < keyInputs.size(); i++){
            key = keyInputs.get(i).getText().toString();
            //iterating through the mapping
            for(int j = 0; j < keyMapping.length; j++){
                if(keyMapping[j].equals(key) && !key.equals("null")){
                    keys.add(j);
                }
            }
        }

        macroAction.changeAction(keys, "CanvasMacro");
        //store setting
        SharedPreferences.Editor editor = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString(macroId, macroAction.toString());
        editor.apply();
    }

    @Override
    public void onNegativeClick(EditCanvasDialog dialog) {
        //this does nothing
    }

    //listener for gestures
    class CanvasGestureListener extends GestureDetector.SimpleOnGestureListener{
        private final CanvasView canvasView;

        public CanvasGestureListener(CanvasView canvasView) {
            this.canvasView = canvasView;
        }

        @Override
        public boolean onDown(MotionEvent event){
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            ((MainActivity) getContext()).getNetworkManager().printAction(flingAction);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ((MainActivity) getContext()).getNetworkManager().printAction(doubleTapAction);
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            ((MainActivity) getContext()).getNetworkManager().printAction(singleTapAction);
            return true;
        }
        //show dialog
        @Override
        public void onLongPress(MotionEvent e) {
            if(getContext() instanceof FragmentActivity) {
                EditCanvasDialog dialog = new EditCanvasDialog();
                //set the initial values
                dialog.initialSingleTap = new ArrayList<>();
                dialog.initialDoubleTap = new ArrayList<>();
                dialog.initialFling = new ArrayList<>();
                setInitialValues(singleTapAction, dialog.initialSingleTap);
                setInitialValues(doubleTapAction, dialog.initialDoubleTap);
                setInitialValues(flingAction, dialog.initialFling);
                dialog.palmRejection = palmRejection;

                dialog.listener = canvasView;
                dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "edit canvas settings");
            }
        }
    }
}
