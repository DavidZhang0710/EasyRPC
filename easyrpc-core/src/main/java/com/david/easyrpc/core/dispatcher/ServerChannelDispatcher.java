package com.david.easyrpc.core.dispatcher;

import com.david.easyrpc.core.common.RpcInvocation;
import com.david.easyrpc.core.common.RpcProtocol;
import com.david.easyrpc.core.exception.IRpcException;
import com.david.easyrpc.core.server.NamedThreadFactory;
import com.david.easyrpc.core.server.ServerChannelReadData;

import java.lang.reflect.Method;
import java.util.concurrent.*;

import static com.david.easyrpc.core.cache.CommonServerCache.*;
import static com.david.easyrpc.core.cache.CommonServerCache.SERVER_SERIALIZE_FACTORY;

public class ServerChannelDispatcher {

    private BlockingQueue<ServerChannelReadData> RPC_DATA_QUEUE;

    private ExecutorService executorService;

    public void init(int queueSize, int bizThreadNums) {
        
        
        
        
        RPC_DATA_QUEUE = new ArrayBlockingQueue<>(queueSize);
        executorService = new ThreadPoolExecutor(bizThreadNums, bizThreadNums,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(512));
    }

    public void add(ServerChannelReadData serverChannelReadData) {
        RPC_DATA_QUEUE.add(serverChannelReadData);
    }

    class ServerJobCoreHandle implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    ServerChannelReadData serverChannelReadData = RPC_DATA_QUEUE.take();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            RpcProtocol rpcProtocol = serverChannelReadData.getRpcProtocol();
                            
                            RpcInvocation rpcInvocation = SERVER_SERIALIZE_FACTORY.deserialize(rpcProtocol.getContent(), RpcInvocation.class);
                            System.out.println("rpcInvocation:" + rpcInvocation.getTargetServiceName());
                            System.out.println("serialize:" + SERVER_SERIALIZE_FACTORY);
                            
                            try {
                                SERVER_BEFORE_FILTER_CHAIN.doFilter(rpcInvocation);
                            } catch (Exception e) {
                                if (e instanceof IRpcException) {
                                    IRpcException rpcException = (IRpcException) e;
                                    RpcInvocation repParam = rpcException.getRpcInvocation();
                                    rpcInvocation.setE(e);
                                    byte[] body = SERVER_SERIALIZE_FACTORY.serialize(repParam);
                                    RpcProtocol respRpcProtocol = new RpcProtocol(body);
                                    serverChannelReadData.getChannelHandlerContext().writeAndFlush(respRpcProtocol);
                                    return;
                                }
                            }
                            
                            Object aimObject = PROVIDER_CLASS_MAP.get(rpcInvocation.getTargetServiceName());
                            
                            Method[] methods = aimObject.getClass().getDeclaredMethods();
                            Object result = null;
                            
                            for (Method method : methods) {
                                if (method.getName().equals(rpcInvocation.getTargetMethod())) {
                                    if (method.getReturnType().equals(Void.TYPE)) {
                                        try {
                                            method.invoke(aimObject, rpcInvocation.getArgs());
                                        } catch (Exception e) {
                                            rpcInvocation.setE(e);
                                        }
                                    } else {
                                        try {
                                            result = method.invoke(aimObject, rpcInvocation.getArgs());
                                        } catch (Exception e) {
                                            rpcInvocation.setE(e);
                                        }
                                    }
                                    break;
                                }
                            }
                            
                            rpcInvocation.setResponse(result);
                            
                            SERVER_AFTER_FILTER_CHAIN.doFilter(rpcInvocation);
                            
                            RpcProtocol respRpcProtocol = new RpcProtocol(SERVER_SERIALIZE_FACTORY.serialize(rpcInvocation));
                            serverChannelReadData.getChannelHandlerContext().writeAndFlush(respRpcProtocol);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startDataConsume() {
        Thread thread = new Thread(new ServerJobCoreHandle());
        thread.start();
    }
}
