package com.david.easyrpc.core.router;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.registry.URL;

import java.util.List;

import static com.david.easyrpc.core.cache.CommonClientCache.*;

public class RotateRouterImpl implements IRouter {

    @Override
    public void refreshRouteArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for (int i = 0; i < channelFutureWrappers.size(); i++) {
            arr[i] = channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector);
    }

    @Override
    public void updateWeight(URL url) {

    }
}
