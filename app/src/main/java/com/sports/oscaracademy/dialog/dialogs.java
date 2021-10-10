package com.sports.oscaracademy.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.viewModel.Pay_playViewModel;


public class dialogs {

    public void alertDialogLogin(ProgressDialog progressDialog , String msg){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissDialog(ProgressDialog progressDialog){
        progressDialog.dismiss();
    }

    public final void displayDialog(String str, Context context) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setMessage(str);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().getWindowStyle();
    }

    public final void bookingT_C(Context context, Pay_playViewModel model, View decorView, SpinKitView progress) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setPositiveButton(context.getString(R.string.Accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.payFees(decorView, progress);
            }
        });
        builder.setNegativeButton(context.getString(R.string.dismiss), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "-ve clicked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("TERMS AND CONDITIONS -");
        builder.setMessage("- A maximum of 5 members per booking per badminton court is admissible\n\n" +
                "- Non Marking Shoes compulsory for Badminton. Shoes must be worn after entering the facility.\n\n" +
                "- Barefoot play is strictly prohibited.\n\n" +
                "- Sports equipment not available on rent.");
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().getWindowStyle();
        alertDialog.setOnDismissListener(dialogInterface -> {
            progress.setVisibility(View.GONE);
        });
    }
}