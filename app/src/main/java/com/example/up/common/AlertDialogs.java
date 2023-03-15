package com.example.up.common;
import android.content.Context;
import android.content.DialogInterface;
import com.example.up.R;
import androidx.appcompat.app.AlertDialog;
public class AlertDialogs {
    public static void OpenAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Уведомление")
                .setMessage(message)
                .setIcon(R.drawable.icon)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create();
        builder.show();
    }
}
