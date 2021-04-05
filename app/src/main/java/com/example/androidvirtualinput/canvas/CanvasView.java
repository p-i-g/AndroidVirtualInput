package com.example.androidvirtualinput.canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidvirtualinput.MainActivity;
import com.example.androidvirtualinput.R;

//I probably shouldn't extend constraint layout
//Basically constraint layout but with on touch and on hover overridden
public class CanvasView extends ConstraintLayout {
    private boolean palmRejection = true;
    private TextView centerText;
    //center text need not exist.
    //this is just for this program only/doing it this way is going to kill anyone who tries to use this later

    public CanvasView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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

    //for touch corresponds to InContact in the windows thing
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if(palmRejection) {
            if (!(motionEvent.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS)) {
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

    public void setPalmRejection(boolean palmRejection) {
        this.palmRejection = palmRejection;
    }
}
