package com.yasobafinibus.nnmtc.enrollment.infra.model;


public enum AppSortOrder {

    ASCENDING, DESCENDING, UNSORTED;

    public boolean isAscending() {
        return ASCENDING.equals(this);
    }
}
