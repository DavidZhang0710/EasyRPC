package com.david.easyrpc.provider;

import com.david.easyrpc.core.event.IRpcListenerLoader;
import com.david.easyrpc.core.server.ApplicationShutdownHook;
import com.david.easyrpc.core.server.Server;
import com.david.easyrpc.core.server.ServiceWrapper;
import com.david.easyrpc.service.DataServiceImpl;
import com.david.easyrpc.service.UserServiceImpl;

import java.io.IOException;

public class ProviderDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        Server server = new Server();
        server.initServerConfig();
        IRpcListenerLoader iRpcListenerLoader = new IRpcListenerLoader();
        iRpcListenerLoader.init();
        ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(new DataServiceImpl(), "dev");
        dataServiceServiceWrapper.setServiceToken("token-a");
        dataServiceServiceWrapper.setLimit(4);
        ServiceWrapper userServiceServiceWrapper = new ServiceWrapper(new UserServiceImpl(), "dev");
        userServiceServiceWrapper.setServiceToken("token-b");
        userServiceServiceWrapper.setLimit(4);
        server.exportService(dataServiceServiceWrapper);
        server.exportService(userServiceServiceWrapper);
        ApplicationShutdownHook.registryShutdownHook();
        server.startApplication();
    }
}