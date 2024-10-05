package com.david.easyrpc.core.filter.server;

import com.david.easyrpc.core.annotations.SPI;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.common.ServerServiceSemaphoreWrapper;
import com.david.easyrpc.core.filter.IServerFilter;

import static com.david.easyrpc.core.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

@SPI("after")
public class ServerServiceAfterLimitFilterImpl implements IServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        serverServiceSemaphoreWrapper.getSemaphore().release();
    }
}
