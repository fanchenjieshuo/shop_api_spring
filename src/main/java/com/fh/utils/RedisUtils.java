package com.fh.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//redis的连接池
public class RedisUtils {
    //从静态块中初始化
    private static JedisPool jedisPool;

    private RedisUtils(){

    }

    //static  静态块
    static {
        //创建redis池的配置
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(30);
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setMaxWaitMillis(30000);
        //初始化redis池
        jedisPool=new JedisPool(jedisPoolConfig,"47.93.196.77",6379);
    }
    //从池中拿连接
    public static Jedis getJedis(){
        return jedisPool.getResource();
    };
    //连接用还给池中
    public static void returnJedis(Jedis jedis){
        jedisPool.returnResource(jedis);
    }

}
