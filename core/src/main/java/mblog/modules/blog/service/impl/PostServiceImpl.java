/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.modules.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import mblog.base.context.SpringContextHolder;
import mblog.base.lang.Consts;
import mblog.base.lang.EntityStatus;
import mblog.base.utils.PreviewTextUtils;
import mblog.core.event.PostUpdateEvent;
import mblog.modules.blog.dao.FavorDao;
import mblog.modules.blog.data.PostVO;
import mblog.modules.blog.entity.Favor;
import mblog.modules.user.data.UserVO;
import mblog.modules.blog.dao.PostAttributeDao;
import mblog.modules.blog.dao.PostDao;
import mblog.modules.blog.entity.Channel;
import mblog.modules.blog.entity.PostAttribute;
import mblog.modules.blog.entity.Post;
import mblog.modules.utils.BeanMapUtils;
import mblog.modules.blog.service.ChannelService;
import mblog.modules.blog.service.FavorService;
import mblog.modules.blog.service.PostService;
import mblog.modules.user.service.UserEventService;
import mblog.modules.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author Feicus
 *
 */
@Service
@Transactional
@CacheConfig(cacheNames = "postsCaches")
public class PostServiceImpl implements PostService {
	@Autowired
	private PostDao postDao;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEventService userEventService;
	@Autowired
	private FavorService favorService;
	@Autowired
	private FavorDao favorDao;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private PostAttributeDao postAttributeDao;

	@Override
	@Cacheable
	public Page<PostVO> paging(Pageable pageable, int channelId, Set<Integer> excludeChannelIds, String ord) {

		Page<Post> page = postDao.findAll((root, query, builder) -> {
			List<Order> orders = new ArrayList<>();
			if (Consts.order.FAVOR.equals(ord)) {
				orders.add(builder.desc(root.<Long>get("favors")));
			} else if (Consts.order.HOTTEST.equals(ord)) {
				orders.add(builder.desc(root.<Long>get("comments")));
			} else {
				orders.add(builder.desc(root.<Long>get("weight")));
			}
			orders.add(builder.desc(root.<Long>get("created")));

            if (Consts.order.HOTTEST.equals(ord)) {
                orders.add(builder.desc(root.<Long>get("views")));
            }


            Predicate predicate = builder.conjunction();

            if (channelId > Consts.ZERO) {
                predicate.getExpressions().add(
						builder.equal(root.get("channelId").as(Integer.class), channelId));
            }

            if (null != excludeChannelIds && !excludeChannelIds.isEmpty()) {
                predicate.getExpressions().add(
						builder.not(root.get("channelId").in(excludeChannelIds)));
            }

            predicate.getExpressions().add(builder.equal(root.get("isDelete"),Consts.DELETE_DEFAULT));
            predicate.getExpressions().add(builder.equal(root.get("isPublic"),Consts.PUBLISH_CHOISE_ALLSEE));

			query.orderBy(orders);

			return predicate;
		}, pageable);

		return new PageImpl<>(toPosts(page.getContent()), pageable, page.getTotalElements());
	}

	@Override
	public Page<PostVO> paging4Admin(Pageable pageable, long id, String title, int channelId) {
		Page<Post> page = postDao.findAll((root, query, builder) -> {
            query.orderBy(
					builder.desc(root.<Long>get("weight")),
					builder.desc(root.<Long>get("created"))
			);

            Predicate predicate = builder.conjunction();

			if (channelId > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("channelId").as(Integer.class), channelId));
			}

			if (StringUtils.isNotBlank(title)) {
				predicate.getExpressions().add(
						builder.like(root.get("title").as(String.class), "%" + title + "%"));
			}

			if (id > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("id").as(Integer.class), id));
			}

            return predicate;
        }, pageable);

		return new PageImpl<>(toPosts(page.getContent()), pageable, page.getTotalElements());
	}

	@Override
	@Cacheable
	public Page<PostVO> pagingByAuthorId(Pageable pageable, long userId) {
		Page<Post> page = postDao.findAllByAuthorIdOrderByCreatedDesc(pageable, userId);
		return new PageImpl<>(toPosts(page.getContent()), pageable, page.getTotalElements());
	}


    @Override
    @Cacheable
    public Page<PostVO> pagingByAuthorId(Pageable pageable, long userId,int isDelete) {
        Page<Post> page = postDao.findAllByAuthorIdAndIsDeleteOrderByCreatedDesc(pageable, userId,isDelete);
        return new PageImpl<>(toPosts(page.getContent()), pageable, page.getTotalElements());
    }


    @Override
	@Cacheable
	public List<PostVO> findAllFeatured() {
		List<Post> list = postDao.findTop5ByFeaturedGreaterThanOrderByCreatedDesc(Consts.FEATURED_DEFAULT);
		return toPosts(list);
	}

	@Override
	public Page<PostVO> search(Pageable pageable, String q) throws Exception {
		Page<PostVO> page = postDao.search(pageable, q);

		HashSet<Long> ids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		for (Post po : page.getContent()) {
			ids.add(po.getId());
			uids.add(po.getAuthorId());
		}

		// 加载用户信息
		buildUsers(page.getContent(), uids);

		return page;
	}

	@Override
	public Page<PostVO> searchByTag(Pageable pageable, String tag) {
		Page<PostVO> page = postDao.searchByTag(pageable, tag);

		HashSet<Long> ids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		for (Post po : page.getContent()) {
			ids.add(po.getId());
			uids.add(po.getAuthorId());
		}

		// 加载用户信息
		buildUsers(page.getContent(), uids);
		return page;
	}

	@Override
    @Cacheable
    public List<PostVO> findLatests(int maxResults, long ignoreUserId) {
        List<Post> list = postDao.findTop10ByOrderByCreatedDesc();
        List<PostVO> rets = new ArrayList<>();

        list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));

        return rets;
    }

    @Override
    @Cacheable
    public List<PostVO> findLatests(int maxResults, long ignoreUserId,int isDelete) {
        List<Post> list = postDao.findTop10ByIsDeleteOrderByCreatedDesc(isDelete);
        List<PostVO> rets = new ArrayList<>();

        list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));

        return rets;
    }


	@Override
	@Cacheable
	public List<PostVO> findHots(int maxResults, long ignoreUserId) {
		List<Post> list = postDao.findTop10ByOrderByViewsDesc();
		List<PostVO> rets = new ArrayList<>();

		list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));
		return rets;
	}


    @Override
    @Cacheable
    public List<PostVO> findHots(int maxResults, long ignoreUserId,int isDelete) {
        List<Post> list = postDao.findTop10ByIsDeleteOrderByViewsDesc(isDelete);
        List<PostVO> rets = new ArrayList<>();

        list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));
        return rets;
    }

	@Override
	public Map<Long, PostVO> findMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		List<Post> list = postDao.findAllByIdIn(ids);
		Map<Long, PostVO> rets = new HashMap<>();

		HashSet<Long> uids = new HashSet<>();

		list.forEach(po -> {
			rets.put(po.getId(), BeanMapUtils.copy(po, 0));
			uids.add(po.getAuthorId());
		});

		// 加载用户信息
		buildUsers(rets.values(), uids);

		return rets;
	}


    @Override
    public Map<Long, PostVO> findMapByIds(Set<Long> ids, int isDelete) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Post> list = postDao.findAllByIdInAndIsDelete(ids,isDelete);
        Map<Long, PostVO> rets = new HashMap<>();

        HashSet<Long> uids = new HashSet<>();

        list.forEach(po -> {
            rets.put(po.getId(), BeanMapUtils.copy(po, 0));
            uids.add(po.getAuthorId());
        });

        // 加载用户信息
        buildUsers(rets.values(), uids);

        return rets;
    }

    @Override
	@Transactional
	@CacheEvict(allEntries = true)
	public long post(PostVO post) {
		Post po = new Post();

		BeanUtils.copyProperties(post, po);

		po.setCreated(new Date());
		po.setStatus(EntityStatus.ENABLED);

		// 处理摘要
		if (StringUtils.isBlank(post.getSummary())) {
			po.setSummary(trimSummary(post.getContent()));
		} else {
			po.setSummary(post.getSummary());
		}

		postDao.save(po);

		PostAttribute attr = new PostAttribute();
		attr.setContent(post.getContent());
		attr.setId(po.getId());
		submitAttr(attr);

		onPushEvent(po, PostUpdateEvent.ACTION_PUBLISH);
		return po.getId();
	}

	@Override
	@Cacheable(key = "'view_' + #id")
	public PostVO get(long id) {
		Post po = postDao.findOne(id);
		PostVO d = null;
		if (po != null) {
			d = BeanMapUtils.copy(po, 1);

			d.setAuthor(userService.get(d.getAuthorId()));

			d.setChannel(channelService.getById(d.getChannelId()));

			PostAttribute attr = postAttributeDao.findOne(po.getId());
			if (attr != null) {
				d.setContent(attr.getContent());
			}
		}
		return d;
	}


    @Override
    @Cacheable(key = "'view_' + #id")
    public PostVO get(long id,int isDelete) {
        Post po = postDao.findByIdAndIsDelete(id,isDelete);
        PostVO d = null;
        if (po != null) {
            d = BeanMapUtils.copy(po, 1);

            d.setAuthor(userService.get(d.getAuthorId()));

            d.setChannel(channelService.getById(d.getChannelId()));

            PostAttribute attr = postAttributeDao.findOne(po.getId());
            if (attr != null) {
                d.setContent(attr.getContent());
            }
        }
        return d;
    }



	/**
	 * 更新文章方法
	 * @param p
	 */
	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void update(PostVO p){
		Post po = postDao.findOne(p.getId());

		if (po != null) {
			po.setTitle(p.getTitle());//标题
			po.setChannelId(p.getChannelId());
			po.setThumbnail(p.getThumbnail());
            po.setIsPublic(p.getIsPublic());
			// 处理摘要
			if (StringUtils.isBlank(p.getSummary())) {
				po.setSummary(trimSummary(p.getContent()));
			} else {
				po.setSummary(p.getSummary());
			}

			po.setTags(p.getTags());//标签

			// 保存扩展
			PostAttribute attr = new PostAttribute();
			attr.setContent(p.getContent());
			attr.setId(po.getId());
			submitAttr(attr);
		}
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void updateFeatured(long id, int featured) {
		Post po = postDao.findOne(id);

		if (po != null) {
			int status = Consts.FEATURED_ACTIVE == featured ? Consts.FEATURED_ACTIVE: Consts.FEATURED_DEFAULT;
			po.setFeatured(status);
			postDao.save(po);
		}
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void updateWeight(long id, int weight) {
		Post po = postDao.findOne(id);

		if (po != null) {
			int max = weight;
			if (Consts.FEATURED_ACTIVE == weight) {
				max = postDao.maxWeight() + 1;
			}
			po.setWeight(max);
			postDao.save(po);
		}
	}


	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void updateDelete(long id,long authorId,int delete){
		Post po = postDao.findOne(id);
		if(po !=null){
            // 判断文章是否属于当前登录用户
            Assert.isTrue(po.getAuthorId() == authorId, "认证失败");
			int status=Consts.DELETE_ACTIVE==delete?Consts.DELETE_ACTIVE:Consts.DELETE_DEFAULT;
			po.setIsDelete(status);
			postDao.save(po);
			onPushEvent(po, PostUpdateEvent.ACTION_DELETE);
		}
	}


	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void updateDelete(long id,int delete){
		Post po = postDao.findOne(id);
		if(po !=null){
			int status=Consts.DELETE_ACTIVE==delete?Consts.DELETE_ACTIVE:Consts.DELETE_DEFAULT;
			po.setIsDelete(status);
			postDao.save(po);
		}
	}



	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(long id) {
		Post po = postDao.findOne(id);
		if (po != null) {
			postDao.delete(id);
			postAttributeDao.delete(id);

			onPushEvent(po, PostUpdateEvent.ACTION_DELETE);
		}
	}
	
	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(long id, long authorId) {
		Post po = postDao.findOne(id);
		if (po != null) {
			// 判断文章是否属于当前登录用户
			Assert.isTrue(po.getAuthorId() == authorId, "认证失败");

			postDao.delete(id);
			postAttributeDao.delete(id);

			onPushEvent(po, PostUpdateEvent.ACTION_DELETE);
		}
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(Collection<Long> ids) {
		ids.forEach(this::delete);
	}

	@Override
	@Transactional
	public void identityViews(long id) {
		// 次数不清理缓存, 等待文章缓存自动过期
		postDao.updateViews(id, Consts.IDENTITY_STEP);
	}

	@Override
	@Transactional
	public void identityComments(long id) {
		postDao.updateComments(id, Consts.IDENTITY_STEP);
	}

	@Override
	@Transactional
	@CacheEvict(key = "'view_' + #postId")
	public void favor(long userId, long postId) {
	    //1.看本地数据库 喜欢 表中
        Favor po = favorDao.findByOwnIdAndPostIdAndIsDelete(userId, postId,Consts.DELETE_ACTIVE);
        if(po==null){
            favorService.add(userId, postId);
            postDao.updateFavors(postId, Consts.IDENTITY_STEP);
        }else {
            po.setIsDelete(Consts.DELETE_DEFAULT);
            favorDao.save(po);
            postDao.updateFavors(postId, Consts.IDENTITY_STEP);
        }
	}

	@Override
	@Transactional
	@CacheEvict(key = "'view_' + #postId")
	public void unfavor(long userId, long postId) {
        favorService.updateDelete(userId, postId,Consts.DELETE_ACTIVE);
        postDao.updateFavors(postId,  Consts.DECREASE_STEP);
	}
	
	@Override
	@Transactional
	public void resetIndexs() {
		postDao.resetIndexs();
	}

	/**
	 * 截取文章内容
	 * @param text
	 * @return
	 */
	private String trimSummary(String text){
		return PreviewTextUtils.getText(text, 126);
	}

	private List<PostVO> toPosts(List<Post> posts) {
		List<PostVO> rets = new ArrayList<>();

		HashSet<Long> pids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();
		HashSet<Integer> groupIds = new HashSet<>();

		posts.forEach(po -> {
			pids.add(po.getId());
			uids.add(po.getAuthorId());
			groupIds.add(po.getChannelId());

			rets.add(BeanMapUtils.copy(po, 0));
		});

		// 加载用户信息
		buildUsers(rets, uids);
		buildGroups(rets, groupIds);

		return rets;
	}

	private void buildUsers(Collection<PostVO> posts, Set<Long> uids) {
		Map<Long, UserVO> userMap = userService.findMapByIds(uids);
		posts.forEach(p -> p.setAuthor(userMap.get(p.getAuthorId())));
	}

	private void buildGroups(Collection<PostVO> posts, Set<Integer> groupIds) {
		Map<Integer, Channel> map = channelService.findMapByIds(groupIds);
		posts.forEach(p -> p.setChannel(map.get(p.getChannelId())));
	}

	private void submitAttr(PostAttribute attr) {
		postAttributeDao.save(attr);
	}

	private void onPushEvent(Post post, int action) {
		PostUpdateEvent event = new PostUpdateEvent(System.currentTimeMillis());
		event.setPostId(post.getId());
		event.setUserId(post.getAuthorId());
		event.setAction(action);
		SpringContextHolder.publishEvent(event);
	}
}
