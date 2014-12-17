// MPSInterface.aidl
package douai.mines.isic.moodplayer;

// Declare any non-default types here with import statements

interface MPSInterface {
	void clearPlaylist();
	void addSongPlaylist( in String song );
	void playFile( in int position );

	void pause();
	void stop();
	void skipForward();
	void skipBack();
}
