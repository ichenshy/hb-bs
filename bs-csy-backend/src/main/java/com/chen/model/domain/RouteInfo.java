package com.chen.model.domain;

import lombok.Data;

@Data
public class RouteInfo {
    private Long id;
    private String path;
    private String component;
    private String title;
    private String icon;
    private Long parentId;

}
