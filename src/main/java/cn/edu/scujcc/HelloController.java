package cn.edu.scujcc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
	
	@GetMapping
	public Map<String, Object> sayHello(){
		Map<String, Object> result = new HashMap<>();
		result.put("hello", "¹ù»ªÓî 184020268");
		return result;
	}
}
