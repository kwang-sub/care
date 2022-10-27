package com.example.care.reserve.repository;

import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.domain.ReserveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReserveRepository extends JpaRepository<Reserve, Long>, ReserveRepositoryCustom {

    @Modifying
    @Query("update Reserve r set r.reserveStatus = :afterStatus where r.reserveDate <= :from and r.reserveTime <= :hour and r.reserveStatus = :beforeStatus")
    void updateStatusComplete(@Param("from") LocalDate from, @Param("hour") int hour,
                              @Param("afterStatus") ReserveStatus afterStatus,
                              @Param("beforeStatus") ReserveStatus beforeStatus);
}
