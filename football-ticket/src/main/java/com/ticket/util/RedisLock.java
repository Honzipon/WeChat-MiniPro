package com.ticket.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 尝试获取锁
     * @param key 锁键
     * @param waitTime 等待时间(ms)
     * @param leaseTime 自动释放时间(ms)
     * @return 是否获取成功
     */
    public boolean tryLock(String key, long waitTime, long leaseTime) {
        long start = System.currentTimeMillis();
        String value = Thread.currentThread().getId() + "-" + System.nanoTime();
        while (System.currentTimeMillis() - start < waitTime) {
            Boolean success = redisTemplate.opsForValue()
                    .setIfAbsent(key, value, leaseTime, TimeUnit.MILLISECONDS);
            if (Boolean.TRUE.equals(success)) {
                return true;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
