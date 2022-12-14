package com.yasobafinibus.nnmtc.enrollment.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.yasobafinibus.nnmtc.enrollment.infra.model.AppSortOrder;
import com.yasobafinibus.nnmtc.enrollment.infra.model.Filter;
import com.yasobafinibus.nnmtc.enrollment.model.*;
import com.yasobafinibus.nnmtc.enrollment.model.Applicant.AdmissionStatus;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.model.SortMeta;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.yasobafinibus.nnmtc.enrollment.model.PaymentLedger.PaymentLedgerType.valueOf;


@Stateless
public class ApplicantService implements Serializable {


    @PersistenceContext
    private EntityManager entityManager;

    public List<Applicant> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Applicant> cq = cb.createQuery(Applicant.class);
        Root<Applicant> root = cq.from(Applicant.class);
        cq.select(root);
        return getEntityManager().createQuery(cq).getResultList();
    }


    public List<String> getApplicantNumbers(String query) {
        return getEntityManager()
                .createNamedQuery("Applicant.findApplicantNumbers", String.class)
                .setParameter("nmcNumber", "%" + query + "%").getResultList();
    }


    public void save(@Valid Applicant applicant) {
        getEntityManager().persist(applicant);
    }

    public void saveOrUpdate(Applicant applicant) {
        if (applicant.getId() == null) {
            getEntityManager().persist(applicant);
        } else {
            PaymentLedger _entity = getEntityManager().find(PaymentLedger.class, applicant.getId());
            if (_entity != null) {
                getEntityManager().merge(applicant);
            } else {
                getEntityManager().persist(applicant);
            }
        }
    }


    public void remove(Applicant applicant) {
        getEntityManager().remove(applicant);
    }


    public Optional<Applicant> findApplicantByNumber(String number) {
        return getEntityManager()
                .createNamedQuery("Applicant.findByApplicantNumber", Applicant.class)
                .setParameter("nmcNumber", number).getResultStream().findFirst();

    }

    public Applicant findById(Integer id) {
        return getEntityManager().find(Applicant.class, id);
    }


    @Transactional
    public Applicant update(Applicant applicant) {
        getEntityManager().merge(applicant);
        getEntityManager().flush();
        return applicant;
    }

    public Long totalAdmissionStatusTrue(boolean status) {
        return getEntityManager()
                .createNamedQuery("Applicant.findTotalAdmissionStatusTrue", Long.class)
                .setParameter("admissionStatus", status)
                .getSingleResult();
    }

    private List<Predicate> getFilterCondition(CriteriaBuilder cb, Root<Applicant> root, Filter<Applicant> filters) {

        List<Predicate> predicates = new ArrayList<>();

        Map<String, Object> params = filters.getParams();
        if (!params.isEmpty()) {
            //get all declared fields in applicant class
            List<String> applicantFields = Arrays
                    .stream(Applicant.class.getDeclaredFields()).collect(Collectors.toList())
                    .stream()
                    .map(Field::getName)
                    .collect(Collectors.toList());

            //loop over column keys
            Iterator<Map.Entry<String, Object>> columns = params.entrySet().iterator();

            //while column keys contains a value
            while (columns.hasNext()) {
                Map.Entry<String, Object> column = columns.next();
                //for every applicant field
                for (String applicantField : applicantFields) {
                    //if the column can be found in the applicant field
                    if (applicantField.equals(column.getKey())) {
//                        "%" + (String) column.getValue() + "%"
                        //create a predicate of it using the value associated with that value
                        predicates.add(cb.like(root.get(applicantField), "%" + column.getValue().toString() + "%"));
                    }
                }
                columns.remove(); // avoids a ConcurrentModificationException
            }

        }
        return predicates;

    }


    public int count(Filter<Applicant> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Applicant> root = cq.from(Applicant.class);
        List<Predicate> filterCondition = getFilterCondition(cb, root, filters);

        cq.where(cb.and(filterCondition.toArray(new Predicate[0])));

        cq.select(cb.count(root));
        return getEntityManager()
                .createQuery(cq)
                .getSingleResult()
                .intValue();
    }

    public List<Applicant> getResultList(Filter<Applicant> filters) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Applicant> cq = cb.createQuery(Applicant.class);
        Root<Applicant> root = cq.from(Applicant.class);
        List<Predicate> filterCondition = getFilterCondition(cb, root, filters);


        if (filters.getSortOrder() != null) {
            List<String> sortFields = filters.getSortFields();
            //define list of order
            List<Order> orders = new ArrayList<>();
            if (filters.getSortOrder() == AppSortOrder.ASCENDING) {
                for (String sortField : sortFields) {
                    orders.add(cb.asc(root.get(sortField)));
                }
                //add to list of order
                cq.orderBy(orders);
            } else if (filters.getSortOrder() == AppSortOrder.DESCENDING) {
                for (String sortField : sortFields) {
                    orders.add(cb.asc(root.get(sortField)));
                }
                //add to list of order
                cq.orderBy(orders);
            }
        }

        cq.where(cb.and(filterCondition.toArray(new Predicate[0])));
        return getEntityManager()
                .createQuery(cq)
                .setFirstResult(filters.getFirst())
                .setMaxResults(filters.getPageSize())
                .getResultList();
    }

    /*
      The main EJB Service method you should use in you overridden LazyDataModel.load() method
    */
    public List<?> findEntities(Class<?> c, int first, int pageSize,
                                Map<String, SortMeta> sortBy,
                                Map<String, FilterMeta> filterBy) {
        List<?> result = new ArrayList<>();
        String sql = buildWhereParams("from " + c.getName() + " ent where 1=1 ", filterBy);
        sql = buildOrderBy(sql, sortBy);
        TypedQuery<?> query = entityManager.createQuery(sql, c)
                .setFirstResult(first)
                .setMaxResults(pageSize);
        query = setWhereParams(query, filterBy);
        result = query.getResultList();
        return result;
    }


    protected String buildWhereParams(String sql, Map<String, FilterMeta> filters) {
        if ((filters != null) && !filters.isEmpty()) {
            Iterator<String> fColumns = filters.keySet().iterator();
            String column;
            FilterMeta fMeta;
            MatchMode mMode;
            Object fValue;
            StringBuilder sqlBuilder = new StringBuilder(sql);
            while (fColumns.hasNext()) {
                column = fColumns.next();
                fMeta = filters.get(column);
                mMode = fMeta.getMatchMode();
                sqlBuilder.append(" and ent.").append(column).append(mapMatchModeToJPAOperator(mMode)).append(":").append(column).append(" ");
            }
            sql = sqlBuilder.toString();
        }
        return sql;
    }// of buildWhereParams()


    /*<p:column headerText="..." sortBy="#{comp.name}" filterBy="#{comp.name}" filterMatchMode="contains">
       <h:outputText value="..." />
     </p:column>
     the filterMatchMode has constants that we use the method to match to a query condition
     */
    protected String mapMatchModeToJPAOperator(MatchMode mMode) {
        switch (mMode) {
            case CONTAINS:
            case ENDS_WITH:
            case STARTS_WITH:
                return " like ";
            case EQUALS: // Checks if column value equals the filter value
            case EXACT:
                return " = "; // Checks if string representations of column value and filter value are same
            case GREATER_THAN:
                return " > ";
            case GREATER_THAN_EQUALS:
                return " >= ";
            case LESS_THAN:
                return " < ";
            case LESS_THAN_EQUALS:
                return " <= ";
        }
        return " = ";
    }


    protected TypedQuery<?> setWhereParams(TypedQuery<?> query, Map<String, FilterMeta> filters) {
        if ((filters != null) && !filters.isEmpty()) {
            Iterator<String> fColumns = filters.keySet().iterator();
            String column;
            FilterMeta fMeta;
            MatchMode mMode;
            Object fValue;
            while (fColumns.hasNext()) {
                column = fColumns.next();//get the column (key,value)
                fMeta = filters.get(column);//get the column value
                mMode = fMeta.getMatchMode();//get match mode
                fValue = fMeta.getFilterValue();// get filtered value
                query.setParameter(column, buildParamValue(fValue, mMode));
            }
        }
        return query;
    }


    protected Object buildParamValue(Object fValue, MatchMode mMode) {
        if ((fValue instanceof String)) {
            fValue = addPrefixWildcardIfNeed(mMode) + fValue;
        }
        return fValue;
    }

    private String addPrefixWildcardIfNeed(MatchMode mMode) {
        switch (mMode) {
            case CONTAINS:
            case STARTS_WITH:
                return "%";
        }
        return "";
    }


    protected String buildOrderBy(String sql, Map<String, SortMeta> sortBy) {
        if ((sortBy != null) && !sortBy.isEmpty()) {
            SortMeta sortM = sortBy.entrySet().stream().findFirst().get().getValue();
            String sortField = sortM.getField().trim();
            String sortOrder = sortM.getOrder().toString();
            sql += " order by ent." + sortField + " " + ((sortOrder.equals("ASCENDING") ? "ASC" : "DESC"));
        }
        return sql;
    }

    public Applicant findApplicantById(Integer key) {
        return this.findById(key);
    }


    public Integer countApplicants(AdmissionStatus admissionStatus) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Applicant> root = cq.from(Applicant.class);
        cq.where(cb.equal(root.get(Applicant_.ADMISSION_STATUS), admissionStatus));
        cq.select(cb.count(root));
        return getEntityManager()
                .createQuery(cq)
                .getSingleResult()
                .intValue();
    }

    public Set<Applicant> csvToApplicants(String filename, InputStream inputStream) {


        List<ApplicantDTO> beans;
        List<Applicant> applicants;
        beans = new CsvToBeanBuilder(new InputStreamReader(inputStream))
                .withType(ApplicantDTO.class)
                .build()
                .parse();

        applicants = beans.stream().map(applicantDTO -> new Applicant(
                applicantDTO.getNumber(),
                applicantDTO.getSurname(),
                applicantDTO.getOtherNames(),
                applicantDTO.getGender(),
                new Program(applicantDTO.getProgram()),
                applicantDTO.getAdmissionYear(),
                Applicant.AdmissionStatus.valueOf(applicantDTO.getAdmissionStatus()),
                new PaymentLedger(valueOf(applicantDTO.getLedgerName()),
                        PaymentLedger.PaymentStatus.valueOf(applicantDTO.getPaymentStatus()),
                        applicantDTO.getAmountToPay().doubleValue())


        )).collect(Collectors.toList());
        applicants.forEach(System.out::println);

        return new HashSet<>(applicants);
    }

    private EntityManager getEntityManager() {
        return this.entityManager;
    }
}

