package com.company.jmixpm.screen.userinfo;

import com.company.jmixpm.app.PostService;
import com.company.jmixpm.entity.UserInfo;
import com.google.common.collect.ImmutableMap;
import io.jmix.core.LoadContext;
import io.jmix.ui.action.Action;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.navigation.UrlIdSerializer;
import io.jmix.ui.navigation.UrlParamsChangedEvent;
import io.jmix.ui.navigation.UrlRouting;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("UserInfo.edit")
@UiDescriptor("user-info-edit.xml")
@EditedEntityContainer("userInfoDc")
@DialogMode(width = "AUTO", height = "AUTO", forceDialog = true)
@Route("user-info")
public class UserInfoEdit extends Screen {

    @Autowired
    private PostService postService;
    @Autowired
    private UrlRouting urlRouting;

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

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        String serializedId = UrlIdSerializer.serializeId(userId);
        urlRouting.replaceState(this, ImmutableMap.of("id", serializedId));
    }

    @Subscribe
    public void onUrlParamsChanged(UrlParamsChangedEvent event) {
        String serializedId = event.getParams().get("id");
        userId = ((Long) UrlIdSerializer.deserializeId(Long.class, serializedId));
    }
}