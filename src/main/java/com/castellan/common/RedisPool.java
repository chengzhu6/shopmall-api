package com.castellan.common;

import com.castellan.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static JedisPool jedisPool;


    // 最大连接数
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.max.total");

    // 最大空闲数
    private static Integer maxIdle = PropertiesUtil.getIntegerProperty("redis.max.idle");

    // 最小空闲数
    private static Integer minIdle = PropertiesUtil.getIntegerProperty("redis.min.idle");

    // test on borrow
    private static Boolean testOnBorrow = PropertiesUtil.getBooleanProperty("redis.test.borrow");

    // test on return
    private static Boolean testOnReturn = PropertiesUtil.getBooleanProperty("redis.test.return");

    // redis ip
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");

    // redis port
    private static Integer redisPort = PropertiesUtil.getIntegerProperty("redis.port");

    private static void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);

        jedisPool = new JedisPool(config,redisIp,redisPort,1000*2);
    }

    static {
        init();
    }

    public static Jedis getJedis(){
        return jedisPool.getResource();
    }


    public static void closeJedis(Jedis jedis) {
        jedis.close();
    }


    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        jedis.set("aaa", "bbbb");
        RedisPool.closeJedis(jedis);
    }



}
