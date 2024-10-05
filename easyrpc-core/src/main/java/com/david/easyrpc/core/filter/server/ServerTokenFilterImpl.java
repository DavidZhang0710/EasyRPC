package com.david.easyrpc.core.filter.server;

import com.david.easyrpc.core.annotations.SPI;
import com.david.easyrpc.core.common.CommonUtils;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.exception.IRpcException;
import com.david.easyrpc.core.filter.IServerFilter;
import com.david.easyrpc.core.server.ServiceWrapper;
import com.david.easyrpc.core.common.CommonUtils;

import static com.david.easyrpc.core.cache.CommonClientCache.RESP_MAP;
import static com.david.easyrpc.core.cache.CommonServerCache.PROVIDER_SERVICE_WRAPPER_MAP;

@SPI("before")
public class ServerTokenFilterImpl implements IServerFilter {

    
    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String token = String.valueOf(rpcInvocation.getAttachments().get("serviceToken"));
        ServiceWrapper serviceWrapper = PROVIDER_SERVICE_WRAPPER_MAP.get(rpcInvocation.getTargetServiceName());
        String matchToken = String.valueOf(serviceWrapper.getServiceToken());
        if (CommonUtils.isEmpty(matchToken)) return;
        if (!CommonUtils.isEmpty(token) && token.equals(matchToken)) return;
        rpcInvocation.setRetry(0);
        rpcInvocation.setE(new RuntimeException("service token is illegal for service " + rpcInvocation.getTargetServiceName()));
        rpcInvocation.setResponse(null);
        
        RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
        throw new IRpcException(rpcInvocation);
    }
}
