/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.site;

import javax.servlet.http.HttpServletRequest;

import mblog.modules.user.data.AccountProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import mblog.base.lang.Consts;
import mblog.web.controller.BaseController;

/**
 * @author Feicus
 *
 */
@Controller
public class IndexController extends BaseController{
	@RequestMapping(value= {"/", "/index"})
	public String root(ModelMap model, HttpServletRequest request) {
        AccountProfile profile = getSubject().getProfile();
        if(profile==null || profile.getUsername()==null || profile.getId()<1L){
            return view(Views.LOGIN);
        }

        String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
		int pn = ServletRequestUtils.getIntParameter(request, "pn", 1);
		model.put("order", order);
		model.put("pn", pn);
		return view(Views.INDEX);
	}

}
