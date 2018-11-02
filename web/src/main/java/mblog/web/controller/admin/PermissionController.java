package mblog.web.controller.admin;

import mblog.modules.authc.data.PermissionTree;
import mblog.modules.authc.entity.Permission;
import mblog.modules.authc.service.PermissionService;
import mblog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/admin/permission")
public class PermissionController extends BaseController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/tree")
    @ResponseBody
    public List<PermissionTree> tree() {
        return permissionService.tree();
    }
}
