package pl.moras.notifier;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import pl.moras.model.AppDatabase;
import pl.moras.model.ToDoEvent;
import pl.moras.todoapplication.MainActivity;
import pl.moras.todoapplication.R;

import java.time.Duration;
import java.time.LocalDateTime;


public class NotifierWorker extends Worker {

    private static final int NOTIFICATION_ID = 101;
    private static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "Notification Channel";

    public NotifierWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ToDoEvent toDoEvent = AppDatabase.getDBInstance(getApplicationContext()).toDoEventDao().getFirst();
        while (toDoEvent.getDatakoniec().isBefore(LocalDateTime.now())){
            AppDatabase.getDBInstance(getApplicationContext()).toDoEventDao().delete(toDoEvent);
            toDoEvent = AppDatabase.getDBInstance(getApplicationContext()).toDoEventDao().getFirst();
        }
        enableChannel();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, notifier(toDoEvent));
        return Result.success();
    }

    private void enableChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private Notification notifier(ToDoEvent toDoEvent) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.options_button)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle(getApplicationContext().getString(R.string.notifier_reminder_title))
                .setContentText(getApplicationContext().getString(R.string.notfier_reminder_text, toDoEvent.getOpis(), convertDate(toDoEvent.getDatakoniec())))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return notificationBuilder.build();
    }

    private String convertDate(LocalDateTime localDateTime){
        Duration between = Duration.between(LocalDateTime.now(), localDateTime);
        if (between.toDays()>0)
            return getApplicationContext().getResources().getQuantityString(R.plurals.notifier_days, (int)between.toDays(), between.toDays());
        else
            return getApplicationContext().getResources().getQuantityString(R.plurals.notifier_hours, (int)between.toHours(), between.toHours());
    }
}