package com.example.androidvirtualinput.canvas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import com.example.androidvirtualinput.R;

import java.util.ArrayList;
public class EditCanvasDialog extends DialogFragment {
    DialogClickListener listener;
    //button
    SwitchCompat button;
    //original values
    ArrayList<String> initialSingleTap;
    ArrayList<String> initialDoubleTap;
    ArrayList<String> initialFling;
    //new edit text fields
    ArrayList<EditText> singleTapInputs;
    ArrayList<EditText> doubleTapInputs;
    ArrayList<EditText> flingInputs;
    //linear layouts
    LinearLayout singleTapLinearLayout;
    LinearLayout doubleTapLinearLayout;
    LinearLayout flingLinearLayout;

    NestedScrollView scrollView;
    //initial palm rejection
    boolean palmRejection;
    //the same interface
    public interface DialogClickListener{
        void onPositiveClick(EditCanvasDialog dialog);
        void onNegativeClick(EditCanvasDialog dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        //for inflating the alert layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.canvas_settings_dialog, null);
        button = root.findViewById(R.id.switch1);
        //finding linear layouts
        singleTapLinearLayout = root.findViewById(R.id.singleTapLinearLayout);
        doubleTapLinearLayout = root.findViewById(R.id.doubleTapLinearLayout);
        flingLinearLayout = root.findViewById(R.id.flingLinearLayout);
        //create the builder
        builder.setTitle("Canvas Settings").setView(root).setPositiveButton("Save", (dialog, which) -> listener.onPositiveClick(this)).setNegativeButton("Cancel", ((dialog, which) -> listener.onNegativeClick(this)));
        //initialize ArrayLists
        singleTapInputs = new ArrayList<>();
        doubleTapInputs = new ArrayList<>();
        flingInputs = new ArrayList<>();
        //add initial keys
        addInitialKeys(initialSingleTap, inflater, singleTapLinearLayout, singleTapInputs);
        addInitialKeys(initialDoubleTap, inflater, doubleTapLinearLayout, doubleTapInputs);
        addInitialKeys(initialFling, inflater, flingLinearLayout, flingInputs);
        //listener for palm rejection
        scrollView = root.findViewById(R.id.scrollView);
        button.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                scrollView.setVisibility(View.VISIBLE);
            }else{
                scrollView.setVisibility(View.GONE);
            }
        });
        button.setChecked(palmRejection);
        return builder.create();
    }
    //helper method for adding initial keys
    private void addInitialKeys(ArrayList<String> initialKeys, LayoutInflater inflater, LinearLayout linearLayout, ArrayList<EditText> keyInputs){
        int count  = 1;
        for(; count <= initialKeys.size(); count ++) {
            @SuppressLint("InflateParams") View row = inflater.inflate(R.layout.dialog_row, null);
            TextView title = row.findViewById(R.id.textView);
            AutoCompleteTextView key = row.findViewById(R.id.editText);
            keyInputs.add(key);
            title.setText(String.format(getString(R.string.key), count));
            key.setText(initialKeys.get(count - 1));
            //for autocomplete
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.keys, android.R.layout.simple_dropdown_item_1line);
            key.setAdapter(adapter);

            linearLayout.addView(row);
        }
        addRow("Control", inflater, count, keyInputs, linearLayout);
    }
    //helper method for adding other rows
    private void addRow(String hint, LayoutInflater inflater, int count, ArrayList<EditText> keyInputs, LinearLayout linearLayout){
        @SuppressLint("InflateParams") View row = inflater.inflate(R.layout.dialog_row, null);
        TextView title = row.findViewById(R.id.textView);
        AutoCompleteTextView key = row.findViewById(R.id.editText);
        keyInputs.add(key);
        title.setText(String.format(getString(R.string.key), count));
        key.setHint(hint);
        //for autocomplete
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.keys, android.R.layout.simple_dropdown_item_1line);
        key.setAdapter(adapter);
        //add a new row
        key.addTextChangedListener(new TextWatcher() {
            private boolean changed = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressWarnings("UnusedAssignment")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && !changed && !s.equals("null")){
                    addRow("Z", inflater, count++, keyInputs, linearLayout);
                    changed = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linearLayout.addView(row);
    }
}
