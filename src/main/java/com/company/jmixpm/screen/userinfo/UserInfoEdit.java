package com.company.jmixpm.screen.userinfo;

import com.company.jmixpm.app.PostService;
import com.company.jmixpm.entity.UserInfo;
import io.jmix.core.LoadContext;
import io.jmix.ui.action.Action;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("UserInfo.edit")
@UiDescriptor("user-info-edit.xml")
@EditedEntityContainer("userInfoDc")
@DialogMode(width = "AUTO", height = "AUTO", forceDialog = true)
public class UserInfoEdit extends Screen {

    @Autowired
    private PostService postService;

    private Long userId;

    public UserInfoEdit setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    @Install(to = "userInfoDl", target = Target.DATA_LOADER)
    private UserInfo userInfoDlLoadDelegate(LoadContext<UserInfo> loadContext) {
        return postService.fetchUserInfo(userId);
    }

    @Subscribe("windowClose")
    public void onWindowClose(Action.ActionPerformedEvent event) {
        closeWithDefaultAction();
    }
}