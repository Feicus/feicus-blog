/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.site.auth;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import mblog.modules.user.service.NotifyService;
import mblog.web.controller.BaseController;
import mblog.web.controller.site.Views;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录页
 * @author Feicus
 */
@Slf4j
@Controller
public class LoginController extends BaseController {
    @Autowired
    private NotifyService notifyService;

    /**
     * 跳转登录页
     * @return
     */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String view() {
		return view(Views.LOGIN);
	}

    /**
     * 提交登录
     * @param username
     * @param password
     * @param model
     * @return
     */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String username, String password,@RequestParam(value = "rememberMe",defaultValue = "0") int rememberMe, ModelMap model) {
		log.info("[LoginController][login]username:{},password:{},rememberMe:{},model:{}",username,password,rememberMe,JSONObject.toJSONString(model));

	    String ret = view(Views.LOGIN);
		
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ret;
        }
		
		AuthenticationToken token = createToken(username, password);
        log.info("[LoginController][login]token:{}",JSONObject.toJSON(token));
        if (token == null) {
        	model.put("message", "用户名或密码错误");
            return ret;
        }

        if (rememberMe == 1) {
            ((UsernamePasswordToken) token).setRememberMe(true);
        }

        try {
            Subject currentUser  = SecurityUtils.getSubject();
            currentUser .login(token);
            currentUser .getSession().setTimeout(-10000l);
            ret = Views.REDIRECT_USER;
        } catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
            	model.put("message", "用户不存在");
            } else if (e instanceof LockedAccountException) {
            	model.put("message", "用户被禁用");
            } else {
            	model.put("message", "用户认证失败");
            }
        }

        return ret;
	}

}
