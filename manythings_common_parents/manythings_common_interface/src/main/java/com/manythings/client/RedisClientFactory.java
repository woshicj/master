package com.manythings.client;
import feign.hystrix.FallbackFactory;
public class RedisClientFactory implements FallbackFactory {
    public Object create(Throwable throwable) {
        /**
         *  在服务不可用的时候自动调用fallback的处理方法
         *  但是如果想知道错误原因 可以自定义FallbackFactory
         *
         *
         * */
        return new RedisClient(){


            public void set(String key, String value) {

            }

            public String get(String key) {
                return null;
            }
        };
    }
}
