package com.david.easyrpc.core.client;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.registry.URL;
import com.david.easyrpc.core.registry.zookeeper.ProviderNodeInfo;
import com.david.easyrpc.core.common.CommonUtils;
import com.david.easyrpc.core.router.Selector;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.david.easyrpc.core.cache.CommonClientCache.*;

public class ConnectionHandler {

    private static Bootstrap bootstrap;

    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionHandler.bootstrap = bootstrap;
    }

    public static void connect(String providerServiceName, String providerIp) throws InterruptedException {
        if (bootstrap == null) {
            throw new RuntimeException("bootstrap cannot be null");
        }
        if (!providerIp.contains(":")) {
            return;
        }
        String[] providerAddress = providerIp.split(":");
        String ip = providerAddress[0];
        Integer port = Integer.valueOf(providerAddress[1]);
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        String providerURLInfo = URL_MAP.get(providerServiceName).get(providerIp);
        ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(providerURLInfo);
        System.out.println(providerURLInfo);
        ChannelFutureWrapper wrapper = new ChannelFutureWrapper();
        wrapper.setChannelFuture(channelFuture);
        wrapper.setHost(ip);
        wrapper.setPort(port);
        wrapper.setWeight(providerNodeInfo.getWeight());
        wrapper.setGroup(providerNodeInfo.getGroup());
        SERVER_ADDRESS.add(providerIp);
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(providerServiceName);
        if (CommonUtils.isEmptyList(channelFutureWrappers)) {
            channelFutureWrappers = new ArrayList<>();
        }
        channelFutureWrappers.add(wrapper);
        
        CONNECT_MAP.put(providerServiceName, channelFutureWrappers);
        Selector selector = new Selector();
        selector.setProviderServiceName(providerServiceName);
        IROUTER.refreshRouteArr(selector);
    }

    public static ChannelFuture createChannelFuture(String ip, Integer port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        return channelFuture;
    }

    public static void disConnect(String providerServiceName, String providerIp) {
        SERVER_ADDRESS.remove(providerIp);
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(providerServiceName);
        if (CommonUtils.isNotEmptyList(channelFutureWrappers)) {
            Iterator<ChannelFutureWrapper> iterator = channelFutureWrappers.iterator();
            while (iterator.hasNext()) {
                ChannelFutureWrapper channelFutureWrapper = iterator.next();
                if (providerIp.equals(channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort())) {
                    iterator.remove();
                }
            }
        }
    }

    public static ChannelFuture getChannelFuture(RpcInvocation rpcInvocation) {
        String providerServiceName = rpcInvocation.getTargetServiceName();
        ChannelFutureWrapper[] channelFutureWrappers = SERVICE_ROUTER_MAP.get(providerServiceName);
        if (channelFutureWrappers == null || channelFutureWrappers.length == 0) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setE(new RuntimeException("no provider exist for " + providerServiceName));
            rpcInvocation.setResponse(null);
            
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            return null;
        }
        List<ChannelFutureWrapper> channelFutureWrappersList = new ArrayList<>(channelFutureWrappers.length);
        for (int i = 0; i < channelFutureWrappers.length; i++) {
            channelFutureWrappersList.add(channelFutureWrappers[i]);
        }
        CLIENT_FILTER_CHAIN.doFilter(channelFutureWrappersList, rpcInvocation);
        Selector selector = new Selector();
        selector.setProviderServiceName(providerServiceName);
        selector.setChannelFutureWrappers(channelFutureWrappers);
        ChannelFuture channelFuture = IROUTER.select(selector).getChannelFuture();
        return channelFuture;
    }
}
