package com.company.jmixpm.component;

import io.jmix.ui.xml.layout.loader.AbstractFieldLoader;

public class ColorFieldLoader extends AbstractFieldLoader<ColoField> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(ColoField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();
    }
}
