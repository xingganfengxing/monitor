package com.letv.cdn.monitor.api;

import com.letv.cdn.monitor.utils.Constants;

public abstract class AbstractCheckItem{
    
    public ItemState itemState = new ItemState();
    
    public AbstractCheckItem() {
    
        super();
    }
    
    
    public ItemState getItemState() {
    
        return itemState;
    }
    
    public void setItemState(ItemState itemState) {
    
        this.itemState = itemState;
    }
    
    public void initItemState(String name){
        this.itemState.setItemName(name);
        this.itemState.setValue(Constants.SYSTEM_STATUS_OK);
    }
    
    abstract public ItemState operateCheck();
}
