package com.example.bookwormadventuresdeluxe2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

// https://developer.android.com/guide/topics/ui/dialogs
public class ExitConfirmationDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // https://stackoverflow.com/questions/6014028/closing-application-with-exit-button
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.exit_confirmation_message)
                .setPositiveButton(R.string.confirm, (dialog, id) -> System.exit(0))
                .setNegativeButton(R.string.cancel, (dialog, id) ->
                {
                    return;
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
