package org.example.forum.repositories;

import org.example.forum.Entities.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAllSortedBy(Pageable pageable);
}
