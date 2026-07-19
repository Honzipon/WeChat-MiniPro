package com.ticket.mapper;

import com.ticket.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest  // 启动Spring环境
public class UserMapperTest {

    @Autowired  // 注入要测试的Mapper
    private UserMapper userMapper;

    @Test
    public void testSelectById() {
        // 假设数据库中有id=1的用户
        User user = userMapper.selectById(1L);
        System.out.println(user);

        // 简单断言：验证查询结果不为空
        assert user != null;
    }

    @Test
    public void testSelectByOpenId() {
        // 测试根据openId查询
        User user = userMapper.selectByOpenId("test_openid_123");
        System.out.println(user);
    }
}