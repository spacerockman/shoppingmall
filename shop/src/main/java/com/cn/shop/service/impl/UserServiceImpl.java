package com.cn.shop.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.shop.dao.UserMapper;
import com.cn.shop.pojo.User;
import com.cn.shop.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	
	@Override
	public User getUserById(int userId) {
		// TODO Auto-generated method stub
		return this.userMapper.selectByPrimaryKey(userId);
	}

}
