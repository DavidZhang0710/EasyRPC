package com.david.easyrpc.core.event;

import java.util.List;
import java.util.Map;

public class URLChangeWrapper {

    private String serviceName;

    private List<String> providerUrl;

    
    private Map<String,String> nodeDataUrl;

    @Override
    public String toString() {
        return "URLChangeWrapper{" +
                "serviceName='" + serviceName + '\'' +
                ", providerUrl=" + providerUrl +
                '}';
    }

    public Map<String, String> getNodeDataUrl() {
        return nodeDataUrl;
    }

    public void setNodeDataUrl(Map<String, String> nodeDataUrl) {
        this.nodeDataUrl = nodeDataUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getProviderUrl() {
        return providerUrl;
    }

    public void setProviderUrl(List<String> providerUrl) {
        this.providerUrl = providerUrl;
    }
}
