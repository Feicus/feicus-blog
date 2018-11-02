package mblog.modules.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 喜欢/收藏
 * @author Feicus on 2015/8/31.
 */
@Entity
@Table(name = "mto_favors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favor implements Serializable {
    private static final long serialVersionUID = 5083909349489897263L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * 所属用户
     */
    @Column(name = "own_id")
    private long ownId;

    /**
     * 内容ID
     */
    @Column(name = "post_id")
    private long postId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "is_delete")
    private int isDelete;
}
