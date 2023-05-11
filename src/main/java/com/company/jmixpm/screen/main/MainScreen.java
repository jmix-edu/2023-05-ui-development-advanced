package com.company.jmixpm.screen.main;

import com.company.jmixpm.app.TaskChangedEvent;
import com.company.jmixpm.app.TaskService;
import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.Task;
import com.company.jmixpm.screen.task.TaskEdit;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.ScreenTools;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.component.mainwindow.Drawer;
import io.jmix.ui.component.mainwindow.SideMenu;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.model.CollectionChangeType;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@UiController("MainScreen")
@UiDescriptor("main-screen.xml")
@Route(path = "main", root = true)
public class MainScreen extends Screen implements Window.HasWorkArea {

    @Autowired
    private EntityComboBox<Project> projectSelector;
    @Autowired
    private TextField<String> nameSelector;
    @Autowired
    private DateField<LocalDateTime> dateSelector;

    @Autowired
    private CollectionContainer<Task> tasksDc;
    @Autowired
    private CollectionLoader<Task> tasksDl;

    @Autowired
    private ScreenTools screenTools;
    @Autowired
    private TaskService taskService;

    @Autowired
    private AppWorkArea workArea;
    @Autowired
    private Drawer drawer;
    @Autowired
    private SideMenu sideMenu;
    @Autowired
    private Button collapseDrawerButton;
    @Autowired
    private Notifications notifications;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Metadata metadata;

    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @Subscribe("collapseDrawerButton")
    private void onCollapseDrawerButtonClick(Button.ClickEvent event) {
        drawer.toggle();
        if (drawer.isCollapsed()) {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_RIGHT);
        } else {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_LEFT);
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        screenTools.openDefaultScreen(
                UiControllerUtils.getScreenContext(this).getScreens());

        screenTools.handleRedirect();

        updateTaskCount();
    }

    @Subscribe("addTask")
    public void onAddTask(Action.ActionPerformedEvent event) {
        if (projectSelector.getValue() == null
                || nameSelector.getValue() == null
                || dateSelector.getValue() == null) {
            notifications.create()
                    .withCaption(messageBundle.getMessage("validation.fieldsNotFilled.message"))
                    .withType(Notifications.NotificationType.WARNING)
                    .show();

            projectSelector.focus();
            return;
        }

        taskService.createTask(projectSelector.getValue(),
                nameSelector.getValue(),
                dateSelector.getValue());
        tasksDl.load();

        projectSelector.clear();
        nameSelector.clear();
        dateSelector.clear();
    }

    @Subscribe("refresh")
    public void onRefresh(Action.ActionPerformedEvent event) {
        getScreenData().loadAll();
    }

    @Subscribe("tasksCalendar")
    public void onTasksCalendarCalendarEventClick(Calendar.CalendarEventClickEvent<Task> event) {
        Task task = ((Task) event.getEntity());

        TaskEdit editor = screenBuilders.editor(Task.class, this)
                .editEntity(task)
                .withScreenClass(TaskEdit.class)
                .withOpenMode(OpenMode.DIALOG)
                .build();

        editor.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                Task editedEntity = editor.getEditedEntity();
                tasksDc.replaceItem(editedEntity);
            }
        });

        editor.show();
    }

    @Subscribe(id = "tasksDc", target = Target.DATA_CONTAINER)
    public void onTasksDcCollectionChange(CollectionContainer.CollectionChangeEvent<Task> event) {
        CollectionChangeType changeType = event.getChangeType();
        notifications.create()
                .withCaption("CollectionChangeEvent: " + changeType)
                .withType(Notifications.NotificationType.TRAY)
                .show();
    }

    @EventListener
    private void taskChanged(TaskChangedEvent event) {
        updateTaskCount();
    }

    private void updateTaskCount() {
        long count = dataManager.getCount(new LoadContext<>(metadata.getClass(Task.class)));
        sideMenu.getMenuItemNN("Task_.browse").setBadgeText(String.valueOf(count));
    }
}
