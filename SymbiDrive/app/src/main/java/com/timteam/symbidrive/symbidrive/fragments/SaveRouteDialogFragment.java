package com.timteam.symbidrive.symbidrive.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.timteam.symbidrive.symbidrive.R;

/**
 * Created by andreea on 31.03.2015.
 */
public class SaveRouteDialogFragment extends AlertDialog {
    private EditText mEditText;

    protected SaveRouteDialogFragment(Context context) {
        super(context);
    }
}
