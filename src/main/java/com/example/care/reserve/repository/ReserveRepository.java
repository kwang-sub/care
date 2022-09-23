package com.example.care.reserve.repository;

import com.example.care.reserve.domain.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long>, ReserveRepositoryCustom {

}
