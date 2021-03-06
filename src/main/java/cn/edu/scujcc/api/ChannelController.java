package cn.edu.scujcc.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.scujcc.model.Channel;
import cn.edu.scujcc.model.Comment;
import cn.edu.scujcc.service.ChannelService;
import cn.edu.scujcc.service.Result;
import cn.edu.scujcc.service.UserService;


@RestController
@RequestMapping("/channel")
public class ChannelController {
	private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

	@Autowired
	private ChannelService service;
	
	@Autowired
	private UserService userService;

	/**
	 * 获取所有频道
	 * 
	 * @return 所有频道的数组
	 */
	@GetMapping
	public Result<List<Channel>> getAllChannels() {
		logger.info("正在查找所有频道信息...");
		Result<List<Channel>> result = new Result<List<Channel>>();
		List<Channel> channels = service.getAllChannels();
		logger.debug("所有频道的数量是：" + channels.size());
		result = result.ok();
		result.setData(channels);
		return result;
	}

	/**
	 * 获取一个指定频道的JSON数据
	 * 
	 * @param id 指定频道的编号
	 * @return id对象频道的JSON数据
	 */
	@GetMapping("/{id}")
	public Result<Channel> getChannel(@PathVariable String id) {
		logger.info("找到频道"+id);
		Result<Channel> result = new Result<Channel>();
		Channel c = service.getChannel(id);
		if (c != null) {
			result = result.ok();
			result.setData(c);
		} else {
			logger.error("找不到指定频道。");
			result = result.error();
			result.setMessage("找不到指定的频道");
		}
		return result;
	}

	/**
	 * 删除一个指定频道
	 * 
	 * @param id 待删除频道的编号
	 * @return 成功或失败的消息
	 */
	@DeleteMapping("/{id}")
	public Result<Channel> deleteChannel(@PathVariable String id) {
		logger.info("即将删除频道:id=" + id);
		Result<Channel> result = new Result<Channel>();
		boolean del = service.deleteChannel(id);
		if (del) {
			result = result.ok();
		} else {
			result.setStatus(Result.ERROR); 
			result.setMessage("删除失败"); 
		}
		return result;
	}

	/**
	 * 新建一个频道
	 * 
	 * @param c 待新建频道的数据
	 * @return 保存后的频道数据
	 */
	@PostMapping
	public Result<Channel> createChannel(@RequestBody Channel c) {
		logger.info("即将新建频道，频道数据:" + c);
		Result<Channel> result = new Result<Channel>();
		Channel saved = service.createChannel(c);
		result = result.ok();
		result.setData(saved);
		return result;
	}

	@PutMapping
	public Result<Channel> updateChannel(@RequestBody Channel c) {
		logger.info("即将更新频道:频道数据" + c);
		Result<Channel> result = new Result<Channel>();
		Channel updated = service.updateChannel(c);
		result = result.ok();
		result.setData(updated);
		return result;
	}

	@PostMapping("/{channelId}/comment")
	public Channel addComment(@RequestHeader("token") String token, @PathVariable String channelId, @RequestBody Comment comment) {
		Channel result = null;
		logger.debug("即将评论频道：" + channelId + "，评论对象" + comment);
		//检查用户是否登录
		//把评论保存到数据库
		String username = userService.currentUser(token);
		logger.debug("登录用户"+username+"正在评论...");
		comment.setAuthor(username);
		result = service.addComment(channelId, comment);
		return result;
	}

	@GetMapping("/{channelId}/hotcomments")
	public Result<List<Comment>> hotComments(@PathVariable String channelId) {
		Result<List<Comment>> result = new Result<List<Comment>>();
		logger.debug("获取频道" + channelId + "的热门评论");
		result = result.ok();
		result.setData(service.hotComments(channelId));
		return result;
	}
	

	@GetMapping("/t/{title}")
	public List<Channel> searchByTitle(@PathVariable String title) {
		return service.searcha(title);
	}

	@GetMapping("/q/{quality}")
	public List<Channel> searchByQuality(@PathVariable String quality) {
		return service.searchb(quality);
	}

	@GetMapping("/cold")
	public List<Channel> getColdChannels() {
		return service.findColdChannels();
	}

	@GetMapping("/p/{page}")
	public List<Channel> getChannelsPage(@PathVariable int page) {
		return service.findChannelsPage(page);
	}
}
