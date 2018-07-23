package com.forms.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RedisLock {
	public String fieldKey(); // 缓存key

	public int timeout() default 10000; // 锁超时

	public int expire() default 60000; // 锁失效
}