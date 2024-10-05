package com.david.easyrpc.consumer;

import com.david.easyrpc.core.client.Client;
import com.david.easyrpc.core.client.ConnectionHandler;
import com.david.easyrpc.core.client.RpcReference;
import com.david.easyrpc.core.client.RpcReferenceWrapper;
import com.david.easyrpc.service.DataService;

import java.util.Scanner;


public class ConsumerDemo {

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setAimClass(DataService.class);
        rpcReferenceWrapper.setGroup("dev");
        rpcReferenceWrapper.setServiceToken("token-a");
        rpcReferenceWrapper.setTimeOut(3000);
        rpcReferenceWrapper.setRetry(0);
        rpcReferenceWrapper.setAsync(false);
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            for(int i=0;i<1;i++){
                String result = dataService.testErrorV2();
                System.out.println(result);
            }
            System.out.println("并发结束");
        }
    }
}
