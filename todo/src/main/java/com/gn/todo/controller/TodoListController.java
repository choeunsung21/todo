package com.gn.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class TodoListController {
	
	private final TodoService service;
	
	@GetMapping({"","/"})
	public String todoList(Model model) {
		
		List<Todo> resultList = service.selectTodoAll();
		model.addAttribute("todoList", resultList);
		
		return "todoList";
	}
	
	@PostMapping("/addTodo")
	@ResponseBody
	public Map<String, String> addTodoApi(TodoDto dto) {
		System.out.println(dto);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할 일 추가 중 오류 발생");
		
		int result = service.addTodo(dto);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할 일 추가 완료");
		}
		
		return resultMap;
	}
}
