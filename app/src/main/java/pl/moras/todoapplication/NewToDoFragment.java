package pl.moras.todoapplication;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import pl.moras.model.ToDoEvent;


import pl.moras.viewmodel.ToDoViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewToDoFragment extends DialogFragment implements TimePicker.OnClickListener {
    private TimePicker timePicker;
    private EditText editText;
    private String message;
    private LocalDate date;
    private LocalTime time;
    private LocalDateTime endDateTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        date = LocalDate.now();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View textEntryView = factory.inflate(R.layout.new_to_do_fragment, null);
        builder.setView(textEntryView);
        editText = textEntryView.findViewById(R.id.taskTextView);
        timePicker = textEntryView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        CalendarView calendarView = textEntryView.findViewById(R.id.calendarView2);
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());

        builder.setTitle("Nowe zadanie")
                .setPositiveButton("Stwórz", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.O)
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {
                        message = editText.getText().toString();
                        time = LocalTime.of(timePicker.getHour(), timePicker.getMinute());
                        endDateTime = LocalDateTime.of(date, time);
                        if(endDateTime.isBefore(LocalDateTime.now()))
                            Toast.makeText(getContext(), "Nie możesz ustawić czasu który już minął.", Toast.LENGTH_SHORT).show();
                        else if (message.isEmpty())
                            Toast.makeText(getContext(), "Nie podałeś żadnego zadania", Toast.LENGTH_LONG).show();
                        else {
                            ToDoEvent toDoEvent = new ToDoEvent();
                            toDoEvent.setOpis(message);
                            toDoEvent.setDatastart(LocalDateTime.now());
                            toDoEvent.setDatakoniec(endDateTime);
                            ViewModelProviders.of(requireActivity()).get(ToDoViewModel.class).insert(toDoEvent);
                        }
                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int rok, int mies, int dzien) {
                mies++; //miesiace z 0-11 na 1-12
                date = LocalDate.of(rok,mies,dzien);

            }
        });
        return builder.create();
    }


    @Override
    public void onClick(View view) {

    }

}
