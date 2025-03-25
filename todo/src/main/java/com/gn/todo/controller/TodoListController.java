package com.gn.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TodoListController {
	
	@GetMapping({"","/"})
	public String todoList() {
		return "todoList";
	}
	
	@PostMapping("/addTodo")
	@ResponseBody
	public String addTodoApi() {
		
		return null;
	}
}
