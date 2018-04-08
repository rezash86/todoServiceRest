package com.todo.springboot.controller;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.todo.springboot.model.TodoItem;
import com.todo.springboot.service.ToDoService;
import com.todo.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
public class RestApiController {
	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	ToDoService toDoService;

	// Retrieve All Todos
	@RequestMapping(value = "/todo/", method = RequestMethod.GET)
	public ResponseEntity<List<TodoItem>> listAllTodos() {
		List<TodoItem> todoItems = getService().findAllTodoItems();
		if (todoItems.isEmpty()) {
			return new ResponseEntity<List<TodoItem>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<TodoItem>>(todoItems, HttpStatus.OK);
	}

	// Retrieve Single Todo
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getTodoItem(@PathVariable("id") long id) {
		logger.info("Fetching TodoItem with id {}", id);
		TodoItem toDoItem = getService().findById(id);
		if (toDoItem == null) {
			logger.error("TodoItem with id {} not found.", id);
			return new ResponseEntity<Object>(new CustomErrorType("TodoItem with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TodoItem>(toDoItem, HttpStatus.OK);
	}

	// Retrieve Single Todo
	@RequestMapping(value = "/title/todo/", method = RequestMethod.GET)
	public ResponseEntity<?> getTodoItemByName(@QueryParam(value = "title") String title) {
		logger.info("Fetching TodoItem with title {}", title);
		TodoItem toDoItem = getService().findByTitle(title);
		if (toDoItem == null) {
			logger.error("TodoItem with title {} not found.", title);
			return new ResponseEntity<Object>(new CustomErrorType("TodoItem with title " + title + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<TodoItem>(toDoItem, HttpStatus.OK);
	}

	// Create a Todo
	@RequestMapping(value = "/todo/", method = RequestMethod.POST)
	public ResponseEntity<?> createTodo(@RequestBody TodoItem todoItem) {
		logger.info("Creating ToDoItem : {}", todoItem);

		if (getService().isTodoItemExist(todoItem)) {
			logger.error("Unable to create. A ToDo Item with title {} already exist", todoItem.getTitle());
			return new ResponseEntity<Object>(
					new CustomErrorType(
							"Unable to create. A ToDoItem with title: " + todoItem.getTitle() + " already exist."),
					HttpStatus.CONFLICT);
		}
		TodoItem savedTodoItem = getService().saveTodoItem(todoItem);

		return new ResponseEntity<TodoItem>(savedTodoItem, HttpStatus.CREATED);
	}

	// Update a Todo
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTodoItem(@PathVariable("id") long id, @RequestBody TodoItem todoItem) {
		logger.info("Updating ToDoItem with id {}", id);

		TodoItem currentTodoItem = getService().findById(id);

		if (currentTodoItem == null) {
			logger.error("Unable to update. ToDo Item with id {} not found.", id);
			return new ResponseEntity<Object>(
					new CustomErrorType("Unable to upate. To do Item with id " + id + " does not exist."),
					HttpStatus.NOT_FOUND);
		}

		currentTodoItem.setTitle(todoItem.getTitle());
		currentTodoItem.setDescription(todoItem.getDescription());
		currentTodoItem.setCompleted(todoItem.isCompleted());

		TodoItem updateTodoItem = getService().updateTodoItem(currentTodoItem);
		if (updateTodoItem != null) {
			return new ResponseEntity<TodoItem>(updateTodoItem, HttpStatus.OK);
		}
		return new ResponseEntity<TodoItem>(updateTodoItem, HttpStatus.NOT_FOUND);
	}

	// Delete a Todo
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTodoItem(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting ToDoItem with id {}", id);

		TodoItem todoItem = getService().findById(id);
		if (todoItem == null) {
			logger.error("Unable to delete. ToDoItem with id {} not found.", id);
			return new ResponseEntity<Object>(
					new CustomErrorType("Unable to delete. ToDoItem with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		getService().deleteTodoItemById(id);
		return new ResponseEntity<TodoItem>(HttpStatus.NO_CONTENT);
	}

	// Delete All Todos
	@RequestMapping(value = "/todo/", method = RequestMethod.DELETE)
	public ResponseEntity<TodoItem> deleteAllTodoItems() {
		logger.info("Deleting All ToDoItems");

		getService().deleteAllTodoItems();
		return new ResponseEntity<TodoItem>(HttpStatus.NO_CONTENT);
	}

	// for MicroTest usage
	protected ToDoService getService() {
		return toDoService;
	}
}
