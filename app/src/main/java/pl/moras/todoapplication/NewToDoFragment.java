package pl.moras.todoapplication;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import pl.moras.model.ToDoEvent;


import pl.moras.viewmodel.ToDoViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Calendar;

public class NewToDoFragment extends DialogFragment {
    private TextInputEditText dateText, timeText;
    private LocalDate localDate = LocalDate.now();
    private LocalTime localTime = LocalTime.now();
    private InterstitialAd interstitialAd;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View view = factory.inflate(R.layout.new_to_do_fragment, null);
        interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        builder.setView(view);
        TextInputEditText descriptionText = view.findViewById(R.id.newtodotextinput);
        dateText = view.findViewById(R.id.newtododateinput);
        timeText = view.findViewById(R.id.newtodotimeinput);
        dateText.setOnClickListener(dateClickListener);
        timeText.setOnClickListener(timeClickListener);
        builder.setTitle(getString(R.string.new_todo_dialog_title))
                .setPositiveButton(getString(R.string.new_todo_dialog_confirm), (dialog, id) -> {
                    String message = descriptionText.getText().toString();
                    LocalDateTime endDateTime = LocalDateTime.of(localDate, localTime);
                    if(endDateTime.isBefore(LocalDateTime.now()))
                        Toast.makeText(getContext(), getString(R.string.new_todo_bad_time), Toast.LENGTH_SHORT).show();
                    else if (message.isEmpty())
                        Toast.makeText(getContext(), getString(R.string.new_todo_bad_text), Toast.LENGTH_LONG).show();
                    else {
                        ToDoEvent toDoEvent = new ToDoEvent();
                        toDoEvent.setOpis(message);
                        toDoEvent.setDatastart(LocalDateTime.now());
                        toDoEvent.setDatakoniec(endDateTime);
                        new ViewModelProvider(requireActivity()).get(ToDoViewModel.class).insert(toDoEvent);
                        interstitialAd.show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
                });
        return builder.create();
    }


    private View.OnClickListener dateClickListener = (v)->{
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
            datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
            localDate = LocalDate.of(i, i1+1, i2);
            dateText.setText(localDate.toString());
        }, localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    };

    private View.OnClickListener timeClickListener = (v)->{
        new TimePickerDialog(getContext(), (timePicker, i, i1) -> {
            localTime = LocalTime.of(i, i1);
            timeText.setText(localTime.toString());
        }, localTime.getHour(), localTime.getMinute(), true).show();
    };


}
