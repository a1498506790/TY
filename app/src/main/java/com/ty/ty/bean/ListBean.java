package com.ty.ty.bean;

import java.util.List;

/**
 * 分页加载数据Bean
 *
 */
public class ListBean<T> {

    /**
     * pageAll:  最大页,
     * pageIndex: 当前页,
     * pageSize: 总条数
     * recordList : []
     */

    public int pageAll;
    public int pageIndex;
    public int pageSize;
    public List<T> lists;

    public int getPageAll() {
        return pageAll;
    }

    public void setPageAll(int pageAll) {
        this.pageAll = pageAll;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getLists() {
        return lists;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }
}
