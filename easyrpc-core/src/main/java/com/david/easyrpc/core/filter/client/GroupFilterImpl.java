package com.david.easyrpc.core.filter.client;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.common.CommonUtils;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.filter.IClientFilter;
import com.david.easyrpc.core.common.CommonUtils;

import java.util.Iterator;
import java.util.List;

import static com.david.easyrpc.core.cache.CommonClientCache.RESP_MAP;

public class GroupFilterImpl implements IClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String group = String.valueOf(rpcInvocation.getAttachments().get("group"));
        Iterator<ChannelFutureWrapper> channelFutureWrapperIterator = src.iterator();
        while (channelFutureWrapperIterator.hasNext()){
            ChannelFutureWrapper channelFutureWrapper = channelFutureWrapperIterator.next();
            if (!channelFutureWrapper.getGroup().equals(group)) {
                channelFutureWrapperIterator.remove();
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setE(new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in group " + group));
            rpcInvocation.setResponse(null);
            
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            throw new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in group " + group);
        }
    }
}
