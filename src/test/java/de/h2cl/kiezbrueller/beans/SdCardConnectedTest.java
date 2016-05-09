package de.h2cl.kiezbrueller.beans;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by martin.junker on 07.05.16.
 */
public class SdCardConnectedTest {

    private static final Logger LOG = LoggerFactory.getLogger(SdCardConnectedTest.class);

    private static Path TMP_DIR;

    private SdCardConnected sdCardConnected;

    /**
     * creates tmp dir with shutdown hook for deletes
     * 
     * @throws IOException
     */
    @BeforeClass
    public static void init() throws IOException {
        TMP_DIR = Files.createTempDirectory("de.h2cl.bruellertest");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Files.delete(TMP_DIR);
                    LOG.info("removed test directory:  {}", TMP_DIR.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        LOG.info("created test directory: {}", TMP_DIR.toString());
    }

    @Before
    public void setup() throws IOException {
        // alle dateien löschen
        Arrays.stream(TMP_DIR.toFile().listFiles()).forEach(File::delete);
        sdCardConnected = new SdCardConnected(TMP_DIR.toFile());
    }

    @Test
    public void testEmptyCard() {
        assertEquals(sdCardConnected.songs().size(), 0);
    }

}