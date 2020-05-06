package cn.edu.scujcc.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.scujcc.model.User;
import cn.edu.scujcc.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService service;
	
	@PostMapping("/register")
	public User register(@RequestBody User u) {
		logger.debug("注册用户，数据："+u);
		User saved = service.createUser(u);
		return saved;
	}
	
	@GetMapping("/login/{username}/{password}")
	public int login(@PathVariable String username,@PathVariable String password) {
		int result = 0;
		//FIXME 删除此句日志
		logger.debug("用户"+username+"准备登陆，密码是："+password);
		boolean status = service.checkUser(username, password);
		if(status) {
			result = 1;
		}
		return result;
	}
}
