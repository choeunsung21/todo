package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.TodoRepository;
import com.gn.todo.specification.TodoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	
	private final TodoRepository repository;
	
	public int addTodo(TodoDto dto) {
		int result = 0;
		try {
			Todo entity = dto.toEntity();
			Todo saved = repository.save(entity);
			Long no = saved.getNo();
			
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Page<Todo> selectTodoAll(SearchDto searchDto, PageDto pageDto) {
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage() - 1, pageDto.getNumPerPage(),
				Sort.by("no").ascending());
		
		Specification<Todo> spec = (root, query, criteriaBuilder) -> null;
		if(searchDto.getSearch_text()==null) {
			searchDto.setSearch_text("");
		}
		spec = spec.and(TodoSpecification.contentContains(searchDto.getSearch_text()));
		Page<Todo> resultList = repository.findAll(spec, pageable);
		return resultList;
	}

	public int deleteTodo(Long id) {
		int result = 0;
		try {
			Todo target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
			}
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Todo selectOneTodo(Long id) {
		return repository.findById(id).orElse(null);
	}

	public int updateTodo(Todo todo) {
		int result = 0;
		try {
			repository.save(todo);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;
	}

}
