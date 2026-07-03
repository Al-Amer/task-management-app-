package com.example.taskmanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTitleContaining(String keyword);
    List<Task> findByDone(boolean done);
    List<Task> findByCreatedBy(String createdBy);

}