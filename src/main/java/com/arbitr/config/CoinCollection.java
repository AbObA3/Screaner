package com.arbitr.config;

import com.arbitr.repository.Repository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
@Getter
@Setter
public class CoinCollection {


    @Inject
    Repository repository;

    public CoinCollection() {
        hashMap = new ConcurrentHashMap<>();
        list = new ArrayList<>();
        queue = new ConcurrentLinkedQueue<>();
    }
    private ConcurrentHashMap<String, List<String>>  hashMap;

    private List<String> list;

    private ConcurrentLinkedQueue<String> queue;

    public void addToMap(String coin, List<String> dexList) {
        hashMap.put(coin, dexList);
        list.add(coin);
        queue.add(coin);
    }

    public void clearCollection() {
        hashMap.clear();
        list.clear();
        queue.clear();
        repository.deleteAll();
    }



}
