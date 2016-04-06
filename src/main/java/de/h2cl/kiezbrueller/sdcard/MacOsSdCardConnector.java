package de.h2cl.kiezbrueller.sdcard;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import de.h2cl.kiezbrueller.beans.SdCardConnected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by martin.junker on 27.12.15.
 */
@Component
public class MacOsSdCardConnector {

    private static final Logger LOG = LoggerFactory.getLogger(MacOsSdCardConnector.class);

    private static final String BRUELLER = ".brueller";

    /**
     * Checks all volumes for hidden file and returns SdCardConnected if found.
     * 
     * @return optionalSdCard
     */
    @Timed
    public Optional<SdCardConnected> lookForBrueller() {
        return volumes().stream()
                .filter(this::checkIsBrueller)
                .map(SdCardConnected::new)
                .findFirst();
    }

    /**
     *
     * @param sdcard
     * @return true if creating brueller file was successful
     */
    public boolean makeItBrueller(final File sdcard) {
        try {
            return new File(sdcard.getAbsolutePath() + "/" + BRUELLER).createNewFile();
        } catch (IOException e) {
            LOG.debug("can't create brueller file in {}", sdcard.getName(), e);
            return false;
        }
    }

    private List<File> volumes() {
        LOG.info("looking for sdcards ...");
        return Lists.newArrayList(
                new File("/Volumes").listFiles(File::isDirectory));
    }

    private boolean checkIsBrueller(final File sdcard) {
        boolean isBrueller = sdcard.listFiles((dir, name) -> name.equals(BRUELLER)).length > 0;
        LOG.debug("check isBrueller was {} for sdcard {}", isBrueller, sdcard.getName());
        return isBrueller;
    }

    private void setupSdCard() {

        /*
         * Alert alert = new Alert(Alert.AlertType.INFORMATION);
         * alert.setTitle("KiezBrüller");
         * alert.setHeaderText("Setup SD-Card");
         * alert.setContentText("No kiezbrüller configured SD-Card was found. To initialize a new card " +
         * "please remove your SD card if already connected and press OK.");
         * // alert.setOnCloseRequest(event -> System.exit(0));
         * alert.showAndWait();
         * 
         * List<File> volumesBefore = volumes();
         * 
         * Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
         * alert2.setTitle("KiezBrüller");
         * alert2.setHeaderText("Setup SD-Card");
         * alert2.setContentText("Insert your SD-Card now. Wait 5 seconds and press OK.");
         * // alert2.setOnCloseRequest(event -> System.exit(0));
         * alert2.showAndWait();
         * 
         * List<File> volumesAfter = volumes();
         * volumesAfter.removeAll(volumesBefore);
         * 
         * if (volumesAfter.size() == 1) {
         * LOG.info("ready for setup volume {} ", volumesAfter.get(0));
         * makeItBrueller(volumesAfter.get(0));
         * } else {
         * setupSdCard();
         * }
         */
    }
}
