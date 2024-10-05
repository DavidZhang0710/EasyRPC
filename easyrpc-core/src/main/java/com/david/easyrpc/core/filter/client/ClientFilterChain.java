package com.david.easyrpc.core.filter.client;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.filter.IClientFilter;

import java.util.ArrayList;
import java.util.List;

public class ClientFilterChain {

    private static List<IClientFilter> iClientFilterList = new ArrayList<>();

    public void addClientFilter(IClientFilter iClientFilter) {
        iClientFilterList.add(iClientFilter);
    }

    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        for (IClientFilter iClientFilter : iClientFilterList) {
            iClientFilter.doFilter(src, rpcInvocation);
        }
    }
}
