package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.action.Action;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Project.browse")
@UiDescriptor("project-browse.xml")
@LookupComponent("projectsTable")
public class ProjectBrowse extends StandardLookup<Project> {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private CollectionLoader<Project> projectsDl;
    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe("projectsTable.generateData")
    public void onProjectsTableGenerateData(Action.ActionPerformedEvent event) {
        SaveContext saveContext = new SaveContext();
        for (int i = 0; i < 200; i++) {
            Project project = dataManager.create(Project.class);
            User user = (User) currentAuthentication.getUser();
            project.setManager(user);
            project.setName("Project #" + i);
            saveContext.saving(project);
        }

        dataManager.save(saveContext);
        projectsDl.load();
    }


}