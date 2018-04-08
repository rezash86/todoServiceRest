package com.todo.springboot.service;

import java.util.List;

import com.todo.springboot.model.TodoItem;
	
public interface ToDoService {

	TodoItem findById(long id);
	
	TodoItem findByTitle(String title);
	
	TodoItem saveTodoItem(TodoItem todoItem);
	
	TodoItem updateTodoItem(TodoItem todoItem);
	
	void deleteTodoItemById (long id);

	List<TodoItem> findAllTodoItems();
	
	void deleteAllTodoItems();
	
	boolean isTodoItemExist(TodoItem todoItem);
}
