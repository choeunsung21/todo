package com.gn.todo.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long>{
	
	List<Todo> findAll(Specification<Todo> spec);
}
