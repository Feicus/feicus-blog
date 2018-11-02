package mblog.modules.blog.dao.custom;

import mblog.modules.blog.data.FeedsVO;

/**
 *
 */
public interface FeedsDaoCustom {
    /**
     * 添加动态, 同时会分发给粉丝
     *
     * @param feeds
     * @return
     */
    int batchAdd(FeedsVO feeds);
}
