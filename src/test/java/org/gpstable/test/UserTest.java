package org.gpstable.test;

import org.gpstable.domain.User;
import org.gpstable.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTest extends BaseTest {
	
	
	@Autowired
	private UserMapper userMapper;
	
	@org.junit.Test
	public void Test2(){
		userMapper.insert(new User(2l,"aa"));
		System.out.println(userMapper.query(2l));
		userMapper.delete(2l);
		userMapper.update(2l);
	}	
}
