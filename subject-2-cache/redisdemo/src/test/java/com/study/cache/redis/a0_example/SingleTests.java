package com.study.cache.redis.a0_example;

import com.study.cache.redis.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("single") // 设置profile
public class SingleTests {
    @Autowired
    SingleExampleService exampleService;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // ------- spring redistemplate功能演示
    @Test
    public void setTest() {
        stringRedisTemplate.opsForValue().set("gzy_0217", "2020-3-28");
        System.out.println(stringRedisTemplate.opsForValue().get("gzy_0217"));
    }

    @Test
    public void getTest() throws Exception {
        String userId = "gzy";
        User cachedUser = (User) redisTemplate.opsForValue().get(userId);
        if(cachedUser != null){
            System.out.println(cachedUser);
        }
        User sqlRet = new User("gzy","333");
        redisTemplate.opsForValue().set(userId, sqlRet);


    }


}
