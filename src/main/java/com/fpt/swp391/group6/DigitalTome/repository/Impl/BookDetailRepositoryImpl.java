package com.fpt.swp391.group6.DigitalTome.repository.Impl;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import com.fpt.swp391.group6.DigitalTome.repository.IBookDetailRepository;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
@Transactional
public class BookDetailRepositoryImpl implements IBookDetailRepository {
    private static final Logger logger = LoggerFactory.getLogger(BookDetailRepositoryImpl.class);

    private EntityManager entityManager;

    @Autowired
    public BookDetailRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<BookEntity> findByStatus(int status, Pageable pageable) {
        List<BookEntity> list = entityManager.createQuery("""
                select b from BookEntity b 
                left join fetch b.categoryEntityList
                where b.status = :status 
                 """, BookEntity.class).setParameter("status",status)
                                        .setFirstResult((int) pageable.getOffset())
                                        .setMaxResults(pageable.getPageSize())
                                        .getResultList();

        list = entityManager.createQuery("""
               select b from BookEntity b 
               left join fetch b.authorEntityList
               where b.status = :status 
               """, BookEntity.class).setParameter("status",status)
                                     .setFirstResult((int) pageable.getOffset())
                                     .setMaxResults(pageable.getPageSize())
                                     .getResultList();
        return list;
    }

    @Override
    public BookEntity findByIsbnIncludeCategoriesAndAuthors(String isbn) {
        List<BookEntity> list = entityManager.createQuery("""
            select b from BookEntity b 
            left join fetch b.categoryEntityList
            where b.isbn = :isbn 
            """, BookEntity.class).setParameter("isbn",isbn).getResultList();

        list = entityManager.createQuery("""
               select b from BookEntity b 
               left join fetch b.authorEntityList
               where b.isbn = :isbn 
               """, BookEntity.class).setParameter("isbn",isbn).getResultList();
        return (list.isEmpty())?new BookEntity():list.get(0);
    }

    @Override
    public Page<BookEntity> search(SearchRequest searchRequest, Pageable pageable) {
       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
       CriteriaQuery<BookEntity> cq = cb.createQuery(BookEntity.class);

        Root<BookEntity> root = cq.from(BookEntity.class);

        List<Predicate> keyworkPredicates = new ArrayList<>();
        keyworkPredicates.add(cb.like(root.get("title"),"%"+ searchRequest.getKeyword()+"%"));
        keyworkPredicates.add(cb.like(root.get("isbn"),"%"+ searchRequest.getKeyword()+"%"));
        keyworkPredicates.add(cb.like(root.get("description"),"%"+ searchRequest.getKeyword()+"%"));
        keyworkPredicates.add(cb.like(root.get("edition"),"%"+ searchRequest.getKeyword()+"%"));
        keyworkPredicates.add(cb.like(root.get("language"),"%"+ searchRequest.getKeyword()+"%"));

        Predicate orPredicate = cb.or(keyworkPredicates.toArray(new Predicate[0]));

        List<Predicate> optionsPredicate = new ArrayList<>();
        optionsPredicate.add(cb.equal(root.get("status"),2));

        if(searchRequest.getMinPoint() != null){
            optionsPredicate.add(cb.greaterThanOrEqualTo(root.get("point"), searchRequest.getMinPoint()));
        }

        if(searchRequest.getMaxPoint() != null){
            optionsPredicate.add(cb.lessThanOrEqualTo(root.get("point"), searchRequest.getMaxPoint()));
        }

        if(searchRequest.getYears() != null && !searchRequest.getYears().isEmpty()){
            Expression<Integer> yearExpression = cb.function("YEAR", Integer.class, root.get("publicationDate"));
            optionsPredicate.add(yearExpression.in(searchRequest.getYears()));
        }

        if(searchRequest.getCategories() != null && !searchRequest.getCategories().isEmpty()){
           Join<BookEntity, CategoryEntity> categoryEntityJoin =  root.join("categoryEntityList", JoinType.INNER);
           optionsPredicate.add(categoryEntityJoin.get("name").in(searchRequest.getCategories()));
        }


        List<Predicate> criteriaPredicates = new ArrayList<>();
        criteriaPredicates.add(orPredicate);
        criteriaPredicates.addAll(optionsPredicate);

        cq.where(cb.and(criteriaPredicates.toArray(new Predicate[0])));
        List<Order> orders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            Path<Object> sortByPath = root.get(order.getProperty());
            orders.add(order.isAscending() ? cb.asc(sortByPath) : cb.desc(sortByPath));
        }
        if (!orders.isEmpty()) {
            cq.orderBy(orders);
        }
        TypedQuery<BookEntity> query = entityManager.createQuery(cq);

        int total = query.getResultList().size();


        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<BookEntity> result = query.getResultList().stream().map(b -> this.findByIsbnIncludeCategoriesAndAuthors(b.getIsbn())).collect(Collectors.toList());

        return new PageImpl<>(result, pageable, total);
    }
}
