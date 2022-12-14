package com.yasobafinibus.nnmtc.enrollment.controller;

import com.yasobafinibus.nnmtc.enrollment.model.Applicant;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yasobafinibus.nnmtc.enrollment.infra.model.AppSortOrder.*;

public class ApplicantLazyDataModel extends LazyDataModel<Applicant> {

    private final ApplicantController listController;

    public ApplicantLazyDataModel(ApplicantController applicantController) {
        this.listController = applicantController;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return super.getRowCount();
    }

    @Override
    public List<Applicant> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
                                Map<String, FilterMeta> filterBy) {

        if (sortBy != null && sortBy.size() > 0) {
            //TODO fixSortBy, this does not take into consideration the position of the field and sortCriteria
            //the fields to sort by
            List<String> sortFields = sortBy.values().stream().map(SortMeta::getField).collect(Collectors.toList());
            //in what order to sort the field
            Optional<SortOrder> sortOrder = sortBy.values().stream().map(SortMeta::getOrder).findFirst();


            //determine what sort criteria to use
            if (sortOrder.isPresent()) {
                //add the sort fields
                listController.filter.setSortFields(sortFields);
                switch (sortOrder.get()) {
                    case ASCENDING:
                        listController.filter.setSortOrder(ASCENDING);
                        break;
                    case DESCENDING:
                        listController.filter.setSortOrder(DESCENDING);
                        break;
                    default:
                        listController.filter.setSortOrder(UNSORTED);
                }
            }

        }

        //check if the filter for number is empty then set applicant number as entity filter number
        //the search box to work in tandem to the filter columns
        if (listController.getApplicantNumber() != null) {
            listController.filter.getEntity().setNumber(listController.getApplicantNumber());
        }

        //loop through the filter value put the field and its value filter params
        if ((filterBy != null) && !filterBy.isEmpty()) {
            for (String filterColumnName : filterBy.keySet()) {
                listController.filter.addParam(filterColumnName, filterBy.get(filterColumnName).getFilterValue());
            }
        }

        listController.filter.setFirst(offset).setPageSize(pageSize);
        List<Applicant> s = listController.getApplicantService().getResultList(listController.filter);
        setRowCount(listController.getApplicantService().count(listController.filter));

        return s;
    }

    @Override
    public int getRowCount() {
        return super.getRowCount();
    }

    @Override
    public Applicant getRowData(String key) {
        return listController.getApplicantService().findApplicantById(Integer.valueOf(key));
    }

    @Override
    public String getRowKey(Applicant applicant) {
        return String.valueOf(applicant.getId());
    }
}
