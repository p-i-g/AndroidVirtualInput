package com.example.androidvirtualinput.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;

import com.example.androidvirtualinput.macro.MacroAction;
import com.example.androidvirtualinput.ui.MainActivity;

//I probably shouldn't extend constraint layout
//Basically constraint layout but with on touch and on hover overridden
//also a bunch of gestures
public class CanvasView extends ConstraintLayout {
    private final boolean palmRejection = true;
    //for gestures
    private GestureDetectorCompat gestureDetector;
    //gesture actions
    private MacroAction flingAction;
    private MacroAction doubleTapAction;
    private MacroAction singleTapAction;

    public CanvasView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
//        TypedArray a = context.getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.CanvasView,
//                0, 0);
//        int mIdReference = a.getResourceId(R.styleable.CanvasView_centerText, 0);
//        System.out.println(mIdReference);
//        ViewGroup parent = this;
//        while ((centerText = parent.findViewById(mIdReference)) == null) {
//            parent = (ViewGroup) parent.getParent();
//        }
    }

    public CanvasView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
//        TypedArray a = context.getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.CanvasView,
//                0, 0);
//        int mIdReference = a.getResourceId(R.styleable.CanvasView_centerText, 0);
//        System.out.println(mIdReference);
//        ViewGroup parent = this;
//        while ((centerText = parent.findViewById(mIdReference)) == null) {
//            parent = (ViewGroup) parent.getParent();
//        }
    }
    //literally faux constructor
    private void initialize(){
        CanvasGestureListener listener = new CanvasGestureListener();
        gestureDetector = new GestureDetectorCompat(getContext(), listener);

        gestureDetector.setOnDoubleTapListener(listener);

        //settings stuff
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        flingAction = new MacroAction(preferences.getString("Fling", "Undo:17,90"));
        doubleTapAction = new MacroAction(preferences.getString("Double Tap", "Undo:17,90"));
        singleTapAction = new MacroAction(preferences.getString("Single Tap", "Undo:17,90"));
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
//        centerText.setText(getContext().getString(R.string.canvas_touch, posX, posY, pressure));
        ((MainActivity) getContext()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.DOWN, pressure));//this cast is bad
        return true;
    }
    //for hover, corresponds to InContact not set in the windows thing
    @Override
    public boolean onHoverEvent(MotionEvent motionEvent){
        float posX = motionEvent.getX() / getWidth();
        float posY = motionEvent.getY() / getHeight();
        double pressure = motionEvent.getPressure();
//        centerText.setText(getContext().getString(R.string.canvas_hover, posX, posY));
        ((MainActivity) getContext()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.HOVER, pressure));//this cast is bad
        return true;
    }

    //listener
    class CanvasGestureListener extends GestureDetector.SimpleOnGestureListener{
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
    }
}
