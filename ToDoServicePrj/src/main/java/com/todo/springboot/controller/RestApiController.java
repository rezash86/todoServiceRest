package com.todo.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.todo.springboot.model.TodoItem;
import com.todo.springboot.service.ToDoService;
import com.todo.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
public class RestApiController {
	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	ToDoService toDoService;

	// Retrive All Todos
	@RequestMapping(value = "/todo/", method = RequestMethod.GET)
	public ResponseEntity<List<TodoItem>> listAllTodos() {
		List<TodoItem> todoItems = toDoService.findAllTodoItems();
		if (todoItems.isEmpty()) {
			return new ResponseEntity<List<TodoItem>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<TodoItem>>(todoItems, HttpStatus.OK);
	}

	// Retrieve Single Todo
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getToDoItem(@PathVariable("id") long id) {
		logger.info("Fetching TodoItem with id {}", id);
		TodoItem toDoItem = toDoService.findById(id);
		if (toDoItem == null) {
			 logger.error("TodoItem with id {} not found.", id);
			 return new ResponseEntity<Object>(new CustomErrorType("TodoItem with id " + id
			 + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TodoItem>(toDoItem, HttpStatus.OK);
	}

	// Create a Todo
	@RequestMapping(value = "/todo/", method = RequestMethod.POST)
	public ResponseEntity<?> createToDo(@RequestBody TodoItem todoItem, UriComponentsBuilder ucBuilder) {
		logger.info("Creating ToDoItem : {}", todoItem);

		if (toDoService.isTodoItemExist(todoItem)) {
			 logger.error("Unable to create. A ToDo Item with title {} already exist",
			 todoItem.getTitle());
			 return new ResponseEntity<Object>(new CustomErrorType("Unable to create. A ToDoItem with title: " +
					 todoItem.getTitle() + " already exist."),HttpStatus.CONFLICT);
		}
		toDoService.saveTodoItem(todoItem);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/todo/{id}").buildAndExpand(todoItem.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// Update a Todo

	@RequestMapping(value = "/todo/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTodoItem(@PathVariable("id") long id, @RequestBody TodoItem todoItem) {
		logger.info("Updating ToDoItem with id {}", id);

		TodoItem currentTodoItem = toDoService.findById(id);

		if (currentTodoItem == null) {
			 logger.error("Unable to update. ToDo Item with id {} not found.", id);
			 return new ResponseEntity<Object>(new CustomErrorType("Unable to upate. To do Item with id" +
					 id + " already exist."), HttpStatus.NOT_FOUND);
		}

		currentTodoItem.setTitle(todoItem.getTitle());
		currentTodoItem.setDescription(todoItem.getDescription());
		currentTodoItem.setCompleted(todoItem.isCompleted());

		toDoService.updateTodoItem(currentTodoItem);
		return new ResponseEntity<TodoItem>(currentTodoItem, HttpStatus.OK);
	}

	// Delete a Todo
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTodoItems(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting ToDoItem with id {}", id);

		TodoItem todoItem = toDoService.findById(id);
		if (todoItem == null) {
			logger.error("Unable to delete. ToDoItem with id {} not found.", id);
			return new ResponseEntity<Object>(new CustomErrorType("Unable to delete. ToDoItem with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		toDoService.deleteTodoItemById(id);
		return new ResponseEntity<TodoItem>(HttpStatus.NO_CONTENT);
	}

	// Delete All Todos
	@RequestMapping(value = "/todo/", method = RequestMethod.DELETE)
	public ResponseEntity<TodoItem> deleteAllTodoItems() {
		logger.info("Deleting All ToDoItems");

		toDoService.deleteAllTodoItems();
		return new ResponseEntity<TodoItem>(HttpStatus.NO_CONTENT);
	}
}
