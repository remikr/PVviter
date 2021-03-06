package com.dreamteam.pvviter.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.dreamteam.pvviter.activities.TimeStampActivity;
import com.dreamteam.pvviter.utils.DateManipulation;

import java.util.Calendar;
import java.util.Hashtable;

/**
 * Created by FlorianDoublet on 28/10/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimeStampActivity timeStampActivity = null;

    public void setTimeStampActivity(TimeStampActivity timeStampActivity) {
        this.timeStampActivity = timeStampActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Calendar c = Calendar.getInstance();
        if (timeStampActivity.isChangeTimeMode()) {
            c.setTimeInMillis(timeStampActivity.getParkingTimeStore());
        }

        c.add(Calendar.HOUR_OF_DAY, timeStampActivity.getNumberPickerHours().getValue());
        c.add(Calendar.MINUTE, timeStampActivity.getNumberPickerMinutes().getValue() * TimeStampActivity.getMinuteStepSize());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //set a call with the new parameters for hours and minutes
        Calendar cal = Calendar.getInstance();
        if (timeStampActivity.isChangeTimeMode()) {
            cal.setTimeInMillis(timeStampActivity.getParkingTimeStore());
        }
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        //create a new calendar at the current time
        Calendar calNow = Calendar.getInstance();
        if (timeStampActivity.isChangeTimeMode()) {
            calNow.setTimeInMillis(timeStampActivity.getParkingTimeStore());
        }


        //calculus the difference and set the result into a hashtable
        Hashtable<Integer, Integer> res = DateManipulation.diffBetweenTwoDate(cal, calNow);

        int elapsedHours = res.get(DateManipulation.ELAPSED_HOURS);
        int elapsedMinutes = res.get(DateManipulation.ELAPSED_MINUTES);

        if ((elapsedMinutes % 5) != 0) {
            this.timeStampActivity.customMinutesNumberPicker(1);
        }

        if (elapsedMinutes < 0 && elapsedHours <= 0) {
            elapsedHours -= 1;
        }

        //Log.d("elapsed",elapsedHours+":"+elapsedMinutes );
        this.timeStampActivity.getNumberPickerHours().setValue(elapsedHours);
        this.timeStampActivity.getNumberPickerMinutes().setValue(elapsedMinutes / TimeStampActivity.getMinuteStepSize());
        this.timeStampActivity.updateTimeStampEndValue();
    }

}