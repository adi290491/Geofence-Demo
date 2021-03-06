package com.example.android.geofencedemo;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    /***
     * Handles the Broadcast message sent when the Geofence Transition is triggered
     * Careful here though, this is running on the main thread so make sure you start an AsyncTask for
     * anything that takes longer than say 10 second to run
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive called");
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        int transitionType = event.getGeofenceTransition();

       /* if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
            setRingerMode(context, AudioManager.RINGER_MODE_SILENT);
        }else if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
            setRingerMode(context, AudioManager.RINGER_MODE_NORMAL);
        }else {
            Log.d(TAG, String.format("Unknown transition : %d", transitionType));
            return;
        }*/
        sendNotification(context, transitionType);

    }

    private void setRingerMode(Context context, int mode){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT < 24 ||
                Build.VERSION.SDK_INT >= 24 && !nm.isNotificationPolicyAccessGranted()){
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(mode);
        }
    }

    private void sendNotification(Context context, int transitionType){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER){
            builder.setSmallIcon(R.drawable.ic_volume_off_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_volume_off_white_24dp))
                    .setContentTitle(context.getString(R.string.silent_mode_activated));
        }else if(transitionType == Geofence.GEOFENCE_TRANSITION_EXIT){
            builder.setSmallIcon(R.drawable.ic_volume_up_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_volume_up_white_24dp))
                    .setContentTitle(context.getString(R.string.back_to_normal));
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, builder.build());
    }
}
