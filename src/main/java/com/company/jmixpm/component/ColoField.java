package com.company.jmixpm.component;

import io.jmix.core.common.event.Subscription;
import io.jmix.ui.component.*;
import io.jmix.ui.component.data.ValueSource;
import io.jmix.ui.component.validation.Validator;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;

@CompositeDescriptor("color-field-layout.xml")
public class ColoField extends CompositeComponent<CssLayout> implements Field<String>,
        CompositeWithHtmlCaption, CompositeWithHtmlDescription, CompositeWithIcon, CompositeWithContextHelp {

    public static final String NAME = "colorField";

    private TextField<String> valueField;
    private ColorPicker colorPicker;

    public ColoField() {
        addCreateListener(this::onCreate);
    }

    private void onCreate(CreateEvent createEvent) {
        valueField = getInnerComponent("colorField_valueField");
        colorPicker = getInnerComponent("colorField_colorPicker");
    }

    @Override
    public boolean isEditable() {
        return valueField.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        valueField.setEditable(editable);
        colorPicker.setEditable(editable);
    }

    @Override
    public void addValidator(Validator<? super String> validator) {
        valueField.addValidator(validator);
    }

    @Override
    public void removeValidator(Validator<String> validator) {
        valueField.removeValidator(validator);
    }

    @Override
    public Collection<Validator<String>> getValidators() {
        return valueField.getValidators();
    }

    @Nullable
    @Override
    public String getValue() {
        return valueField.getValue();
    }

    @Override
    public void setValue(@Nullable String value) {
        if (getValueSource() == null) {
            throw new IllegalStateException("No value source defined");
        }

        getValueSource().setValue(value);
    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<String>> listener) {
        return valueField.addValueChangeListener(listener);
    }

    @Override
    public boolean isRequired() {
        return valueField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        valueField.setRequired(true);
        getComposition().setRequiredIndicatorVisible(true);
    }

    @Nullable
    @Override
    public String getRequiredMessage() {
        return valueField.getRequiredMessage();
    }

    @Override
    public void setRequiredMessage(@Nullable String msg) {
        valueField.setRequiredMessage(msg);
    }

    @Override
    public boolean isValid() {
        return valueField.isValid();
    }

    @Override
    public void validate() throws ValidationException {
        valueField.validate();
    }

    @Override
    public void setValueSource(@Nullable ValueSource<String> valueSource) {
        valueField.setValueSource(valueSource);
        colorPicker.setValueSource(valueSource);
        getComposition().setRequiredIndicatorVisible(valueField.isRequired());
    }

    @Nullable
    @Override
    public ValueSource<String> getValueSource() {
        return valueField.getValueSource();
    }
}
