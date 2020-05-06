package cn.edu.scujcc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.scujcc.service.UserService;
import cn.edu.scujcc.dao.UserRepository;
import cn.edu.scujcc.model.User;

@Service
public class UserService {
	@Autowired
	private UserRepository repo;
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	/**
	 * 新建用户（用户注册）
	 * @param user
	 * @return
	 */
	public User createUser(User user) {
		logger.debug("用户注册："+user);
		User result = null;
		//TODO 保存前加密用户密码
		result = repo.save(user);
		return result;
	}
	
	/**
	 * 检查用户名密码是否匹配
	 * @param username 用户名
	 * @param password 密码
	 * @return 如果正确返回true 错误返回false
	 */
	public boolean checkUser(String username, String password) {
		boolean result = false;
		User u = repo.findOneByUsernameAndPassword(username, password);
		logger.debug("数据库中的用户信息是："+u);
		if(null != u) {
			result = true;
		}
		return result;
	}
}
