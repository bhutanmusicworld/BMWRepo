package com.bmusic.bmusicworld.servermethod;

/**
 * Created by TECHMIT on 2/21/2018.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.downloads.CheckForSDCard;
import com.bmusic.bmusicworld.utility.Utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadService2 extends IntentService {

    public DownloadService2() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;
    private String songname,filename;
    private File apkStorage = null;
    private File file;


    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
         songname = intent.getStringExtra("name");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.download2)
                .setContentTitle(songname)
                .setContentText("Downloading Song....")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initDownload(urlToDownload);

    }

    private void initDownload(String songurl){

        try {
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            URL url = new URL(songurl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            if (new CheckForSDCard().isSDCardPresent()) {
                apkStorage = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + ".Bmusic");
            } else
            {

            }
            //If File is not present create directory
            if (!apkStorage.exists()) {
                apkStorage.mkdir();
                Log.e("success", "Directory Created.");
            }
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.

             filename= songname+".mp3";
            // you can download to any type of file ex:.jpeg (image) ,.txt(text file),.mp3 (audio file)

            Log.i("Local filename:",""+filename);
            file = new File(apkStorage,filename);
            if(file.createNewFile())
            {
                file.createNewFile();
            }
            OutputStream output = new FileOutputStream(file);
            long startTime = System.currentTimeMillis();
            int timeCount = 1;
            while ((count = input.read(data)) != -1) {

                total += count;
                totalFileSize = (int) (fileLength / (Math.pow(1024, 2)));
                double current = Math.round(total / (Math.pow(1024, 2)));

                int progress = (int) ((total * 100) / fileLength);

                long currentTime = System.currentTimeMillis() - startTime;

                Download download = new Download();
                download.setTotalFileSize(totalFileSize);

                if (currentTime > 1000 * timeCount) {

                    download.setCurrentFileSize((int) current);
                    download.setProgress(progress);
                    sendNotification(download);
                    timeCount++;
                }

                output.write(data, 0, count);
            }
            onDownloadComplete();
            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void sendNotification(Download download){

       // sendIntent(download);
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText("Downloading file ..."+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

//    private void sendIntent(Download download){
//
//        Intent intent = new Intent(MainActivity.MESSAGE_PROGRESS);
//        intent.putExtra("download",download);
//        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
//    }

    private void onDownloadComplete(){

        Download download = new Download();
        download.setProgress(100);
      //  sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());
        Utility.count++;
        Myclass.startService(Utility.count);


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}
