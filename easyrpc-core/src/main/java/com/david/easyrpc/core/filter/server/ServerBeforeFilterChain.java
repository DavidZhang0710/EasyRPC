package com.david.easyrpc.core.filter.server;

import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.filter.IServerFilter;

import java.util.ArrayList;
import java.util.List;

public class ServerBeforeFilterChain {

    private static List<IServerFilter> iServerFilters = new ArrayList<>();

    public void addServerFilter(IServerFilter iServerFilter) {
        iServerFilters.add(iServerFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (IServerFilter iServerFilter : iServerFilters) {
            iServerFilter.doFilter(rpcInvocation);
        }
    }
}
