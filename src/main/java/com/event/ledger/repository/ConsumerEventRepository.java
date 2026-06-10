package com.event.ledger.repository;

import com.event.ledger.model.ConsumerEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumerEventRepository extends JpaRepository<ConsumerEventEntity, String> {

    List<ConsumerEventEntity> findByAccountId(String accountId);

    List<ConsumerEventEntity> findByAccountIdAndType(String accountId, String type);

    List<ConsumerEventEntity> findByType(String type);

    @Query("SELECT ce FROM ConsumerEventEntity ce WHERE ce.accountId = :accountId ORDER BY ce.eventTimestamp ASC")
    List<ConsumerEventEntity> findByAccountIdOrderByTimestamp(@Param("accountId") String accountId);

    @Query("SELECT ce FROM ConsumerEventEntity ce WHERE ce.accountId = :accountId AND ce.type = :type ORDER BY ce.eventTimestamp ASC")
    List<ConsumerEventEntity> findByAccountIdAndTypeOrderByTimestamp(
            @Param("accountId") String accountId,
            @Param("type") String type
    );

    Optional<ConsumerEventEntity> findByEventId(String eventId);

    @Query("SELECT COALESCE(SUM(CASE WHEN ce.type = 'CREDIT' THEN ce.amount ELSE 0 END), 0.0) " +
           "- COALESCE(SUM(CASE WHEN ce.type = 'DEBIT' THEN ce.amount ELSE 0 END), 0.0) " +
           "FROM ConsumerEventEntity ce WHERE ce.accountId = :accountId")
    Double calculateNetBalanceByAccountId(@Param("accountId") String accountId);
}
