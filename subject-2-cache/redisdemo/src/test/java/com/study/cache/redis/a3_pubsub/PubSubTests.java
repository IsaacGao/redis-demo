package com.study.cache.redis.a3_pubsub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("pubsub") // 设置profile
public class PubSubTests {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test1() throws InterruptedException {
        System.out.println("开始测试发布订阅机制，5秒后发布一条消息");
        Thread.sleep(5000L);
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                // 发送通知
                Long received = connection.publish(PubsubRedisAppConfig.TEST_CHANNEL_NAME.getBytes(), "{手机号码10086~短信内容~~}".getBytes());
                return received;
            }
        });
    }

    /**
     * 隐藏功能~~黑科技~~当key被删除，或者key过期之后，也会有通知~
     * http://redis.cn/topics/notifications.html
     * https://redis.io/topics/notifications
     * 可以订阅事件或者key
     * 可以用来做订单超时未支付的实现
     * - 监听删除消息可以知道具体的订单未支付
     *
     *  __keyspace@0  @0 是数据库下标 可以通过 select index 选择
     */
    @Test
    public void test2() throws InterruptedException {
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.subscribe((message, pattern) -> {
                    System.out.println("收到消息，使用redisTemplate收到的：" + message);
                }, "__keyevent@0__:del".getBytes());
                return null;
            }
        });

        redisTemplate.opsForValue().set("hkkkk", "tony");
        Thread.sleep(1000L);
        redisTemplate.delete("hkkkk");
    }
}
