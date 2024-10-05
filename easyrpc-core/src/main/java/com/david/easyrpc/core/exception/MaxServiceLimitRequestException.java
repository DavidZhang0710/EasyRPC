package com.david.easyrpc.core.exception;

import com.david.easyrpc.core.common.RpcInvocation;

public class MaxServiceLimitRequestException extends IRpcException {

    public MaxServiceLimitRequestException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }
}
