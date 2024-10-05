package com.david.easyrpc.core.router;

import com.david.easyrpc.core.common.ChannelFutureWrapper;
import com.david.easyrpc.core.registry.URL;

public interface IRouter {

    void refreshRouteArr(Selector selector);

    ChannelFutureWrapper select(Selector selector);

    void updateWeight(URL url);
}
