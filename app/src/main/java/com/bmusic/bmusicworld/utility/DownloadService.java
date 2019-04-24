package com.bmusic.bmusicworld.utility;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.downloads.CheckForSDCard;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import es.dmoral.toasty.Toasty;

/**
 * Created by TECHMIT on 2/19/2018.
 */

public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    public static NotificationManager mNotifyManager;
    public static NotificationCompat.Builder build;
    public static int id = 1;
    File apkStorage = null;
    public DownloadService() {
        super("DownloadService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        String urlToDownload = intent.getStringExtra("url");
        String songname = intent.getStringExtra("name");
        mNotifyManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        build = new NotificationCompat.Builder(this);
        build.setContentTitle(songname)
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.download);
        build.setProgress(100, 0, false);
        mNotifyManager.notify(id, build.build());

        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        try {
            URL url = new URL(urlToDownload);
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
                // apkStorage=Environment.getExternalStoragePublicDirectory(
                // Environment.DIRECTORY_DCIM);
                Log.e("success", "Directory Created.");
            }
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.

            String filename= songname+".mp3";
            // you can download to any type of file ex:.jpeg (image) ,.txt(text file),.mp3 (audio file)
            File file;
            Log.i("Local filename:",""+filename);
            file = new File(apkStorage,filename);
            if(file.createNewFile())
            {
                file.createNewFile();
            }

            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(file);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                receiver.send(UPDATE_PROGRESS, resultData);
                fileOutput.write(data, 0, count);
            }

            fileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress" ,100);
        receiver.send(UPDATE_PROGRESS, resultData);
    }
}