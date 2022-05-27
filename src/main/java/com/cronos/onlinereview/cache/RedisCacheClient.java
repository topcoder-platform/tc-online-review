package com.cronos.onlinereview.cache;

import com.cronos.onlinereview.ConfigurationException;
import com.topcoder.onlinereview.component.cache.CacheAddress;
import com.topcoder.onlinereview.component.cache.CacheClient;
import com.topcoder.onlinereview.component.cache.MaxAge;
import com.topcoder.onlinereview.component.cache.TCCacheException;
import com.topcoder.onlinereview.component.webcommon.TCResourceBundle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

/**
 * This class implements the CacheClient and uses the Redis as the back-end cache.
 *
 * This class is thread-safe because it's immutable and interacts with the Redis
 * through the JedisPool, which is thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class RedisCacheClient implements CacheClient {
    /**
     * The resource bundle to load the properties.
     */
    private static final TCResourceBundle bundle = new TCResourceBundle("cache");

    /**
     * The Redis key prefix
     */
    private final static String REDIS_KEY_PREFIX = "tccache/";

    /**
     * The Redis key pattern
     */
    private final static byte[] REDIS_KEY_PATTERN = "tccache/*".getBytes();

    /**
     * The JedisPool instance to obtain the connection to Redis.
     */
    private static JedisPool jedisPool;

    /**
     * The constructor to initialize the Jedis.
     */
    public RedisCacheClient() {
    }

    static {
        // init pool config
        JedisPoolConfig config = new JedisPoolConfig();

        int maxTotal = bundle.getIntProperty("redis.maxTotalConnections", 10);
        int maxIdle = bundle.getIntProperty("redis.maxIdleConnections", 10);
        int minIdle = bundle.getIntProperty("redis.minIdleConnections", 5);
        String testOnBorrow = bundle.getProperty("redis.testOnBorrow", "true");
        String blockWhenExhausted = bundle.getProperty("redis.blockWhenExhausted", "false");

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
        config.setBlockWhenExhausted(Boolean.parseBoolean(blockWhenExhausted));

        String redisURI = bundle.getProperty("redis.uri");
        int redisTimeout = bundle.getIntProperty("redis.timeout", 2000);

        // init pool
        try {
            jedisPool = new JedisPool(config, new URI(redisURI), redisTimeout);
        } catch (URISyntaxException e) {
            throw new ConfigurationException("The configured redis URI is invalid", e);
        }
    }


    /**
     * Set the key-value to the cache.
     *
     * @param key the key
     * @param value the value
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public void set(String key, Object value) throws TCCacheException {
        set(key, value, MaxAge.MAX);
    }

    /**
     * Set the key-value to the cache with the maxAge (expiration).
     *
     * @param key the key
     * @param value the value
     * @param maxAge the expiration
     * @throws TCCacheException if any error occurs.
     */
    private void set(String key, Object value, MaxAge maxAge) throws TCCacheException {
        Jedis jedis = null;
        try {
            byte[] valueBytes = serialize(value);
            jedis = jedisPool.getResource();

            jedis.psetex(getCacheKey(key), (long) maxAge.age(), valueBytes);
        } catch (IOException e) {
            throw new TCCacheException("Fail to serialize the value object", e);
        } catch (JedisException e) {
            throw new TCCacheException("Error occurs when interacting with redis", e);
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * Set the key-value to the cache.
     *
     * @param address the adress to generate the key
     * @param value the value
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public void set(CacheAddress address, Object value) throws TCCacheException {
        set(address.getKey(), value);
    }

    /**
     * Set the key-value to the cache with the maxAge (expiration).
     *
     * @param address the adress to generate the key
     * @param value the value
     * @param maxAge the expiration
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public void set(CacheAddress address, Object value, MaxAge maxAge) throws TCCacheException {
        set(address.getKey(), value, maxAge);
    }

    /**
     * Get the value by the key from the cache.
     *
     * @param key the key
     * @return the value
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public Object get(String key) throws TCCacheException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return getCacheObject(jedis, getCacheKey(key));
        } catch (JedisException e) {
            throw new TCCacheException("Error occurs when interacting with redis", e);
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * Get the value by the key from the cache.
     *
     * @param jedis the Jedis instance
     * @param cacheKey the cache key
     * @return the value
     * @throws TCCacheException if any error occurs.
     */
    private static Object getCacheObject(Jedis jedis, byte[] cacheKey) throws TCCacheException {
        byte[] valueBytes = jedis.get(cacheKey);

        if (valueBytes == null) {
            return null;
        }

        try {
            return deserialize(valueBytes);
        } catch (ClassNotFoundException e) {
            throw new TCCacheException("Fail to deserialize the object", e);
        } catch (IOException e) {
            throw new TCCacheException("Fail to deserialize the object", e);
        }
    }

    /**
     * Get the value by the key from the cache.
     *
     * @param address the address to generate the key
     * @return the value
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public Object get(CacheAddress address) throws TCCacheException {
        return get(address.getKey());
    }

    /**
     * Remove the value by the key from the cache.
     *
     * @param key the key
     * @return the removed value
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public Object remove(String key) throws TCCacheException {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] cacheKey = getCacheKey(key);

            Object value = getCacheObject(jedis, cacheKey);
            jedis.del(cacheKey);

            return value;
        } catch (JedisException e) {
            throw new TCCacheException("Error occurs when interacting with redis", e);
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * Remove the value by the key from the cache.
     *
     * @param address the address to generate the key
     * @return the removed value
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public Object remove(CacheAddress address) throws TCCacheException {
        return remove(address.getKey());
    }

    /**
     * It will clear all the key-value pairs set by this library.
     *
     * @throws TCCacheException if any error occurs.
     */
    @Override
    public void clearCache() throws TCCacheException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<byte[]> redisKeys = jedis.keys(REDIS_KEY_PATTERN);

            if (! redisKeys.isEmpty()) {
                jedis.del(redisKeys.toArray(new byte[][]{}));
            }
        } catch (JedisException e) {
            throw new TCCacheException("Error occurs when interacting with redis", e);
        } finally {
            closeJedis(jedis);
        }
    }

//    @Override
    public void close() {
        this.jedisPool.close();
    }

    /**
     * Get the cache key.
     *
     * @param key the original key
     * @return the cache key
     */
    private static byte[] getCacheKey(String key) {
        return (REDIS_KEY_PREFIX + key).getBytes();
    }

    /**
     * Close the Jedis instance.
     *
     * @param jedis the Jedis instance.
     */
    private static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Throwable t) {
                // ignore
            }
        }
    }

    /**
     * Serialize the object into an byte array.
     *
     * @param obj the object to serialize.
     * @return the serialized byte array
     * @throws IOException if any i/o error occurs
     */
    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.close();

        return bout.toByteArray();
    }

    /**
     * Deserialize the byte array into an Object.
     *
     * @param data the byte array
     * @return the deserialized Object
     *
     * @throws IOException if any i/o error occurs
     * @throws ClassNotFoundException if the Object's class doesn't exist.
     */
    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return in.readObject();
    }


}
