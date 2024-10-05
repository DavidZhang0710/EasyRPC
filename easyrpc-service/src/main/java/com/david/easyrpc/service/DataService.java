package com.david.easyrpc.service;

import java.util.List;

public interface DataService {
    String sendData(String body);
    List<String> getList();
    String testErrorV2();
}
