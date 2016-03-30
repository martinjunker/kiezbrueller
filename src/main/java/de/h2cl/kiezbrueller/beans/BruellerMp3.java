package de.h2cl.kiezbrueller.beans;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * BruellerMp3 is a successful linked mp3 file. Its readable for Mp3File and filename suffix is .mp3
 * <p>
 * Created by martin.junker on 28.12.15.
 */
public class BruellerMp3 {

    private static final Logger LOG = LoggerFactory.getLogger(BruellerMp3.class);

    private Mp3File mp3File;

    private File source;

    /**
     * Constructor
     *
     * @param source
     */
    public BruellerMp3(File source) throws IOException, UnsupportedTagException, InvalidDataException {
        this.source = source;
        this.mp3File = new Mp3File(source);
    }

    public String title() {
        return mp3File.getId3v1Tag().getArtist() + " - " + mp3File.getId3v1Tag().getTitle();
    }

    public Image image() {
        return new Image(new ByteArrayInputStream(mp3File.getId3v2Tag().getAlbumImage()));
    }

    public Image imageSmall() {
        return new Image(new ByteArrayInputStream(mp3File.getId3v2Tag().getAlbumImage()), 60.0, 60.0, true, true);
    }


    public boolean delete() {
        return source.delete();
    }

    @Override
    public String toString() {
        return title();
    }

    public void copyTo(File target) throws IOException {
        Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        source = target;
        try {
            this.mp3File = new Mp3File(source);
        } catch (UnsupportedTagException | InvalidDataException e) {
            LOG.error("can't recreate mp3File after moving", e);
        }
    }


}
