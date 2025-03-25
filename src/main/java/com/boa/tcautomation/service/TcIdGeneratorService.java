package com.boa.tcautomation.service;

import com.boa.tcautomation.model.TcSequence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TcIdGeneratorService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public String generateNextTcId() {
        TcSequence sequence = entityManager.createQuery(
                        "SELECT s FROM TcSequence s WHERE s.id = 'tc_id_seq'",
                        TcSequence.class)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getSingleResult();

        long nextValue = sequence.getValue() + 1;
        sequence.setValue(nextValue);
        entityManager.persist(sequence);

        return "TC" + nextValue;
    }
}
