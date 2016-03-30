package de.h2cl.kiezbrueller.ui.components;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import java.io.Serializable;

/**
 * Created by martin.junker on 09.03.16.
 */
@SpringComponent
@UIScope
public class Greeter implements Serializable {

    public String sayHello() {
        return "Hello from bean " + toString();
    }
}