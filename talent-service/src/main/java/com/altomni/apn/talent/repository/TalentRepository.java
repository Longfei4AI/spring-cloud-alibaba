package com.altomni.apn.talent.repository;

import com.altomni.apn.talent.domain.Talent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Candidate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {}
