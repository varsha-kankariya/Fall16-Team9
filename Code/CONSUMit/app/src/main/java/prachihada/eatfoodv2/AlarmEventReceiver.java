package prachihada.eatfoodv2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by laddu on 11/30/16.
 */

public class AlarmEventReceiver extends BroadcastReceiver {


    public static final int NOTIFICATION_ID = 12344;
    public static final int ACTIVITY_ID = 11111;
    public static String NOTIFICATION = "notification";
    private DBHandler dbHandler;

    @Override
    public void onReceive(Context context, Intent intent) {

//        System.out.println("On receiving the notifications!!");


        dbHandler = new DBHandler(context);


        Integer itemCount = dbHandler.getNextTwoDayNotificationCount();
//        System.out.println("**************************************************Item count wasted : " + itemCount);

        if (itemCount > 0) {
            Notification notification = getNotification(context, "Consume " + itemCount + " item(s) in next 2 days!");

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(NOTIFICATION_ID, notification);

        } else {
//            System.out.println("");
//            System.out.println("No food expiring in next 2 days to consume!!");
        }

    }


    private Notification getNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Inventory Status");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(PendingIntent.getActivity(context, ACTIVITY_ID,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        return builder.build();
    }
}
