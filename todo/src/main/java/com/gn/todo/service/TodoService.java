package com.gn.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.TodoRepository;

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

	public List<Todo> selectTodoAll() {
		List<Todo> todoList = repository.findAll();
		return todoList;
	}
}
