package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class TodoListController {
	
	private final TodoService service;
	
	@GetMapping({"","/"})
	public String searchTodo(Model model, SearchDto searchDto, PageDto pageDto) {

		if (pageDto.getNowPage() == 0)
			pageDto.setNowPage(1);

		Page<Todo> resultList = service.selectTodoAll(searchDto, pageDto);
		pageDto.setTotalPage(resultList.getTotalPages());
		
		model.addAttribute("todoList", resultList);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("pageDto", pageDto);
		
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
