package com.example.androidvirtualinput.canvas;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidvirtualinput.MainActivity;
import com.example.androidvirtualinput.R;

public class CanvasFragment extends Fragment {
    private ConstraintLayout canvas;
    private TextView centerText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canvas, container, false);
        canvas = view.findViewById(R.id.canvasView);
        centerText = view.findViewById(R.id.canvasText);

        canvas.setOnTouchListener((view1, motionEvent) -> {//todo: fix warnings, out of view logic
            float posX = motionEvent.getX() / canvas.getWidth();
            float posY = motionEvent.getY() / canvas.getHeight();
            double pressure = motionEvent.getPressure();
            centerText.setText(" X: " + posX + " Y: " + posY + " Pressure: " + pressure + " Touch");
            ((MainActivity) getActivity()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.DOWN, pressure));
            return true;
        });
        canvas.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {
                float posX = motionEvent.getX() / canvas.getWidth();
                float posY = motionEvent.getY() / canvas.getHeight();
                double pressure = motionEvent.getPressure();
                centerText.setText("X: " + posX + "Y: " + posY + "Pressure: " + pressure + " Hover");
                ((MainActivity) getActivity()).getNetworkManager().printAction(new CanvasAction(posX, posY, CanvasAction.HOVER, pressure));
                return true;
            }
        });
        return view;
    }
}