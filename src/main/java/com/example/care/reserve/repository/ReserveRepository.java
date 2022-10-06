package com.example.care.reserve.repository;

import com.example.care.reserve.domain.Reserve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long>, ReserveRepositoryCustom {

    Page<Reserve> findByUserId(Long userId, Pageable pageable);

}
