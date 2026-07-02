package com.group.express.repository;

import com.group.express.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{

    @Override
    Optional<Member> findById(String id);




    List<Member> findByNameContaining(String partialName);
    List<Member> findByIdContaining(String id);

    Member findByNameAndEmail(String name, String email);
    Optional<Member> findByIdAndEmail(String id, String email);









}
