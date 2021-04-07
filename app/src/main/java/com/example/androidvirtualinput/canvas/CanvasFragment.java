package com.example.androidvirtualinput.canvas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.androidvirtualinput.R;

public class CanvasFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //        centerText = view.findViewById(R.id.canvasText);

//        canvas.setOnTouchListener((view1, motionEvent) -> {//todo: fix warnings, out of view logic
//            if(palmRejection){
//                if(motionEvent.getToolType(getId()) == MotionEvent.TOOL_TYPE_STYLUS){
//                    double posX = motionEvent.getX() / canvas.getWidth();
//                    double posY = motionEvent.getY() / canvas.getHeight();
//                    double pressure = motionEvent.getPressure();
//                    centerText.setText(getContext().getString(R.string.canvas_touch, posX, posY, pressure));
//                }
//            }
//                double posX = motionEvent.getX() / canvas.getWidth();
//                double posY = motionEvent.getY() / canvas.getHeight();
//                double pressure = motionEvent.getPressure();
//                centerText.setText(getContext().getString(R.string.canvas_touch, posX, posY, pressure));
//            ((MainActivity) getActivity()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.DOWN, pressure));
//            return true;
//        });
//        canvas.setOnHoverListener((view12, motionEvent) -> {
//            float posX = motionEvent.getX() / canvas.getWidth();
//            float posY = motionEvent.getY() / canvas.getHeight();
//            double pressure = motionEvent.getPressure();
//            centerText.setText(getContext().getString(R.string.canvas_hover, posX, posY));
//            ((MainActivity) getActivity()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.HOVER, pressure));
//            return true;
//        });
        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }
}