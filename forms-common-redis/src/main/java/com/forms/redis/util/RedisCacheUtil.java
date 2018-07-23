package com.forms.redis.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.forms.common.util.MLog;

@Service("redisCacheUtil")
public class RedisCacheUtil {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Resource(name = "redisTemplate")
	private SetOperations<String, Object> setOps;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Object> hashOps;

	private static String redisCode = "utf-8";

	/**
	 * 删除key值的所有数据，包括列表，键值对，hash等
	 * 
	 * @param key
	 */
	public void delete(String key) {
		redisTemplate.delete(key);

	}

	public void deleteKeys(Collection<String> keys) {
		redisTemplate.delete(keys);

	}

	public void set(final byte[] key, final byte[] value, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	/**
	 * 设置key,value对
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value, long liveTime) {
		set(key.getBytes(), value.getBytes(), liveTime);
	}

	public void set(String key, String value) {
		this.set(key, value, 0L);
	}

	/**
	 * redis incr的使用，将键的整数值增加
	 * 
	 * @param key
	 * @param step
	 * @return
	 */
	public long setIncr(final String key, long step) {
		return redisTemplate.opsForValue().increment(key, step);
	}

	/**
	 * 根据key获取value
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					byte[] value = connection.get(key.getBytes());
					if (value == null) {
						return null;
					}
					return new String(value, redisCode);
				} catch (Exception e) {
					MLog.info(e.getMessage(), e);
				}
				return null;
			}
		});
	}

	public Set<String> getKeys(final String key) {
		return redisTemplate.keys(key);
	}

	public long getIncrValue(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
				byte[] rowkey = serializer.serialize(key);
				byte[] rowval = connection.get(rowkey);
				try {
					String val = serializer.deserialize(rowval);
					return Long.parseLong(val);
				} catch (Exception e) {
					return 0L;
				}
			}
		});
	}

	public void expire(final String key, final long timeout, final TimeUnit unit) {
		redisTemplate.expire(key, timeout, unit);
	}

	/**
	 * 向列表头部添加值
	 * 
	 * @param key
	 * @param value
	 */
	public void addListFromHeader(String key, Object obj) {
		redisTemplate.opsForList().leftPush(key, obj);
	}

	/**
	 * 添加列表
	 * 
	 * @param key
	 * @param value
	 */
	public void addList(String key, Object value) {
		redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 获取列表数据
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Object listPop(String key, int start, int end) {
		List<Object> objs = redisTemplate.opsForList().range(key, start, end);
		return objs;
	}

	/**
	 * 获取列表长度
	 * 
	 * @param key
	 * @return
	 */
	public Long getListLength(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 获取列表最后一位值,并删除
	 * 
	 * @param key
	 * @return
	 */
	public Object popListLast(String key) {
		return redisTemplate.opsForList().rightPop(key);
	}

	/**
	 * 删除列表某一位值
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public Long delList(String key, long i, Object value) {
		return redisTemplate.opsForList().remove(key, i, value);
	}

	/**
	 * 获取列表第一位，并删除
	 * 
	 * @param key
	 * @return
	 */
	public Object popListFirst(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 设置某一位值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void setListByIndex(String key, int index, Object value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 向列表尾部追加一组数据
	 * 
	 * @param key
	 * @param values
	 */
	public void addListBundle(String key, List<?> values) {
		redisTemplate.opsForList().rightPushAll(key, values);
	}

	/**
	 * 向列表头部追加一组数据
	 * 
	 * @param key
	 * @param values
	 */
	public void addListBundleFromHeader(String key, List<Object> values) {
		redisTemplate.opsForList().leftPushAll(key, values);
	}

	/**
	 * 获取列表某一位值
	 */
	public Object getListByIndex(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 
	 * 功能描述: (Set集合)将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。<br>
	 * 〈功能详细描述〉
	 *
	 * @param key
	 * @param values
	 * @since [产品/模块版本](可选)
	 */
	public void addSet(String key, Object... values) {
		setOps.add(key, values);

	}

	/**
	 * 
	 * 功能描述:(Set集合)移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 <br>
	 * 〈功能详细描述〉
	 *
	 * @param key
	 * @param values
	 * @since [产品/模块版本](可选)
	 */
	public void removeSet(String key, Object... values) {
		setOps.remove(key, values);
	}

	/**
	 * 
	 * 功能描述:(Set集合)返回集合 key 中的所有成员 <br>
	 * 〈功能详细描述〉
	 *
	 * @param key
	 * @return
	 * @since [产品/模块版本](可选)
	 */
	public Set<Object> getMembers(String key) {
		Set<Object> members = setOps.members(key);
		return members;
	}

	/**
	 * set Hash列表值(String,Long)
	 */
	public void setAllHash(String key, Map<String, String> m) {
		hashOps.putAll(key, m);
	}

	/**
	 * set Object对象到hash表,字段必须为基本数据类型或包装类,已经时间和String类型
	 */
	public void setObjectToHash(String key, Object obj) {
		try {
			Map<String, Object> stringMap = ObjectUtils.objectToMap(obj);
			hashOps.putAll(key, stringMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * get Object对象到 根据hashKey;
	 */
	public <T> T getHashToObjectBykey(String key, Class<T> t) {
		Map<String, Object> entries = hashOps.entries(key);
		try {
			return ObjectUtils.mapToObject(entries, t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * get Hash列表值
	 */
	public Map<String, Object> getAllHash(String key) {
		return hashOps.entries(key);
	}

	/**
	 * 判断某个key是否存在
	 */
	public Boolean isExist(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 向hash指定key加一
	 */
	public void incrHash(String key, String hashKey, Integer i) {
		hashOps.increment(key, hashKey, i);
	}

	/**
	 * 增加hash字段
	 */
	public void putHashKey(String key, String hashKey, String value) {
		hashOps.put(key, hashKey, value);
	}

	/**
	 * 获取hash字段
	 */
	public String getHashKey(String key, String hashKey) {
		return (String) hashOps.get(key, hashKey);
	}

	/**
	 * 删除hash字段
	 */
	public void delHashKey(String key, String hashKey) {
		hashOps.delete(key, hashKey);
	}

	// 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间
	public long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	/**
	 * 增加hash字段
	 */
	public void addHashKey(String key, String hashKey, Object value) {
		hashOps.put(key, hashKey, value);
	}

	/**
	 * 获取hash字段
	 */
	public Object getHashValse(String key, String hashKey) {
		return hashOps.get(key, hashKey);
	}

	// redisTemplate操作 zset的一些 封装;

	/**
	 * 给指定的集合添加 字段和分数
	 */
	public void addZset(String key, Object var, double score) {
		redisTemplate.opsForZSet().add(key, var, score);
	}

	/**
	 * 批量添加, 实现了ZSetOperations.TypedTuple<>接口的的set集合 对象内要有getVule,和getSorce方法;
	 * 
	 * @param key
	 * @param var2
	 */
	public void addZset(String key, Set<ZSetOperations.TypedTuple<Object>> var2) {
		redisTemplate.opsForZSet().add(key, var2);
	}

	/**
	 * 给指定的集合移除字段
	 */
	public void removeZset(String key, Object var) {
		redisTemplate.opsForZSet().remove(key, var);
	}

	/**
	 * 获取排序后的结果
	 */
	public Set<Object> reverseRange(final String key, final long start, final long end) {
		Set<Object> objects = redisTemplate.opsForZSet().reverseRange(key, start, end);
		return objects;
	}

}