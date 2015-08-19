package com.letv.cdn.monitor.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 固定大小队列实现类
 * @author lvzhouyang
 * @createDate 2015年3月11日
 */
public class FixedFIFOLinkedList<T> extends LinkedList<T> implements FixedFIFOList<T>{
    
    private static final long serialVersionUID = 1L;
    
    private int maxSize = Integer.MAX_VALUE;
    private final Object synObj = new Object();
    
    public FixedFIFOLinkedList() {
    
        super();
    }
    
    public FixedFIFOLinkedList(int maxSize) {
    
        super();
        this.maxSize = maxSize;
    }
    
    @Override
    public T addLastSafe(T addLast) {
    
        synchronized (synObj) {
            T head = null;
            while (size() >= maxSize) {
                head = poll();
            }
            addLast(addLast);
            return head;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean addAllLatSafe(Collection<? extends T> c) {
        //判断条件
        T[] a = (T[])c.toArray();
        int numNew = a.length;
        if (numNew == 0){
            return false;
        }
        //依次添加
        for (int i = 0; i < a.length; i++) {
            addLastSafe(a[i]);
        }
        return true;
    }
    
    @Override
    public T pollSafe() {
    
        synchronized (synObj) {
            return poll();
        }
    }
    
    @Override
    public List<T> setMaxSize(int maxSize) {
    
        List<T> list = null;
        if (maxSize < this.maxSize) {
            list = new ArrayList<T>();
            synchronized (synObj) {
                while (size() > maxSize) {
                    list.add(poll());
                }
            }
        }
        this.maxSize = maxSize;
        return list;
    }
    
    @Override
    public int getMaxSize() {
    
        return this.maxSize;
    }
    
}
