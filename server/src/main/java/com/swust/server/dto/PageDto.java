package com.swust.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDto<T> {

    protected int page;

    protected int size;

    protected long total;

    protected List<T> list;
}
