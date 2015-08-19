package com.letv.cdn.monitor.api;


public class CheckItemContext{
    
    private AbstractCheckItem checkItem;
    
    public CheckItemContext(AbstractCheckItem checkItem) {
    
        super();
        this.checkItem = checkItem;
    }
    
    public AbstractCheckItem getCheckItem() {
    
        return checkItem;
    }
    
    public void setCheckItem(AbstractCheckItem checkItem) {
    
        this.checkItem = checkItem;
    }
    
    public ItemState check() {
    
        return checkItem.operateCheck();
    }
    
}
