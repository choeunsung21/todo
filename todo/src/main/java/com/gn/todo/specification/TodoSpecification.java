package com.gn.todo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.todo.entity.Todo;

public class TodoSpecification {

	public static Specification<Todo> contentContains(String keyword) {
		return (root, query, criteriaBuiler)
				-> criteriaBuiler.like(root.get("content"),"%"+keyword+"%");
	}

}
