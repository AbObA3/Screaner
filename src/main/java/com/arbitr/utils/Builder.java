package com.arbitr.utils;

import com.arbitr.model.DexCurrency;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


@UtilityClass
public final class Builder {

    private final ConcurrentLinkedQueue<List<DexCurrency>> queue = new ConcurrentLinkedQueue<>();

    public String getStringBuilder() {
        StringBuilder sb = new StringBuilder();
        if (!queue.isEmpty())
            queue.poll().forEach(sb::append);
        return sb.toString();
    }


    public void appendList(List<DexCurrency> list) {
        queue.add(list);
    }

    public Boolean isEmpty() {
        return queue.isEmpty();
    }

    public void clean() {
        queue.clear();
    }

}
