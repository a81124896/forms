package com.forms.redis.annotation;
  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/** 
 * 缓存注解 
 *  
 *  
 */  
@Retention(RetentionPolicy.RUNTIME)    
@Target({ElementType.METHOD})    
public @interface RedisCache {    
    public String fieldKey(); //缓存key    
    public int expire() default 60;      //缓存多少秒,默认无限期    
}  