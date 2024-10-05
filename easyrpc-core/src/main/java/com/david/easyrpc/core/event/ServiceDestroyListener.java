package com.david.easyrpc.core.event;

import com.david.easyrpc.core.registry.URL;

import static com.david.easyrpc.core.cache.CommonServerCache.PROVIDER_URL_SET;
import static com.david.easyrpc.core.cache.CommonServerCache.REGISTRY_SERVICE;

public class ServiceDestroyListener implements IRpcListener<IRpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}
