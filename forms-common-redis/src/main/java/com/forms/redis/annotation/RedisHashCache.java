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
public @interface RedisHashCache {    
    public String fieldKey(); //缓存key 
    public String fieldHashKey();
}  