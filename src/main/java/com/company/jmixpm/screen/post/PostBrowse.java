package com.company.jmixpm.screen.post;

import com.company.jmixpm.app.PostService;
import com.company.jmixpm.entity.Post;
import com.company.jmixpm.screen.userinfo.UserInfoEdit;
import io.jmix.core.LoadContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Table;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("Post.browse")
@UiDescriptor("post-browse.xml")
@LookupComponent("postsTable")
@Route("posts")
public class PostBrowse extends StandardLookup<Post> {
    @Autowired
    private Table<Post> postsTable;

    @Autowired
    private PostService postService;
    @Autowired
    private ScreenBuilders screenBuilders;

    @Install(to = "postsDl", target = Target.DATA_LOADER)
    private List<Post> postsDlLoadDelegate(LoadContext<Post> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        return query == null
                ? postService.fetchPosts()
                : postService.fetchPosts(query.getFirstResult(), query.getMaxResults());
    }

    @Install(to = "pagination", subject = "totalCountDelegate")
    private Integer paginationTotalCountDelegate() {
        return postService.fetchPosts().size();
    }

    @Install(to = "userInfoScreenFacet", subject = "screenConfigurer")
    private void userInfoScreenFacetScreenConfigurer(UserInfoEdit userInfoEdit) {
        Post selected = postsTable.getSingleSelected();
        if (selected == null || selected.getUserId() == null) {
            throw new IllegalStateException("No post selected");
        }

        userInfoEdit.setUserId(selected.getUserId());
    }
}