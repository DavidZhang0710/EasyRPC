package com.david.easyrpc.core.event;

import com.david.easyrpc.core.common.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IRpcListenerLoader {

    private static List<IRpcListener> iRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(IRpcListener iRpcListener) {
        iRpcListenerList.add(iRpcListener);
    }

    public void init() {
        registerListener(new com.david.easyrpc.core.event.ServiceUpdateListener());
        registerListener(new com.david.easyrpc.core.event.ServiceDestroyListener());
        registerListener(new com.david.easyrpc.core.event.ProviderNodeDataChangeListener());
    }

    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    public static void sendSyncEvent(IRpcEvent iRpcEvent) {
        if (CommonUtils.isEmptyList(iRpcListenerList)) {
            return;
        }
        for (IRpcListener<?> iRpcListener : iRpcListenerList) {
            Class<?> type = getInterfaceT(iRpcListener);
            if (type.equals(iRpcEvent.getClass())) {
                try {
                    iRpcListener.callBack(iRpcEvent.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendEvent(IRpcEvent iRpcEvent) {
        if (CommonUtils.isEmptyList(iRpcListenerList)) {
            return;
        }
        for (IRpcListener<?> iRpcListener : iRpcListenerList) {
            Class<?> type = getInterfaceT(iRpcListener);
            if (type.equals(iRpcEvent.getClass())) {
                eventThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            iRpcListener.callBack(iRpcEvent.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
