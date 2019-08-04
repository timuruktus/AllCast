package trelico.ru.allcast.screens.prepare;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import static trelico.ru.allcast.MyApp.D_TAG;

public class HintDialog extends DialogFragment{

    private String hint;
    private String positiveButton;
    private HintDialogListener listener;
    private String appUri;

    protected static HintDialog createDialog(String hint, String positiveButton,
                                             HintDialogListener listener, String appUri){
        HintDialog hintDialog = new HintDialog();
        hintDialog.hint = hint;
        hintDialog.positiveButton = positiveButton;
        hintDialog.listener = listener;
        hintDialog.appUri = appUri;
        return hintDialog;
    }

    private HintDialog(){ }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        if(hint == null || hint.isEmpty()){
            Log.i(D_TAG, "Dialog hint is empty");
            hint = "";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(hint);
        if(positiveButton != null && !positiveButton.isEmpty()){
            builder.setPositiveButton(positiveButton, (dialog, id) ->
                    listener.onPositiveButtonClicked(appUri));
            dismiss();
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
