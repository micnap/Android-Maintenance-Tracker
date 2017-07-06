package com.mickeywilliamson.mickey.maintenance2;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class DeleteDialogFragment extends DialogFragment {

    private int taskId;

    @Override
    public @NonNull Dialog onCreateDialog(final @NonNull Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        taskId = bundle.getInt("id");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_confirm_message)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    DatabaseHandler db = new DatabaseHandler(getActivity());
                    MaintenanceTask task = db.getMaintenanceTask(taskId);
                    db.deleteMaintenanceTask(task);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), R.string.task_deleted, Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
