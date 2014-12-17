package douai.mines.isic.moodplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Gildéric on 11/12/2014.
 */
public class MPService extends Service {
    private static final int NOTIFY_ID = R.layout.songlist;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<String> songs = new ArrayList<String>();
    private int currentPosition;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        notificationManager.cancel(NOTIFY_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public IBinder getBinder() {
        return mBinder;
    }

    private void playSong(String file) {
        try {

            //Notification notification = new Notification(
            //        R.drawable.playbackstart, file, null, file, null);
            Notification notification = new Notification() ;
            notificationManager.notify(NOTIFY_ID, notification);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer arg0) {
                    nextSong();
                }
            });

        } catch (IOException e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
    }

    private void nextSong() {
        // Check if last song or not
        if (++currentPosition >= songs.size()) {
            currentPosition = 0;
            notificationManager.cancel(NOTIFY_ID);
        } else {
            playSong(MoodPlayer.MEDIA_PATH + songs.get(currentPosition));
        }
    }

    private void prevSong() {
        if (mediaPlayer.getCurrentPosition() < 3000 && currentPosition >= 1) {
            playSong(MoodPlayer.MEDIA_PATH + songs.get(--currentPosition));
        } else {
            playSong(MoodPlayer.MEDIA_PATH + songs.get(currentPosition));
        }
    }

    private final MPSInterface.Stub mBinder = new MPSInterface.Stub() {

        public void playFile(int position) throws DeadObjectException {
            try {
                currentPosition = position;
                playSong(MoodPlayer.MEDIA_PATH + songs.get(position));

            } catch (IndexOutOfBoundsException e) {
                Log.e(getString(R.string.app_name), e.getMessage());
            }
        }

        public void addSongPlaylist(String song) throws DeadObjectException {
            songs.add(song);
        }

        public void clearPlaylist() throws DeadObjectException {
            songs.clear();
        }

        public void skipBack() throws DeadObjectException {
            prevSong();

        }

        public void skipForward() throws DeadObjectException {
            nextSong();
        }

        public void pause() throws DeadObjectException {
            //Notification notification = new Notification(
            //        R.drawable.playbackpause, null, null, null, null);
            Notification notification = new Notification();
            notificationManager.notify(NOTIFY_ID, notification);
            mediaPlayer.pause();
        }

        public void stop() throws DeadObjectException {
            notificationManager.cancel(NOTIFY_ID);
            mediaPlayer.stop();
        }

    };

}
