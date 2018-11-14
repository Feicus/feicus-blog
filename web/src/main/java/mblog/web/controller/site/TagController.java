/**
 *
 */
package mblog.web.controller.site;

import mblog.modules.blog.data.PostVO;
import mblog.modules.blog.service.PostService;
import mblog.modules.user.data.AccountProfile;
import mblog.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 标签
 * @author Feicus
 *
 */
@Controller
public class TagController extends BaseController {
    @Autowired
    private PostService postService;

    @RequestMapping("/tag/{kw}")
    public String tag(@PathVariable String kw, ModelMap model) {
        AccountProfile profile = getSubject().getProfile();
        if(profile==null || profile.getUsername()==null || profile.getId()<1L){
            return view(Views.LOGIN);
        }

        Pageable pageable = wrapPageable();
        try {
            if (StringUtils.isNotEmpty(kw)) {
                Page<PostVO> page = postService.searchByTag(pageable, kw);
                model.put("page", page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.put("kw", kw);
        return view(Views.TAGS_TAG);
    }

}
