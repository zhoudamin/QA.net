package com.zhoudm.async;

import com.alibaba.fastjson.JSONObject;
import com.zhoudm.util.JedisAdapter;
import com.zhoudm.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhoudm on 2016/7/30.
 */
@Service
//事件入口，统一发事件
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    //把eventModel发出去，其实就是保存在队列里面
    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);  //把对象转换成字符串
            String key = RedisKeyUtil.getEventQueueKey();     //
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
