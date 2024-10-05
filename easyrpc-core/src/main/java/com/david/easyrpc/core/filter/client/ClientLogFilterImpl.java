package com.david.easyrpc.core.filter.client;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.filter.IClientFilter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

import static com.david.easyrpc.core.cache.CommonClientCache.CLIENT_CONFIG;

public class ClientLogFilterImpl implements IClientFilter {

    private static Logger logger = LoggerFactory.getLogger(ClientLogFilterImpl.class);

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        rpcInvocation.getAttachments().put("c_app_name",CLIENT_CONFIG.getApplicationName());
        logger.info(rpcInvocation.getAttachments().get("c_app_name")+" do invoke -----> "+rpcInvocation.getTargetServiceName());
    }
}
