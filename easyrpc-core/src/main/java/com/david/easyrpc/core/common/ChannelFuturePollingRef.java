package com.david.easyrpc.core.common;

import com.david.easyrpc.core.router.Selector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ChannelFuturePollingRef {

    private Map<String, AtomicLong> referenceMap = new ConcurrentHashMap<>();

    public com.david.easyrpc.core.common.ChannelFutureWrapper getChannelFutureWrapper(Selector selector) {
        
        AtomicLong referCount = referenceMap.get(selector.getProviderServiceName());
        if (referCount == null) {
            referCount = new AtomicLong(0);
            referenceMap.put(selector.getProviderServiceName(), referCount);
        }
        com.david.easyrpc.core.common.ChannelFutureWrapper[] arr = selector.getChannelFutureWrappers();
        long i = referCount.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }
}
