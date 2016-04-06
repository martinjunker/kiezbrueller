package de.h2cl.kiezbrueller.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.h2cl.kiezbrueller.beans.SdCardConnected;
import de.h2cl.kiezbrueller.sdcard.MacOsSdCardConnector;
import de.h2cl.kiezbrueller.ui.views.DefaultView;
import de.h2cl.kiezbrueller.ui.views.SinglePlayerView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Created by martin.junker on 09.03.16.
 */
@Theme("valo")
@SpringUI
public class InfoUI extends UI {

    // we can use either constructor autowiring or field autowiring
    @Autowired
    private SpringViewProvider viewProvider;

    @Autowired
    private MacOsSdCardConnector sdCardConnector;

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setMargin(true);
        rootLayout.setSpacing(true);
        setContent(rootLayout);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        navigationBar.addComponent(createHomeButton());
        navigationBar.addComponent(createPlayerButton());
        navigationBar.addComponent(createRefreshButton());
        rootLayout.addComponent(navigationBar);

        // add view container
        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        rootLayout.addComponent(viewContainer);
        rootLayout.setExpandRatio(viewContainer, 1.0f);

        // add a footer

        rootLayout.addComponent(createFooter());

        // connect navigator and viewprovider
        final Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);


    }

    private Component createFooter() {
        
        final CssLayout footer = new CssLayout();
        footer.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        final Label sdCardInfo = new Label();
        final Optional<SdCardConnected> sdCardConnected = sdCardConnector.lookForBrueller();

        if (sdCardConnected.isPresent()) {
            sdCardInfo.setValue(sdCardConnected.get().niceSpaceString());
        } else {
            sdCardInfo.setValue("not connected to sdcard");
        }
        footer.addComponent(sdCardInfo);
        return footer;
    }

    private Button createPlayerButton() {
        final Button playerButton = new Button("");
        playerButton.addStyleName(ValoTheme.BUTTON_SMALL);
        playerButton.setIcon(FontAwesome.LIST);
        playerButton.addClickListener(event -> getUI().getNavigator().navigateTo(SinglePlayerView.VIEW_NAME));
        return playerButton;
    }

    private Button createRefreshButton() {
        final Button refreshButton = new Button("", event -> lookForBrueller());
        refreshButton.setIcon(FontAwesome.REFRESH);
        refreshButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return refreshButton;
    }

    private Button createHomeButton() {
        final Button homeButton = new Button("", event -> getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME));
        homeButton.setIcon(FontAwesome.HOME);
        homeButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return homeButton;
    }

    private void lookForBrueller() {
        if (sdCardConnector.lookForBrueller().isPresent()) {
            Notification.show("Connected!");
        } else {
            Notification.show("No sdcard found!");
        }
    }
}
