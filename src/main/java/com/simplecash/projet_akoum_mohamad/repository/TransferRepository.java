package com.simplecash.projet_akoum_mohamad.repository;

import com.simplecash.projet_akoum_mohamad.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySourceAccountId(Long accountId);
    List<Transfer> findByTargetAccountId(Long accountId);
}

