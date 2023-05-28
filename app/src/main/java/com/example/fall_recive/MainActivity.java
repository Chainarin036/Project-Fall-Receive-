package com.example.fall_recive;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView alert;
//    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alert = findViewById(R.id.textView);



        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "Alert");
        DatabaseReference myRef2 = database.getReference( "status");
        new Thread(new Runnable() {
            @Override
            public void run() {
  //              if (!myRef2.equals("0")) {
                    // do something if myRef2 is not equal to "0"
                myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // อ่านข้อมูลจาก Firebase Realtime Database และดำเนินการต่อตามต้องการ

                            String message = dataSnapshot.getValue(String.class);
                            // แสดงผลข้อความบนหน้าจอ
                            if(message.equals("1") ){
                                addNotification("ผู้สูงอายุหกล้ม กำลังต้องการความช่วยเหลือ");
                            }
                            if(message.equals("2") ){
                                addNotification("ผู้สูงอายุหกล้มอาจจะ 'หมดสติ !' ต้องการความช่วยเหลือโดยด่วน");
                            }
                            if(message.equals("0") ){
                                alert.setText(message);
                            }
//                            if(message.equals("1")){
//                                addNotification("ผู้สูงอายุหกล้ม กำลังต้องการความช่วยเหลือ");
//                            }
//                            if(message.equals("2")){
//                                addNotification("ผู้สูงอายุหกล้มอาจจะ 'หมดสติ !' ต้องการความช่วยเหลือโดยด่วน");
//                            }

                            alert.setText(message);
//                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // หากเกิดข้อผิดพลาดในการเชื่อมต่อ Firebase Realtime Database
                            Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                        }
                    });
                }

     //       }
        }).start();

    }

    private void addNotification(String e) {
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("verseurl");
        bigText.setBigContentTitle(e);
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
