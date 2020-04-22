package cn.edu.scujcc.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cn.edu.scujcc.model.Channel;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, String>{
	public List<Channel> findByTitle(String title);
	public List<Channel> findByQuality(String quality);
	public Channel findFirstByTitle(String title);
	/**
	 * 找出没有评论的频道
	 * @return
	 */
	public List<Channel> findByCommentsNull();
}
