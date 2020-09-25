package com.suek.ex61servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {

    MediaPlayer mp;

    //StartService()를 통해 서비스가 시작되면 자동으로 실행되는 메소드
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Oreo 버전부터 운영체제에서 백그라운드작업 및 방송수신작업에 제한을 두고 있음 (배터리문제때문- 오래쓰려고 제한을 둠)
        //만약, 이 서비스객체가 백그라운드에서 계속 동작하고 싶다면
        //foregroundService()로 실행해야만 함.
        //즉, 사용자가 서비스가 가동중임을 인지할 수 있는 장치를 마련했다고 보면됨.

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){   //오레오버전 이상일경우

            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel= new NotificationChannel("ch1", "뮤직서비스", NotificationManager.IMPORTANCE_LOW);  //오레오버전 이상일경우 channel 도
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder= new NotificationCompat.Builder(this, "ch1");

            //알림설정들
            builder.setSmallIcon(R.drawable.ic_stat_name);
            builder.setContentTitle("Music Service");
            builder.setContentText("뮤직서비스가 실행중입니다.");

            //알림창을 클릭했을때 뮤직제어화면(MainActivity)으로 전환되도록
            Intent i= new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            PendingIntent pendingIntent= PendingIntent.getActivity(this, 10, i, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            builder.setAutoCancel(true);  //원래 클릭하면 알림이 자동취소 되어야만 하지만, 포어그라운드에서는 적용되지 않음.

            Notification notification= builder.build();

            //이 서비스를 포어그라운드에서 돌아가도록 명령내리기..
            //포어그라운드 서비스를 하려면 Notification 을 보이도록 해야함.
            startForeground(1, notification);
        }else {

        }


        //미디어플레이어 객체 생성 및 시작!  ----> null 값의 mp 를 새로 만들어줌
        if(mp==null){
            mp= MediaPlayer.create(this, R.raw.s_goodjob);     //서비스도 context 를 상속(this)
            mp.setLooping(true);
            mp.setVolume(1.0f, 1.0f);
        }
        mp.start();

        //메모리 문제로 서비스를 강제로 kill 을 시켰을때..
        //메모리 문제가 해결되는 자동으로 서비스를 다시 실행해달라는 표현!
        // => return START_STICKY
        return START_STICKY;
    }



    //StopService()를 통해 서비스가 종료되면 자동으로 실행되는 메소드
    @Override
    public void onDestroy() {

        if( mp!=null && mp.isPlaying() ){   //mp 가 없거나 플레이중이면 멈춰라
            mp.stop();      //mp 를 멈춰도 참조변수가 null 값이 아님 (값이 남아있음)
            mp.release();   //MediaPlayer 객체를 메모리에서 삭제하도록.
            mp=null;        //이때 mp 에 null 값을 넣어 비워준다.
        }

        super.onDestroy();
    }






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
