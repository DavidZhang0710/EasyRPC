package com.david.easyrpc.core.proxy;

import com.david.easyrpc.core.client.RpcReferenceWrapper;

public interface ProxyFactory {

    <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;
}
