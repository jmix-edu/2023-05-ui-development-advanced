package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.Task;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.action.list.CreateAction;
import io.jmix.ui.action.list.EditAction;
import io.jmix.ui.component.TabSheet;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {

    @Autowired
    private CollectionLoader<Task> tasksDl;

    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Notifications notifications;

    private Table<Task> tasksTable;

    @Subscribe("tabSheet")
    public void onTabSheetSelectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        if ("tasksTab".equals(event.getSelectedTab().getName())) {
            initTable();
        }
    }

    @SuppressWarnings("unchecked")
    private void initTable() {
        if (tasksTable != null) {
            return;
        }

        tasksDl.setParameter("project", getEditedEntity());
        tasksDl.load();

        tasksTable = ((Table<Task>) getWindow().getComponentNN("tasksTable"));
        ((BaseAction) tasksTable.getActionNN(CreateAction.ID))
                .addActionPerformedListener(this::onTasksTableCreate);
        ((BaseAction) tasksTable.getActionNN(EditAction.ID))
                .addActionPerformedListener(this::onTasksTableEdit);
    }

    public void onTasksTableCreate(Action.ActionPerformedEvent event) {
        if (tasksTable == null) {
            return;
        }

        Task newTask = dataManager.create(Task.class);
        newTask.setProject(getEditedEntity());


        screenBuilders.editor(tasksTable)
                .newEntity(newTask)
                .withParentDataContext(getScreenData().getDataContext())
                .show();
    }

    public void onTasksTableEdit(Action.ActionPerformedEvent event) {
        if (tasksTable == null) {
            return;
        }

        Task selected = tasksTable.getSingleSelected();
        if (selected == null) {
            return;
        }

        screenBuilders.editor(tasksTable)
                .editEntity(selected)
                .withParentDataContext(getScreenData().getDataContext())
                .show();
    }

    @Subscribe(id = "tasksDl", target = Target.DATA_LOADER)
    public void onTasksDlPostLoad(CollectionLoader.PostLoadEvent<Task> event) {
        notifications.create()
                .withCaption("[tasksDl] PostLoadEvent")
                .withType(Notifications.NotificationType.TRAY)
                .show();
    }
}