package com.lijian.springgraphqlusage.repository;

import com.lijian.springgraphqlusage.domain.Car;
import com.lijian.springgraphqlusage.domain.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.graphql.data.GraphQlRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long>{
    List<Car> findByOwnerCardNum(String ownerCardNum);
}
