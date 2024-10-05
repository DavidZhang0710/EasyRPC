package com.david.easyrpc.core.filter.client;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.common.CommonUtils;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.filter.IClientFilter;
import com.david.easyrpc.core.common.CommonUtils;

import java.util.Iterator;
import java.util.List;

import static com.david.easyrpc.core.cache.CommonClientCache.RESP_MAP;

public class DirectInvokeFilterImpl implements IClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String url = (String) rpcInvocation.getAttachments().get("url");
        if (CommonUtils.isEmpty(url)) return;
        Iterator<ChannelFutureWrapper> iterator = src.iterator();
        while (iterator.hasNext()) {
            ChannelFutureWrapper next = iterator.next();
            if (!(next.getHost() + ":" + next.getPort()).equals(url)) {
                iterator.remove();
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setE(new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in url " + url));
            rpcInvocation.setResponse(null);
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            throw new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in url " + url);
        }
    }
}
