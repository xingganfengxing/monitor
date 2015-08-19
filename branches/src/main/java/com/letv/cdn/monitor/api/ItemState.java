package com.letv.cdn.monitor.api;



/**
 * 记录 某一监控项的状态
 * 
 * @author lvzhouyang
 * @createDate 2015年3月17日
 */
public class ItemState{
    
    private String itemName;
    private String value;
    
    public String getItemName() {
    
        return itemName;
    }
    
    public void setItemName(String itemName) {
    
        this.itemName = itemName;
    }
    
    public String getValue() {
    
        return value;
    }
    
    public void setValue(String value) {
    
        this.value = value;
    }
    
    public void changeSate(String value) {
        
        this.setValue(value);
        
    }

    @Override
    public String toString() {
    
        return itemName + " " + value;
    }
    
    
}
