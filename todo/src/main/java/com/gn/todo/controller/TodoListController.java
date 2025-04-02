package com.gn.todo.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Attach;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.AttachService;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class TodoListController {
	
	private final TodoService service;
	private final AttachService attachService;
	
	@GetMapping({"","/"})
	public String searchTodo(Model model, SearchDto searchDto, PageDto pageDto) {
		
		if (pageDto.getNowPage() == 0)
			pageDto.setNowPage(1);

		Page<Todo> resultList = service.selectTodoAll(searchDto, pageDto);
		
		List<Attach> attachList = attachService.selectAttachList();
		model.addAttribute("attachList", attachList);
		
		pageDto.setTotalPage(resultList.getTotalPages());
		
		model.addAttribute("todoList", resultList);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("pageDto", pageDto);
		
		return "todoList";
	}
	
	@GetMapping("/download/{id}")
	@ResponseBody
	public ResponseEntity<Object> fileDownload(@PathVariable("id") Long id) {
		try {
			Attach fileData = attachService.selectAttachOne(id);
			if(fileData == null) {
				return ResponseEntity.notFound().build();
			}
			Path filePath = Paths.get(fileData.getAttachPath());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			
			String oriFileName = fileData.getOriName();
			String encodedName = URLEncoder.encode(oriFileName,StandardCharsets.UTF_8);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedName);
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
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
	
	@DeleteMapping("/{id}/delete")
	@ResponseBody
	public Map<String, String> deleteTodoApi(@PathVariable("id") Long id) {

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "삭제 중 오류가 발생했습니다.");

		int result = service.deleteTodo(id);
		if (result != 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "삭제가 완료되었습니다.");
		}
		return resultMap;
	}
	
	@PostMapping("/{id}/update")
	@ResponseBody
	public Map<String, String> updateTodoApi(@PathVariable("id") Long id) {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "오류가 발생했습니다.");

		Todo todo = service.selectOneTodo(id);
		if (todo != null) {
	        if ("N".equals(todo.getFlag())) {
	            todo.setFlag("Y");
	        } else if ("Y".equals(todo.getFlag())) {
	            todo.setFlag("N");
	        }

	        int result = service.updateTodo(todo);
	        if(result != 0) {
	        	resultMap.put("res_code", "200");
		        resultMap.put("res_msg", "변경 성공");
	        }   
	    }

		return resultMap;

	}
}
