package douai.mines.isic.moodplayer;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.IBinder;

/**
 * Created by Gildéric on 10/12/2014.
 */
public class MoodPlayer extends ListActivity {
    public static final String MEDIA_PATH = new String("/sdcard/");
    private List<String> songs = new ArrayList<String>();
    private MPSInterface mpInterface;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mpInterface = MPSInterface.Stub.asInterface((IBinder) service);
            updateSongList();
        }

        public void onServiceDisconnected(ComponentName className) {
            updateSongList();
            mpInterface = null;
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.songlist);
        this.bindService(new Intent(MoodPlayer.this, MPService.class), mConnection, Context.BIND_AUTO_CREATE);

    }

    public void updateSongList() {
        File home = new File(MEDIA_PATH);
        if (home.listFiles( new MP3Filter()).length > 0) {
            for (File file : home.listFiles( new MP3Filter())) {
                songs.add(file.getName());
            }

            ArrayAdapter<String> songList = new ArrayAdapter<String>(this,R.layout.song_item,songs);
            setListAdapter(songList);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            mpInterface.playFile(position);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), e.getMessage());
        }
    }
}