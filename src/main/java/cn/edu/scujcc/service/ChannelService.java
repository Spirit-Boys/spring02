package cn.edu.scujcc.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.edu.scujcc.dao.ChannelRepository;
import cn.edu.scujcc.model.Channel;
import cn.edu.scujcc.model.Comment;

@Service
public class ChannelService {
	public static final Logger logger = LoggerFactory.getLogger(ChannelService.class);
	@Autowired
	private ChannelRepository repo;

	/**
	 * 
	 */
//	private List<Channel> getAllChannels(){
//		return repo.findAll();
//		}

	public Channel getChannel(String channelId) {
		Optional<Channel> result = repo.findById(channelId);

		if (result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

	/**
	 * 获取所有频道
	 */
	public List<Channel> getAllChannels() {
		return repo.findAll();
	}

	/**
	 * 删除指定频道
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteChannel(String channelId) {
		boolean result = true;
		repo.deleteById(channelId);
		return result;
	}

	/**
	 * 更新一个频道
	 * 
	 * @param c 待更新的频道
	 * @return 更新后的频道
	 */
	public Channel updateChannel(Channel c) {
		// TODO 仅修改用户指定的属性
		Channel saved = getChannel(c.getId());
		if (saved != null) {
			if (c.getTitle() != null) {
				saved.setTitle(c.getTitle());
			}
			if (c.getQuality() != null) {
				saved.setQuality(c.getQuality());
			}
			if (c.getUrl() != null) {
				saved.setUrl(c.getUrl());
			}
			if (c.getComments() != null) {
				if (saved.getComments() != null) {// 把新评论追加到老评论后面
					saved.getComments().addAll(c.getComments());
				} else {// 用新评论代替老的空评论
					saved.setComments(c.getComments());
				}
			}
			if (c.getCover() != null) {
				saved.setCover(c.getCover());
			}
		}
		return repo.save(saved); // 保存更新后的实体对象
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public Channel createChannel(Channel c) {
		return repo.save(c);
		/*
		 * c.setId(this.channels.get(this.channels.size()-1).getId()+1);
		 * this.channels.add(c); return c;
		 */

	}

	public List<Channel> searcha(String title) {
		return repo.findByTitle(title);

	}

	public List<Channel> searchb(String quality) {
		return repo.findByQuality(quality);

	}

	/**
	 * 获取冷门频道
	 * 
	 * @return
	 */
	public List<Channel> findColdChannels() {
		return repo.findByCommentsNull();
	}

	public List<Channel> findChannelsPage(int page) {
		Page<Channel> p = repo.findAll(PageRequest.of(page, 2));
		return p.toList();
	}

	/**
	 * 向指定频道添加一条评论
	 * 
	 * @param channelId 目标频道编号
	 * @param comment   即将添加的评论
	 */
	public Channel addComment(String channelId, Comment comment) {
		Channel result = null;
		Channel saved = getChannel(channelId);
		if (null != saved) {// 数据库中有该频道
			saved.addComment(comment);
			result = repo.save(saved);
		}
		return result;
	}

	/**
	 * 获取热门评论指定频道
	 * 
	 * @param channelId 目标频道id
	 * @return 热门评论内容
	 */
	public List<Comment> hotComments(String channelId) {
		List<Comment> result = null;
		Channel saved = getChannel(channelId);
		if (saved != null) {
			result = saved.getComments();
			saved.getComments().sort(new Comparator<Comment>() {
				// 若o1<o2,则返回负数;若o1>o2,则返回正数;若o1=o2,则返回0
				@Override
				public int compare(Comment o1, Comment o2) {
					int re = 0;
					if (o1.getStar() < o2.getStar()) {
						re = 1;
					} else if (o1.getStar() > o2.getStar()) {
						re = -1;
					}
					return re;
				}
			});
			if (result.size() > 3) {
				result = result.subList(0, 3);
			}
			logger.debug("热门评论有" + result.size() + "条。。。");
			logger.debug(result.toString());
		} else {
			logger.warn("指定的频道不存在，id=" + channelId);
		}
		return result;
	}

//	public ChannelService() {
//		channels = new ArrayList<>();
//			channels.add(c);
//		}
//	}
//	/**
//	 * 获取所有频道数据
//	 * @return 频道List
//	 */
//	public List<Channel> getAllChannels(){
//		//return this.channels;
//		return repo.findAll();
//	}
//	
//	/**
//	 * 获取一个频道的数据
//	 * @param channelId 频道编号
//	 * @return 频道对象,若未找到则返回null
//	 */
//	public Channel getChannel(String channelId) {
//		Channel result = null;
//		//循环查找指定频道
//		for (Channel c:channels) {
//			if (c.getId() == channelId) {
//				result = c;
//				break;
//			}
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * 删除指定频道
//	 * @param channelId 但删除频道编号
//	 * @return 若删除成功返回true,失败返回false
//	 */
//	public boolean deleteChannel(String channelId) {
//		boolean result = false;
//		Channel c = getChannel(channelId);
//		if (c != null) {
//			channels.remove(c);
//			result = true;
//		}
//		return result;
//	}
//	
//	/**
//	 * 保存频道
//	 * @param c 待保存频道对象（没有id值）
//	 * @return 保存后的频道（有id值）
//	 */
//	public Channel createChannel(Channel c) {
	// 找到目前最大的id,并增加1做完新频道id
//		String newId = channels.get(channels.size() - 1).getId() + 1;
//		c.setId(newId);
//		channels.add(c);
//		return c;
//		return repo.save(c);
//	}
//	
//	/**
//	 * 更新指定频道信息
//	 * @param c 新的频道信息，用于更新已存在的同一频道
//	 * @return 更新后的频道信息
//	 */
//	public Channel updateChannel(Channel c) {
//		Channel toUpdate = getChannel(c.getId());
//		if (toUpdate != null) {
//			toUpdate.setTitle(c.getTitle());
//			toUpdate.setQuality(c.getQuality());
//			toUpdate.setUrl(c.getUrl());
//		}
//		return toUpdate;
//		return null;
//		return repo.save(c);
//	}
//	public List<>
}
