package de.h2cl.kiezbrueller.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import de.h2cl.kiezbrueller.beans.BruellerMp3;
import de.h2cl.kiezbrueller.beans.ButtonLayout;
import de.h2cl.kiezbrueller.beans.SdCardConnected;
import de.h2cl.kiezbrueller.sdcard.SdCardConnector;
import de.h2cl.kiezbrueller.ui.dragndrop.Mp3DropBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

/**
 * Created by martin.junker on 09.03.16.
 */
@SpringView(name = SinglePlayerView.VIEW_NAME)
public class SinglePlayerView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "singlePlayer";

    @Autowired
    private SdCardConnector sdCardConnector;

    private GridLayout gridLayout;

    @PostConstruct
    void init() {
        setMargin(true);
        setSpacing(true);

        // Create a grid layout
        gridLayout = new GridLayout();
        gridLayout.setStyleName("dotted");
        gridLayout.setSizeFull();
        gridLayout.setHeight("");

        addComponent(gridLayout);

        generateMatrixGrid(ButtonLayout.COL3ROW2);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // TODO VAADIN Meetup: filter events?
        generateMatrixGrid(ButtonLayout.COL3ROW2);
    }

    private void generateMatrixGrid(ButtonLayout buttonLayout) {
        gridLayout.removeAllComponents();
        gridLayout.setRows(buttonLayout.getRows());
        gridLayout.setColumns(buttonLayout.getCols());

        // load sdcard
        final Optional<SdCardConnected> sdCardConnected = sdCardConnector.lookForBrueller();

        // without sdcard we don't need to render
        if (!sdCardConnected.isPresent()) {
            return;
        }

        Map<Integer, BruellerMp3> songs = sdCardConnected.get().songs();

        Integer position;

        for (int row = 0; row < gridLayout.getRows(); row++) {
            for (int col = 0; col < gridLayout.getColumns(); col++) {

                position = ButtonLayout.COL3ROW2.coordinatesToPos(col, row);
                gridLayout.addComponent(new Mp3DropBox(songs.get(position), position));
                gridLayout.setRowExpandRatio(row, 0.0f);
                gridLayout.setColumnExpandRatio(col, 0.0f);
            }
        }
    }

    public SdCardConnector getSdCardConnector() {
        return sdCardConnector;
    }
}