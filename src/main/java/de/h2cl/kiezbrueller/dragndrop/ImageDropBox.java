package de.h2cl.kiezbrueller.dragndrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;

/**
 * Created by martin.junker on 31.03.16.
 */

@StyleSheet("dragndropexample.css")
public class ImageDropBox extends DragAndDropWrapper implements
        DropHandler {
    private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB

    public ImageDropBox(final Component root) {
        super(root);
        setDropHandler(this);
    }

    @Override
    public void drop(final DragAndDropEvent dropEvent) {

        // expecting this to be an html5 drag
        final WrapperTransferable tr = (WrapperTransferable) dropEvent
                .getTransferable();
        final Html5File[] files = tr.getFiles();
        if (files != null) {
            for (final Html5File html5File : files) {
                final String fileName = html5File.getFileName();

                if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
                    Notification
                            .show("File rejected. Max 2Mb files are accepted by Sampler",
                                    Notification.Type.WARNING_MESSAGE);
                } else {

                    final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    final StreamVariable streamVariable = new StreamVariable() {

                        @Override
                        public OutputStream getOutputStream() {
                            return bas;
                        }

                        @Override
                        public boolean listenProgress() {
                            return false;
                        }

                        @Override
                        public void onProgress(
                                final StreamingProgressEvent event) {
                        }

                        @Override
                        public void streamingStarted(
                                final StreamingStartEvent event) {
                        }

                        @Override
                        public void streamingFinished(
                                final StreamingEndEvent event) {
                            //progress.setVisible(false);
                            showFile(fileName, html5File.getType(), bas);
                        }

                        @Override
                        public void streamingFailed(
                                final StreamingErrorEvent event) {
                            // progress.setVisible(false);
                        }

                        @Override
                        public boolean isInterrupted() {
                            return false;
                        }
                    };
                    html5File.setStreamVariable(streamVariable);
                    // progress.setVisible(true);
                }
            }

        } else {
            final String text = tr.getText();
            if (text != null) {
                showText(text);
            }
        }
    }

    private void showText(final String text) {
        showComponent(new Label(text), "Wrapped text content");
    }

    private void showFile(final String name, final String type,
            final ByteArrayOutputStream bas) {
        // resource for serving the file contents
        final StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                if (bas != null) {
                    final byte[] byteArray = bas.toByteArray();
                    return new ByteArrayInputStream(byteArray);
                }
                return null;
            }
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
