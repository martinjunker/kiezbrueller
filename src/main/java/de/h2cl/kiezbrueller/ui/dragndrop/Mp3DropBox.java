package de.h2cl.kiezbrueller.ui.dragndrop;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import de.h2cl.kiezbrueller.beans.BruellerMp3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Mp3DropBox is a digital representation of a player button.
 * It has a postion and maybe an mp3 file is located at that position.
 */
public class Mp3DropBox extends VerticalLayout {

    private final ProgressBar progress;

    private final BruellerMp3 bruellerMp3;

    private final Integer position;

    /**
     * Constructor
     * 
     * @param bruellerMp3
     */
    public Mp3DropBox(final BruellerMp3 bruellerMp3, final Integer position) {
        this.bruellerMp3 = bruellerMp3;
        this.position = position;

        // headline
        addComponent(new Label("Button " + position));

        // drop area
        CssLayout dropPane = new CssLayout();
        dropPane.setWidth("200px");
        dropPane.setHeight("200px");
        dropPane.addStyleName("image-drop-pane");

        // add label
        dropPane.addComponent(new Label(bruellerMp3.title()));

        final StreamResource.StreamSource streamSource = (StreamResource.StreamSource) () -> {
            if (bruellerMp3.imageSmall() != null) {
                return bruellerMp3.imageSmall();
            }
            return null;
        };
        final StreamResource resource = new StreamResource(streamSource,
                bruellerMp3.getSource().getName());
        dropPane.addComponent(new Image(null, resource));

        Mp3DragAndDropHandler dropBox = new Mp3DragAndDropHandler(dropPane);
        dropBox.setSizeUndefined();

        Panel panel = new Panel(dropBox);
        panel.setSizeUndefined();
        panel.addStyleName("no-vertical-drag-hints");
        panel.addStyleName("no-horizontal-drag-hints");
        addComponent(panel);

        // ProgressBar
        progress = new ProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);
        addComponent(progress);
    }

    private class Mp3DragAndDropHandler extends DragAndDropWrapper implements DropHandler {

        private static final long FILE_SIZE_LIMIT = 50L * 1024L * 1024L; // 50MB

        Mp3DragAndDropHandler(Component root) {
            super(root);
            setDropHandler(this);
            setDragStartMode(DragStartMode.HTML5);
            setHTML5DataFlavor("text/plain", bruellerMp3.getSource().getAbsolutePath());
        }

        @Override
        public void drop(DragAndDropEvent dropEvent) {

            // expecting this to be an html5 drag
            WrapperTransferable tr = (WrapperTransferable) dropEvent
                    .getTransferable();
            Html5File[] files = tr.getFiles();
            if (files != null) {
                for (final Html5File html5File : files) {
                    final String fileName = html5File.getFileName();

                    if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
                        Notification.show("File rejected. Max 2MB files are accepted by Sampler");
                    } else {

                        final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                        StreamVariable streamVariable = new StreamVariable() {

                            public OutputStream getOutputStream() {
                                return bas;
                            }

                            public boolean listenProgress() {
                                return false;
                            }

                            public void onProgress(StreamVariable.StreamingProgressEvent event) {
                            }

                            public void streamingStarted(StreamingStartEvent event) {
                            }

                            public void streamingFinished(StreamingEndEvent event) {
                                progress.setVisible(false);
                                showFile(fileName, html5File.getType(), bas);
                            }

                            public void streamingFailed(StreamingErrorEvent event) {
                                progress.setVisible(false);
                            }

                            public boolean isInterrupted() {
                                return false;
                            }
                        };
                        html5File.setStreamVariable(streamVariable);
                        progress.setVisible(true);
                    }
                }

            } else {
                String text = tr.getText();
                if (text != null) {
                    showText(text);
                }
            }
        }

        private void showText(String text) {
            showComponent(new Label(text), "Wrapped text content");
        }

        private void showFile(final String name, final String type, final ByteArrayOutputStream bas) {
            // resource for serving the file contents
            final StreamResource.StreamSource streamSource = (StreamResource.StreamSource) () -> {
                if (bas != null) {
                    final byte[] byteArray = bas.toByteArray();
                    return new ByteArrayInputStream(byteArray);
                }
                return null;
            };
            final StreamResource resource = new StreamResource(streamSource,
                    name);

            // show the file contents - images only for now
            final Embedded embedded = new Embedded(name, resource);
            showComponent(embedded, name);
        }

        private void showComponent(final Component c, final String name) {
            final VerticalLayout layout = new VerticalLayout();
            layout.setSizeUndefined();
            layout.setMargin(true);
            final Window w = new Window(name, layout);
            w.addStyleName("dropdisplaywindow");
            w.setSizeUndefined();
            w.setResizable(false);
            c.setSizeUndefined();
            layout.addComponent(c);
            UI.getCurrent().addWindow(w);

        }

        @Override
        public AcceptCriterion getAcceptCriterion() {
            return AcceptAll.get();
        }
    }
}
