package de.h2cl.kiezbrueller.ui;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import de.h2cl.kiezbrueller.sdcard.SdCardConnected;
import de.h2cl.kiezbrueller.sdcard.SdCardConnector;
import de.h2cl.kiezbrueller.ui.views.ViewScopedView;

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
    private SdCardConnector sdCardConnector;

    private Optional<SdCardConnected> sdCardConnected;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("View Scoped View",
                ViewScopedView.VIEW_NAME));
        root.addComponent(navigationBar);

        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);

        // clicklistener for
        Button button = new Button("look for sdcard",
                event -> lookForBrueller());
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        navigationBar.addComponent(button);
    }

    private Button createNavigationButton(final String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        // If you didn't choose Java 8 when creating the project, convert this to an anonymous listener class
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    private void lookForBrueller() {
        sdCardConnected = sdCardConnector.lookForBrueller();
        if (sdCardConnected.isPresent()) {
            Notification.show("Connected!");
        } else {
            Notification.show("No sdcard found!");
        }
    }
}
