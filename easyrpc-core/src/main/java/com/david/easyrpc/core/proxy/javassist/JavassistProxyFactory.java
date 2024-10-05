package com.david.easyrpc.core.proxy.javassist;


import com.david.easyrpc.core.client.RpcReferenceWrapper;
import com.david.easyrpc.core.proxy.ProxyFactory;

public class JavassistProxyFactory implements ProxyFactory {

    public JavassistProxyFactory() {
    }

    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                rpcReferenceWrapper.getAimClass(), new JavassistInvocationHandler(rpcReferenceWrapper));
    }
}
