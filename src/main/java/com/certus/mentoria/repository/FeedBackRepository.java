package com.certus.mentoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.certus.mentoria.model.feedback.Feedback;

@Repository
public interface FeedBackRepository extends JpaRepository<Feedback, Long>{
    List<Feedback> findBySesionMentorId(Long mentorId);

}
