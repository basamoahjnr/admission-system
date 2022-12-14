package com.yasobafinibus.nnmtc.enrollment.infra.model;


import org.primefaces.model.FilterMeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Filter<T extends Serializable> {

    private T entity;
    private int first;
    private int pageSize;
    private String sortField;
    private List<String> sortFields;
    private AppSortOrder appSortOrder;
    private Map<String, Object> params = new HashMap<>();


    public Filter() {
    }

    public Filter(T entity) {
        this.entity = entity;
    }

    public int getFirst() {
        return first;
    }

    public Filter setFirst(int first) {
        this.first = first;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Filter setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getSortField() {
        return sortField;
    }

    public Filter setSortField(String sortField) {
        this.sortField = sortField;
        return this;
    }

    public AppSortOrder getSortOrder() {
        return appSortOrder;
    }

    public Filter setSortOrder(AppSortOrder appSortOrder) {
        this.appSortOrder = appSortOrder;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Filter setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public T getEntity() {
        return entity;
    }

    public Filter setEntity(T entity) {
        this.entity = entity;
        return this;
    }

    public Filter addParam(String key, Object value) {
        getParams().put(key, value);
        return this;
    }

    public boolean hasParam(String key) {
        return getParams().containsKey(key) && getParam(key) != null;
    }

    public Object getParam(String key) {
        return ((FilterMeta) getParams().get(key)).getFilterValue();
    }

    public List<String> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<String> sortFields) {
        this.sortFields = sortFields;
    }
}
