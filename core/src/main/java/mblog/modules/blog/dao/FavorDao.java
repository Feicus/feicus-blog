package mblog.modules.blog.dao;

import mblog.modules.blog.entity.Favor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Feicus
 */
public interface FavorDao extends JpaRepository<Favor, Long>, JpaSpecificationExecutor<Favor> {
    /**
     * 指定查询
     * @param ownId
     * @param postId
     * @return
     */
    Favor findByOwnIdAndPostId(long ownId, long postId);
    Favor findByOwnIdAndPostIdAndIsDelete(long ownId,long postId,int isDelete);


    Page<Favor> findAllByOwnIdOrderByCreatedDesc(Pageable pageable, long ownId);

    Page<Favor> findAllByOwnIdAndIsDeleteOrderByCreatedDesc(Pageable pageable, long ownId,int isDelete);
}
