/*
+--------------------------------------------------------------------------
|   WeCMS [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.site.posts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.imageio.plugins.common.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import mblog.base.utils.FileKit;
import mblog.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Ueditor 文件上传
 *
 * @author Feicus
 */
@Slf4j
@Controller
@RequestMapping("/post")
public class UploadController extends BaseController {
    private static HashMap<String, String> errorInfo = new HashMap<>();

    @Value("${site.store.size:20}")
    private String storeSize;

    static {
        errorInfo.put("SUCCESS", "SUCCESS"); //默认成功
        errorInfo.put("NOFILE", "未包含文件上传域");
        errorInfo.put("TYPE", "不允许的文件格式");
        errorInfo.put("SIZE", "图片大小超出限制，最大支持20Mb");
        errorInfo.put("ENTYPE", "请求类型ENTYPE错误");
        errorInfo.put("REQUEST", "上传请求异常");
        errorInfo.put("IO", "IO异常");
        errorInfo.put("DIR", "目录创建失败");
        errorInfo.put("UNKNOWN", "未知错误");
    }


    @PostMapping("/uploadImage")
    @ResponseBody
    public Map<String, Object> uploadImage( @RequestParam("file") MultipartFile file) throws Exception {

        log.info("[UploadController][uploadImage]请求参数file:{}",JSONObject.toJSON(file));

        Map<String, Object> ret = new HashMap<>();
        // 检查空
        if (null == file || file.isEmpty()) {
            ret.put("errorInfo", errorInfo.get("NOFILE"));
            return ret;
        }
        // 检查类型
        String fileName = file.getOriginalFilename();
        if (!FileKit.checkFileType(fileName)) {
            ret.put("errorInfo", errorInfo.get("TYPE"));
            return ret;
        }
        //检查大小
        // 检查大小
        if (file.getSize() > (Long.parseLong(storeSize) * 1024 * 1024)) {
            ret.put("errorInfo", errorInfo.get("SIZE"));
            return ret;
        }


        String location = null;
        try {
            location = fileRepo.store(file, appContext.getThumbsDir());
            ret.put("errorInfo", errorInfo.get("SUCCESS"));
            ret.put("location", location);
        } catch (IOException e) {
            ret.put("errorInfo", errorInfo.get("UNKNOWN"));
            e.printStackTrace();
        }
        log.info("[UploadController][uploadImage]返回结果ret:{}",JSONObject.toJSON(ret));
        return ret;
    }

    @PostMapping("/upload")
    @ResponseBody
    public UploadResult upload(@RequestParam(value = "file", required = false) MultipartFile file,
                               HttpServletRequest request) throws IOException {
        UploadResult result = new UploadResult();
        String cropStr = request.getParameter("crop");
        Boolean crop=false;
        if(cropStr!=null && cropStr.equals("true")){
            crop=true;
        }
        int size = ServletRequestUtils.getIntParameter(request, "size", 800);

        // 检查空
        if (null == file || file.isEmpty()) {
            return result.error(errorInfo.get("NOFILE"));
        }

        String fileName = file.getOriginalFilename();

        // 检查类型
        if (!FileKit.checkFileType(fileName)) {
            return result.error(errorInfo.get("TYPE"));
        }

        // 检查大小
        if (file.getSize() > (Long.parseLong(storeSize) * 1024 * 1024)) {
            return result.error(errorInfo.get("SIZE"));
        }

        // 保存图片
        try {
            String path;
            if (crop) {
                int width = ServletRequestUtils.getIntParameter(request, "width", 364);
                int height = ServletRequestUtils.getIntParameter(request, "height", 200);
                path = fileRepo.storeScale(file, appContext.getThumbsDir(), width, height);
            } else {
                path = fileRepo.storeScale(file, appContext.getThumbsDir(), size);
            }
            result.ok(errorInfo.get("SUCCESS"));
            result.setName(fileName);
            result.setType(getSuffix(fileName));
            result.setPath(path);
            result.setSize(file.getSize());

        } catch (Exception e) {
            result.error(errorInfo.get("UNKNOWN"));
            e.printStackTrace();
        }

        return result;
    }

    static class UploadResult {
        public static int OK = 200;
        public static int ERROR = 400;

        /**
         * 上传状态
         */
        private int status;

        /**
         * 提示文字
         */
        private String message;

        /**
         * 文件名
         */
        private String name;

        /**
         * 文件大小
         */
        private long size;

        /**
         * 文件类型
         */
        private String type;

        /**
         * 文件存放路径
         */
        private String path;

        public UploadResult ok(String message) {
            this.status = OK;
            this.message = message;
            return this;
        }

        public UploadResult error(String message) {
            this.status = ERROR;
            this.message = message;
            return this;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }
}
