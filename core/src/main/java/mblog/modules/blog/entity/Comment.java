/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.modules.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论
 * @author Feicus
 *
 */
@Entity
@Table(name = "mto_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    private static final long serialVersionUID = -1260085843239420410L;
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/**
	 * 所属内容ID
	 */
	@Column(name = "to_id")
	private long toId;

	/**
	 * 父评论ID
	 */
	private long pid;

	/**
	 * 评论内容
	 */
	@Column(name = "content")
	private String content;
	
	@Column(name = "created")
	private Date created;
	
	@Column(name = "author_id")
	private long authorId;
	
	private int status;

	@Column(name = "is_delete")
	private int isDelete;
}
