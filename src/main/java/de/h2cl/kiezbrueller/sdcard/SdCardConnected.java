package de.h2cl.kiezbrueller.sdcard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import de.h2cl.kiezbrueller.beans.BruellerMp3;
import de.h2cl.kiezbrueller.utils.Utils;

/**
 * Created by martin.junker on 21.12.15.
 */
public class SdCardConnected {

    private static final Logger LOG = LoggerFactory.getLogger(SdCardConnected.class);

    private final File sdcardRoot;

    /**
     * Constructor
     *
     * @param sdcardRoot
     */
    public SdCardConnected(final File sdcardRoot) {
        this.sdcardRoot = sdcardRoot;
    }

    /**
     * @return songs
     */
    public Map<Integer, BruellerMp3> songs() {
        Map<Integer, BruellerMp3> songs = new HashMap<>();

        File[] files = sdcardRoot.listFiles(pathname -> pathname.isFile()
                && (pathname.getName().endsWith(".mp3")
                        || pathname.getName().endsWith(".MP3")));
        for (File file : files) {
            try {
                Integer pos = Integer.parseInt(file.getName().substring(0, 3));
                songs.put(pos, new BruellerMp3(file));
            } catch (NumberFormatException ignored) {
                LOG.debug("caught NumberFormatException:", ignored.getMessage());
            } catch (InvalidDataException | IOException | UnsupportedTagException e) {
                LOG.warn("can't open mp3 file " + file.getName(), e);
            }
        }
        LOG.info("found {} songs", songs.size());
        return songs;
    }

    public String name() {
        return sdcardRoot.getName();
    }

    public long getFreeSpace() {
        return sdcardRoot.getFreeSpace();
    }

    public long getUsableSpace() {
        return sdcardRoot.getUsableSpace();
    }

    public long getTotalSpace() {
        return sdcardRoot.getTotalSpace();
    }

    public String niceSpaceString() {
        return sdcardRoot.getName() + ": " +
                Utils.humanReadableByteCount(getFreeSpace())
                + " of "
                + Utils.humanReadableByteCount(getTotalSpace());
    }

    public void copyToPosition(BruellerMp3 bruellerMp3, int i) {
        // delete the file on pos i
        if (songs().containsKey(i)) {
            songs().get(i).delete();
            LOG.info("delete song on pos " + i);
        }
        try {
            bruellerMp3.copyTo(new File(sdcardRoot, Utils.calcSongName(i)));
        } catch (IOException e) {
            LOG.warn("can't move file " + bruellerMp3.title(), e);
        }

    }

}
