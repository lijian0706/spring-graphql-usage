package com.lijian.springgraphqlusage.repository;

import com.lijian.springgraphqlusage.domain.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface HumanRepository extends JpaRepository<Human, Long> {
}
