package de.h2cl.kiezbrueller.sdcard;

import de.h2cl.kiezbrueller.beans.SdCardConnected;

import java.util.Optional;

/**
 * Created by martin.junker on 09.04.16.
 */
public interface SdCardConnector {

    Optional<SdCardConnected> lookForBrueller();

}
