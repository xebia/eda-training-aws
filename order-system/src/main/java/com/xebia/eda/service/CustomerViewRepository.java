package com.xebia.eda.service;

import com.xebia.common.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerViewRepository extends JpaRepository<Customer, Long> {
}
