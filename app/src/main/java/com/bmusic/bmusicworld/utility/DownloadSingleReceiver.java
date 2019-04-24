package com.bmusic.bmusicworld.utility;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.bmusic.bmusicworld.servermethod.Myclass;

/**
 * Created by TECHMIT on 2/19/2018.
 */

public class DownloadSingleReceiver extends ResultReceiver {

    public DownloadSingleReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (resultCode == DownloadService.UPDATE_PROGRESS) {

            int progress = resultData.getInt("progress");
            DownloadService.build.setProgress(100, progress, false);
            DownloadService.mNotifyManager.notify(DownloadService.id, DownloadService.build.build());

//            mProgressDialog.setProgress(progress);
            Log.e("progress",""+progress);
            if (progress == 100) {

                DownloadService.build.setContentText("Download complete");
                DownloadService.build.setProgress(0, 0, false);
                DownloadService.mNotifyManager.notify(DownloadService.id, DownloadService.build.build());

//                mProgressDialog.dismiss();
            }
        }
    }
}