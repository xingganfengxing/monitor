package com.letv.cdn.report.pojo;

/**
 * Created by liufeng1 on 2015/3/16.
 */
public class BusinessNode implements Comparable<BusinessNode> {
    private String id;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(BusinessNode o) {
        int id1 = Integer.parseInt(id);
        int id2 = Integer.parseInt(o.getId());
        return id1 - id2;
    }
}
