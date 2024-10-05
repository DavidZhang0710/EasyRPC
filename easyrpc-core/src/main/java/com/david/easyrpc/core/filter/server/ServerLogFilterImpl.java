package com.david.easyrpc.core.filter.server;

import com.david.easyrpc.core.annotations.SPI;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.filter.IServerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI("before")
public class ServerLogFilterImpl implements IServerFilter {

    private static Logger logger = LoggerFactory.getLogger(ServerLogFilterImpl.class);

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        logger.info(rpcInvocation.getAttachments().get("c_app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}
