package com.example.MediaShare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MyAlertDialogFragment extends DialogFragment {
    public static MyAlertDialogFragment newInstance(String title,String msg,String pos,String neg) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg",msg);
        args.putString("pos",pos);
        args.putString("neg",neg);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title =getArguments().getString("title");
        String msg =getArguments().getString("msg");
        String pos =getArguments().getString("pos");
        String neg =getArguments().getString("neg");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(pos,  new DialogInterface.OnClickListener() {
            //User clicked on YES
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().moveTaskToBack(true);
                getActivity().finish();
            }
        });
        alertDialogBuilder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }
}
