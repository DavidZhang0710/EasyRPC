package com.david.easyrpc.core.serialize;

import com.alibaba.fastjson.JSON;
import com.david.easyrpc.core.serialize.SerializeFactory;

public class FastJsonSerializeFactory implements SerializeFactory {

    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data), clazz);
    }
}
