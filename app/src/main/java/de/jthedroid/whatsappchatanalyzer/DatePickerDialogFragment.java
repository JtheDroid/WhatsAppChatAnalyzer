package de.jthedroid.whatsappchatanalyzer;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DateReceiver dateReceiver = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        return new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (dateReceiver == null) return;
        Calendar c = Calendar.getInstance();
        c.set(i, i1, i2);
        dateReceiver.onReceiveDate(c.getTime());
    }

    public void setDateReceiver(DateReceiver dateReceiver) {
        this.dateReceiver = dateReceiver;
    }
}