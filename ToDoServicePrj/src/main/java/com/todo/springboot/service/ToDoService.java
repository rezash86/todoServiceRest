package com.todo.springboot.service;

import java.util.List;

import com.todo.springboot.model.TodoItem;
	
public interface ToDoService {
	
	/**
	 * This is used for finding todos by Id
	 * @param id
	 * @return TodoItem
	 */
	TodoItem findById(long id);
	
	/**
	 * This is used for finding todos by title
	 * @param title
	 * @return TodoItem
	 */
	TodoItem findByTitle(String title);
	
	/**
	 * This is used for save a todo
	 * @param todoItem
	 * @return TodoItem
	 */
	TodoItem saveTodoItem(TodoItem todoItem);
	
	/**
	 * This is used for update an existing todo 
	 * @param todoItem
	 * @return TodoItem
	 */
	TodoItem updateTodoItem(TodoItem todoItem);
	
	/**
	 * This is used for delete a todo by Id  
	 * @param id
	 */
	void deleteTodoItemById (long id);

	/**
	 *  This is used for finding all todos
	 * @return List
	 */
	List<TodoItem> findAllTodoItems();
	
	/**
	 * This is used for deleting all todos 
	 */
	void deleteAllTodoItems();
	
	/**
	 * returns true if todo items exists
	 * @param todoItem
	 * @return true or false
	 */
	boolean isTodoItemExist(TodoItem todoItem);
}
