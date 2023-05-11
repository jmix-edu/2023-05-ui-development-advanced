package com.company.jmixpm.screen.sandbox;

import com.vaadin.ui.AbstractOrderedLayout;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.JavaScriptComponent;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.risto.stepper.IntStepper;

@Route("sandbox")
@UiController("SandboxScreen")
@UiDescriptor("sandbox-screen.xml")
public class SandboxScreen extends Screen {
    @Autowired
    private JavaScriptComponent quill;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInit(InitEvent event) {
        IntStepper stepper = new IntStepper("Stepper");
        stepper.setMinValue(0);
        stepper.setMaxValue(20);

        getWindow().unwrap(AbstractOrderedLayout.class).addComponent(stepper, 0);

        QuillState state = new QuillState();
        state.theme = "snow";
        state.placeholder = "Compose an epic...";

        quill.setState(state);

        quill.addFunction("valueChanged", javaScriptCallbackEvent -> {
            String value = javaScriptCallbackEvent.getArguments().getString(0);
            notifications.create()
                    .withCaption(value)
                    .withType(Notifications.NotificationType.TRAY)
                    .show();
        });
    }

    @Subscribe("clearQuillBtn")
    public void onClearQuillBtnClick(Button.ClickEvent event) {
        quill.callFunction("deleteText");
    }

    class QuillState {
        public String theme;
        public String placeholder;
    }
}