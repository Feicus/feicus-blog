package mblog.modules.user.data;

import mblog.modules.blog.data.PostVO;
import mblog.modules.user.entity.Notify;

/**
 * @author Feicus
 */
public class NotifyVO extends Notify {
    // extend
    private UserVO from;
    private PostVO post;

    public UserVO getFrom() {
        return from;
    }

    public void setFrom(UserVO from) {
        this.from = from;
    }

    public PostVO getPost() {
        return post;
    }

    public void setPost(PostVO post) {
        this.post = post;
    }
}
