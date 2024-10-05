package com.david.easyrpc.core.filter;

import com.david.easyrpc.core.common.RpcInvocation;

public interface IServerFilter extends IFilter {

    void doFilter(RpcInvocation rpcInvocation);
}
