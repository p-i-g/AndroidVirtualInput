package com.example.androidvirtualinput.macro;

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
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.androidvirtualinput.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

//used for editing button name and keys
public class EditButtonDialog extends DialogFragment {
    DialogClickListener listener;
    ArrayList<EditText> keyInputs;
    TextView nameTextView;
    EditText nameEditText;
    LinearLayout linearLayout;
    //for interfacing with the macro button
    public interface DialogClickListener{
        void onPositiveClick(EditButtonDialog dialog);
        void onNegativeClick(EditButtonDialog dialog);
    }
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //for inflating the alert layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_layout, null);
        nameTextView = root.findViewById(R.id.textView);
        nameEditText = root.findViewById(R.id.editText);
        //other lines for keys will be added
        linearLayout = root.findViewById(R.id.linearLayout);
        builder.setTitle("Macro Settings").setView(root).setPositiveButton("Save", (dialog, which) -> listener.onPositiveClick(this)).setNegativeButton("Cancel", ((dialog, which) -> listener.onNegativeClick(this)));
        //add the rows for keys
        keyInputs = new ArrayList<>();
        addRow("Control", inflater, 1);
        return builder.create();
    }
    //helper method for adding rows
    private void addRow(String hint, LayoutInflater inflater, int count){
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

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && !changed && !s.equals("null")){
                    addRow("Z", inflater, ++count);
                    changed = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linearLayout.addView(row);
    }

    public void setListener(DialogClickListener listener) {
        this.listener = listener;
    }
}
