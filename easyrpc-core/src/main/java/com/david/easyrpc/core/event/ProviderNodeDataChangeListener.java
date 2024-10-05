package com.david.easyrpc.core.event;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.registry.URL;
import com.david.easyrpc.core.registry.zookeeper.ProviderNodeInfo;

import java.util.List;

import static com.david.easyrpc.core.cache.CommonClientCache.CONNECT_MAP;
import static com.david.easyrpc.core.cache.CommonClientCache.IROUTER;

public class ProviderNodeDataChangeListener implements IRpcListener<IRpcNodeChangeEvent> {

    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            
            String address = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
            if (address.equals(providerNodeInfo.getAddress())) {
                channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
                
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                
                IROUTER.updateWeight(url);
                break;
            }
        }
    }
}
