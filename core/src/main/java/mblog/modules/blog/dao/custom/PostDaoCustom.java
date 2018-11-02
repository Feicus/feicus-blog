package mblog.modules.blog.dao.custom;

import mblog.modules.blog.data.PostVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 */
public interface PostDaoCustom {
    Page<PostVO> search(Pageable pageable, String q) throws Exception;
    Page<PostVO> searchByTag(Pageable pageable, String tag);
    void resetIndexs();
}
