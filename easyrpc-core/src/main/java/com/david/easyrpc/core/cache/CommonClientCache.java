package com.david.easyrpc.core.cache;


import com.david.easyrpc.core.common.ChannelFuturePollingRef;
import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.config.ClientConfig;
import com.david.easyrpc.core.filter.client.ClientFilterChain;
import com.david.easyrpc.core.registry.URL;
import com.david.easyrpc.core.registry.zookeeper.AbstractRegister;
import com.david.easyrpc.core.router.IRouter;
import com.david.easyrpc.core.serialize.SerializeFactory;
import com.david.easyrpc.core.spi.ExtensionLoader;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CommonClientCache {

    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String,Object> RESP_MAP = new ConcurrentHashMap<>();
    
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    public static Map<String, Map<String,String>> URL_MAP = new ConcurrentHashMap<>();
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    
    
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();
    
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();
    public static IRouter IROUTER;
    public static SerializeFactory CLIENT_SERIALIZE_FACTORY;
    public static ClientConfig CLIENT_CONFIG;
    public static ClientFilterChain CLIENT_FILTER_CHAIN;
    public static AbstractRegister ABSTRACT_REGISTER;
    public static ExtensionLoader EXTENSION_LOADER = new ExtensionLoader();
}
