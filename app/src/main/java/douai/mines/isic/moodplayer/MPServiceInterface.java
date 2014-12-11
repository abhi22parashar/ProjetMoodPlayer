package douai.mines.isic.moodplayer;

/**
 * Created by Gildéric on 11/12/2014.
 */
public interface MPServiceInterface {
        void clearPlaylist();
        void addSongPlaylist(String song );
        void playFile(int position );

        void pause();
        void stop();
        void skipForward();
        void skipBack();
    }
