package com.forms.redis.aspect;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.forms.common.util.MLog;
import com.forms.redis.annotation.RedisCache;
import com.forms.redis.annotation.RedisDel;
import com.forms.redis.annotation.RedisHashCache;
import com.forms.redis.annotation.RedisHashDel;
import com.forms.redis.annotation.RedisSet;
import com.forms.redis.util.RedisCacheUtil;

@Component
@Aspect
public class CacheAspect {

	@Autowired
	private RedisCacheUtil redisTools;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 获取或添加缓存
	 * 
	 * @param jp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.forms.redis.annotation.RedisCache)")
	public Object RedisCache(final ProceedingJoinPoint jp) throws Throwable {
		Object[] args = jp.getArgs();
		try {
			Method method = getMethod(jp);
			final RedisCache cache = method.getAnnotation(RedisCache.class);
			final String key = parseKey(cache.fieldKey(), method, jp.getArgs());
			Class<?> returnType = ((MethodSignature) jp.getSignature()).getReturnType();
			String json = redisTools.get(key);
			if (StringUtils.isNotEmpty(json)) {
				MLog.info("[redis:] " + key);
				return JSON.parseObject(json, returnType);
			} else {
				synchronized (this) {
					json = redisTools.get(key);
					if (StringUtils.isNotEmpty(json)) {
						MLog.info("[redis:] " + key);
						return JSON.parseObject(json, returnType);
					}
					final Object result = jp.proceed(args);
					if (result != null) {
						redisTools.set(key, JSON.toJSONString(result), cache.expire());
					}
					return result;
				}
			}
		} catch (Exception e) {
			MLog.info("缓存错误" + e.getMessage(), e);
		}
		return null;

	}

	@Around("@annotation(com.forms.redis.annotation.RedisHashCache)")
	public Object RedisHashCache(final ProceedingJoinPoint jp) throws Throwable {
		Object[] args = jp.getArgs();
		try {
			Method method = getMethod(jp);
			final RedisHashCache cache = method.getAnnotation(RedisHashCache.class);
			final String key = parseKey(cache.fieldKey(), method, args);
			final String hashKey = parseKey(cache.fieldHashKey(), method, args);
			Class<?> returnType = ((MethodSignature) jp.getSignature()).getReturnType();
			String json = redisTools.getHashKey(key, hashKey);
			if (StringUtils.isNotEmpty(json)) {
				MLog.info("[redis:] " + key + "==" + hashKey);
				return JSON.parseObject(json, returnType);
			} else {
				synchronized (this) {
					json = redisTools.getHashKey(key, hashKey);
					if (StringUtils.isNotEmpty(json)) {
						MLog.info("[redis:] " + key + "==" + hashKey);
						return JSON.parseObject(json, returnType);
					}
					final Object result = jp.proceed(args);
					if (result != null) {
						redisTools.putHashKey(key, hashKey, JSON.toJSONString(result));
						// , cache.expire()
					}
					return result;
				}
			}
		} catch (Exception e) {
			MLog.info("缓存错误" + e.getMessage(), e);
		}
		return null;

	}

	/**
	 * 删除缓存
	 * 
	 * @param jp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.forms.redis.annotation.RedisHashDel)")
	public Object RedisHashDel(final ProceedingJoinPoint jp) throws Throwable {
		Object[] args = jp.getArgs();
		Method method = getMethod(jp);
		RedisHashDel cache = method.getAnnotation(RedisHashDel.class);
		final String key = parseKey(cache.fieldKey(), method, args);
		final String fieldHashKey = parseKey(cache.fieldHashKey(), method, args);
		redisTools.delHashKey(key,fieldHashKey);
		return jp.proceed(jp.getArgs());
	}
	
	/**
	 * 删除缓存
	 * 
	 * @param jp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.forms.redis.annotation.RedisDel)")
	public Object RedisDel(final ProceedingJoinPoint jp) throws Throwable {
		@SuppressWarnings("unused")
		Object[] args = jp.getArgs();
		Method method = getMethod(jp);
		RedisDel cache = method.getAnnotation(RedisDel.class);
		final String key = parseKey(cache.fieldKey(), method, jp.getArgs());
		redisTools.delete(key);
		return jp.proceed(jp.getArgs());
	}

	/**
	 * 分布式锁
	 * 
	 * @param jp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	// @Around("@annotation(com.forms.redis.annotation.RedisLock)")
	// public Object RedisLock(final ProceedingJoinPoint jp) throws Throwable {
	// Object[] args = jp.getArgs();
	// boolean redisStartOn =
	// Boolean.parseBoolean(MValue.getParam("redis.startOn"));
	// if (!redisStartOn) {
	// return jp.proceed(args);
	// }
	// Method method = getMethod(jp);
	// RedisLock cache = method.getAnnotation(RedisLock.class);
	// JedisLock lock = new
	// JedisLock(RedisCline.getInstance().getJedisResource(), cache.fieldKey(),
	// cache.timeout(), cache.expire());
	// if (lock.acquire()) {
	// try {
	// return jp.proceed(jp.getArgs());
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// lock.release();// 则解锁
	// }
	// }
	// throw new Throwable("lock is timeOut");
	// }

	/**
	 * 强制更新缓存
	 * 
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.forms.redis.annotation.RedisSet)")
	public Object RedisSet(final ProceedingJoinPoint jp) throws Throwable {
		Object[] args = jp.getArgs();
		try {
			Method method = getMethod(jp);
			RedisSet cache = method.getAnnotation(RedisSet.class);
			final String key = parseKey(cache.fieldKey(), method, jp.getArgs());
			Object result = jp.proceed(args);
			if (result != null) {
				redisTools.set(key, JSON.toJSONString(result), cache.expire());
			}
			return result;
		} catch (Exception e) {
			MLog.info("缓存错误" + e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private Method getMethod(ProceedingJoinPoint pjp) {
		// 获取参数的类型
		Object[] args = pjp.getArgs();
		Class[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Method method = null;
		try {
			method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return method;

	}

	/**
	 * 获取缓存的key key 定义在注解上，支持SPEL表达式
	 * 
	 * @param pjp
	 * @return
	 */
	private String parseKey(String key, Method method, Object[] args) {
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(key).getValue(context, String.class);
	}
}