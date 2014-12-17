package douai.mines.isic.moodplayer;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Gildéric on 10/12/2014.
 */
class MP3Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}