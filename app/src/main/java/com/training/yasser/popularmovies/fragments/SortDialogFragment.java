package com.training.yasser.popularmovies.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.training.yasser.popularmovies.R;

/**
 * Created by yasser on 18/07/2016.
 */
public class SortDialogFragment extends DialogFragment {

    NoticeDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            mListener = (NoticeDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement NoticeDialogListener");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title_sort)
                .setSingleChoiceItems(R.array.sort_array, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                                mListener.onDialogPositiveClick(which);
                            }
                        }
                );

        return builder.create();
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(int item);
    }
}
