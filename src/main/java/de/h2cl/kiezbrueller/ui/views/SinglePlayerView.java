package de.h2cl.kiezbrueller.ui.views;

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.h2cl.kiezbrueller.beans.BruellerMp3;
import de.h2cl.kiezbrueller.beans.SdCardConnected;
import de.h2cl.kiezbrueller.sdcard.MacOsSdCardConnector;
import de.h2cl.kiezbrueller.utils.Utils;

/**
 * Created by martin.junker on 09.03.16.
 */
@SpringView(name = SinglePlayerView.VIEW_NAME)
public class SinglePlayerView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "singlePlayer";

    @Autowired
    private MacOsSdCardConnector sdCardConnector;

    private GridLayout sample;

    @PostConstruct
    void init() {
        setMargin(true);
        setSpacing(true);

        // Create a grid layout
        sample = new GridLayout();
        sample.addStyleName("outlined");
        sample.setSizeFull();
        addComponent(sample);

        generateMatrixGrid(3, 2);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }

    private void generateMatrixGrid(final int rows, final int columns) {
        sample.removeAllComponents();
        sample.setRows(rows);
        sample.setColumns(columns);

        // load sdcard
        final Optional<SdCardConnected> sdCardConnected = sdCardConnector.lookForBrueller();

        // without sdcard we don't need to render
        if (!sdCardConnected.isPresent()) {
            return;
        }

        Map<Integer, BruellerMp3> songs = sdCardConnected.get().songs();

        for (int row = 0; row < sample.getRows(); row++) {
            for (int col = 0; col < sample.getColumns(); col++) {
                sample.addComponent(new Label(songs.get(Utils.coordinatesToPos(row, col)).title() + col + "/" + row), col, row);
                sample.setRowExpandRatio(row, 0.0f);
                sample.setColumnExpandRatio(col, 0.0f);
            }
        }
    }

    // D&D
    // final Label infoLabel = new Label();

    /*
     * final VerticalLayout dropPane = new VerticalLayout(viewContainer);
     * dropPane.setComponentAlignment(viewContainer, Alignment.MIDDLE_CENTER);
     * dropPane.setWidth(280.0f, Unit.PIXELS);
     * dropPane.setHeight(200.0f, Unit.PIXELS);
     * dropPane.addStyleName("drop-area");
     *
     * final ImageDropBox dropBox = new ImageDropBox(dropPane);
     * dropBox.setSizeUndefined();
     *
     * ProgressBar progress = new ProgressBar();
     * progress.setIndeterminate(true);
     * progress.setVisible(false);
     * dropPane.addComponent(progress);
     */

}