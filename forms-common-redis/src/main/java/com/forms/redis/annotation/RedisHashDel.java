package com.forms.redis.annotation;
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
 
@Retention(RetentionPolicy.RUNTIME)    
@Target({ElementType.METHOD})    
public @interface RedisHashDel {    
    public String fieldKey(); //缓存key  
    public String fieldHashKey();
} 