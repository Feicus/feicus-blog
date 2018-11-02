package mblog.modules.blog.service;

import mblog.modules.blog.data.FavorVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Feicus on 2015/8/31.
 */
public interface FavorService {
    /**
     *
     * @param userId
     * @param postId
     * @return
     */
    void add(long userId, long postId);
    void delete(long userId, long postId);
    void updateDelete(long userId, long postId,int isDelete);
    /**
     * 分页查询用户的喜欢记录
     * @param pageable
     * @param ownId
     */
    Page<FavorVO> pagingByOwnId(Pageable pageable, long ownId);

    Page<FavorVO> pagingByOwnId(Pageable pageable, long ownId, int isDelete);
}
