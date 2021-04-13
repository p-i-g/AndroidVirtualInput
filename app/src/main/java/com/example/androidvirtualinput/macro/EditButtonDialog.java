package com.example.androidvirtualinput.macro;

import androidx.fragment.app.DialogFragment;
//used for editing button name and keys
public class EditButtonDialog extends DialogFragment {
    public interface DialogClickListener{
        void onPositiveClick();
        void onNegativeClick();
    }
}
