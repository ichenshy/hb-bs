package com.chen.model.domain;

import lombok.Data;

@Data
public class RouteInfo {
    private String path;
    private String component;
    private String name;
    private String icon;

    public RouteInfo(String path, String component, String name,String icon) {
        this.path = path;
        this.component = component;
        this.name = name;
        this.icon = icon;
    }
}
