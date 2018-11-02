/**
 * 
 */
package mblog.web.controller.site.auth;

import mblog.base.data.Data;
import mblog.base.lang.Consts;
import mblog.base.utils.MailHelper;
import mblog.modules.user.data.AccountProfile;
import mblog.modules.user.data.UserVO;
import mblog.modules.user.service.UserService;
import mblog.modules.user.service.VerifyService;
import mblog.web.controller.BaseController;
import mblog.web.controller.site.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Feicus
 *
 */
@Controller
public class RegisterController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private VerifyService verifyService;
	@Autowired
	private MailHelper mailHelper;
	@Autowired
	private ExecutorService executorService;

	@Value("${website.auth.register}")
	private String registerSwitch;
	
	@GetMapping("/register")
	public String view(ModelMap model) {
		AccountProfile profile = getSubject().getProfile();
        if (profile != null) {
            return view(Views.INDEX);
        }
        Assert.isTrue("on".equals(registerSwitch),"注册功能已关闭");
        return view(Views.REGISTER);
	}
	
	@PostMapping("/register")
	public String register(UserVO post, ModelMap model) {
        Assert.isTrue("on".equals(registerSwitch),"注册功能已关闭");

        Data data;
        String ret = view(Views.REGISTER);

		try {
			post.setAvatar(Consts.AVATAR);
			UserVO user = userService.register(post);

			String code = verifyService.generateCode(user.getId(), Consts.VERIFY_BIND, user.getEmail());
			Map<String, Object> context = new HashMap<>();
			context.put("userId", user.getId());
			context.put("code", code);
			context.put("type", Consts.VERIFY_BIND);

			sendEmail(Consts.EMAIL_TEMPLATE_BIND, user.getEmail(), "邮箱绑定验证", context);

			data = Data.success("恭喜您! 注册成功", Data.NOOP);
			data.addLink("login", "先去登陆尝尝鲜");

			ret = view(Views.REGISTER_RESULT);
			
		} catch (Exception e) {
            model.addAttribute("post", post);
			data = Data.failure(e.getMessage());
		}
		model.put("data", data);
		return ret;
	}

}