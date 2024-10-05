package com.david.easyrpc.core.registry.zookeeper;

import com.david.easyrpc.core.registry.RegistryService;
import com.david.easyrpc.core.registry.URL;

import java.util.List;
import java.util.Map;

import static com.david.easyrpc.core.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static com.david.easyrpc.core.cache.CommonServerCache.PROVIDER_URL_SET;

public abstract class AbstractRegister implements RegistryService {

    @Override
    public void register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url);
    }

    @Override
    public void unSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }

    
    public abstract void doAfterSubscribe(URL url);

    public abstract void doBeforeSubscribe(URL url);

    public abstract List<String> getProviderIps(String serviceName);

    public abstract Map<String, String> getServiceWeightMap(String serviceName);
}
