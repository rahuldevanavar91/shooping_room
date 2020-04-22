package com.android.shopping.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.android.shopping.R;
import com.android.shopping.model.ProductItem;

import java.util.Random;

public class NotificationUtils {

    public static void showNotificationMessage(Context context, ProductItem notificationItem) {
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.product_id), notificationItem.getProductId());
        PendingIntent resultIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.navigation_graph)
                .setDestination(R.id.productDetailFragment)
                .setArguments(args)
                .createPendingIntent();
        int notificationId = new Random().nextInt(100);

        String id = "com.android.shopping";

        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context, id);

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker(Html.fromHtml(notificationItem.getName()))
                .setAutoCancel(true)
                .setContentTitle(Html.fromHtml(notificationItem.getName()))
                .setContentIntent(resultIntent)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(Html.fromHtml(notificationItem.getName()))
                        .bigText(Html.fromHtml(notificationItem.getDesc())))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, "Promotion", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("You will get promotional messages");
            channel.setShowBadge(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            builder.setChannelId(id)
                    .setDefaults(Notification.DEFAULT_ALL);
        }
        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(notificationId, notification);
        }
    }

}
