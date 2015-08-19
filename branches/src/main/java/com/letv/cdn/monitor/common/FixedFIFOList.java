package com.letv.cdn.monitor.common;

import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * 固定大小 队列
 * @author lvzhouyang
 * @createDate 2015年3月11日
 */
public interface FixedFIFOList<T> extends List<T>, Deque<T>, Cloneable, java.io.Serializable{
    /** 
     * 向最后添加一个新的，如果长度超过允许的最大值，则弹出第一个
     */  
    T addLastSafe(T addLast);  
  
    /** 
     * 弹出head，如果Size = 0返回null。而不同于pop抛出异常 
     * @return  
     */  
    T pollSafe();  
  
    /** 
     * 获得最大保存 
     * 
     * @return 
     */  
    int getMaxSize();  
  
    /** 
     * 设置最大存储范围 
     * 
     * @return 返回的是，因为改变了队列大小，导致弹出的head 
     */  
    List<T> setMaxSize(int maxSize);  
    
    /**
     * 批量添加
     * @method: FixedFIFOList  addAllLatSafe
     * @param c
     * @return  boolean
     * @createDate： 2015年3月16日
     * @2015, by lvzhouyang.
     */
    public boolean addAllLatSafe(Collection<? extends T> c);
}
