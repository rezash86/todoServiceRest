package com.todo.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.todo.springboot.model.TodoItem;
import com.todo.springboot.service.ToDoService;

@RunWith(MockitoJUnitRunner.class)
public class ToDoServiceRestMicroTest {

	@Mock
	private RestApiController restServiceToTest;

	@Mock
	private ToDoService todoService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		restServiceToTest = spy(new RestApiController());

		doReturn(todoService).when(restServiceToTest).getService();
	}

	@Test
	public void listAllTodos_positive() {
		when(todoService.findAllTodoItems()).thenReturn(getDummyList());
		ResponseEntity<List<TodoItem>> listAllToDos = restServiceToTest.listAllTodos();
		
		TodoItem restodoItem1 = listAllToDos.getBody().get(0);
		assertEquals(restodoItem1, getDummyList().get(0));
		
		TodoItem restodoItem2 = listAllToDos.getBody().get(1);
		assertEquals(restodoItem2, getDummyList().get(1));
		
		assertEquals(listAllToDos.getBody().size(), getDummyList().size());
	}
	
	@Test
	public void listAllTodos_NOTFOUND() {
		when(todoService.findAllTodoItems()).thenReturn(Collections.emptyList());
		ResponseEntity<List<TodoItem>> listAllToDos = restServiceToTest.listAllTodos();
		assert (listAllToDos.getStatusCode() == HttpStatus.NOT_FOUND);
	}

	@Test
	public void getTodoItem() {
		when(todoService.findById(1)).thenReturn(getDummyList().get(1));
		ResponseEntity<?> todoItem = restServiceToTest.getTodoItem(1);
		
		assertEquals(todoItem.getBody(), getDummyList().get(1));
	}
	
	@Test
	public void getTodoItem_NOTFOUND() {
		when(todoService.findById(1)).thenReturn(null);
		ResponseEntity<?> todoItem = restServiceToTest.getTodoItem(1);
		
		assert (todoItem.getStatusCode() == HttpStatus.NOT_FOUND);
	}

	@Test
	public void createTodo() {
		when(todoService.saveTodoItem(getDummyTodoItem())).thenReturn(getDummyTodoItem());
		ResponseEntity<?> createdTodo = restServiceToTest.createTodo(getDummyTodoItem());
		assertEquals(createdTodo.getBody(), getDummyTodoItem());
	}
	
	@Test
	public void createTodo_CONFLICT() {
		when(todoService.isTodoItemExist(getDummyTodoItem())).thenReturn(true);
		ResponseEntity<?> createdTodo = restServiceToTest.createTodo(getDummyTodoItem());
		assert (createdTodo.getStatusCode() == HttpStatus.CONFLICT);
	}

	@Test
	public void updateTodoItem() {
		when(todoService.findById(1)).thenReturn(getDummyTodoItem());
		ResponseEntity<?> updatedTodoItem = restServiceToTest.updateTodoItem(1, getUpdatedDummyTodoItem());
		assertEquals(updatedTodoItem.getBody(), getUpdatedDummyTodoItem());
	}
	
	@Test
	public void updateTodoItem_NOTFOUND() {
		when(todoService.findById(1)).thenReturn(null);
		ResponseEntity<?> updatedTodoItem = restServiceToTest.updateTodoItem(1, getUpdatedDummyTodoItem());
		assert (updatedTodoItem.getStatusCode() == HttpStatus.NOT_FOUND);
	}

	@Test
	public void deleteTodoItem() {
		when(todoService.findById(1)).thenReturn(getDummyTodoItem());
		restServiceToTest.deleteTodoItem(1);
		verify(todoService).deleteTodoItemById(1);
	}
	
	@Test
	public void deleteTodoItem_NOTFOUND() {
		when(todoService.findById(1)).thenReturn(null);
		ResponseEntity<?> deleteTodoItem = restServiceToTest.deleteTodoItem(1);
		assert (deleteTodoItem.getStatusCode() == HttpStatus.NOT_FOUND);
	}

	@Test
	public void deleteAllTodoItems() {
		ResponseEntity<TodoItem> deleteAllTodoItems = restServiceToTest.deleteAllTodoItems();
		verify(todoService).deleteAllTodoItems();
		assert (deleteAllTodoItems.getStatusCode() == HttpStatus.NO_CONTENT);
	}
	
	private List<TodoItem> getDummyList(){
		ArrayList<TodoItem> listTodos = new ArrayList<>();
		listTodos.add(new TodoItem(1, "test1", "testdesc1", false));
		listTodos.add(new TodoItem(2, "test2", "testdesc2", true));
		listTodos.add(new TodoItem(3, "test3", "testdesc3", false));
		listTodos.add(new TodoItem(4, "test4", "testdesc4", true));
		
		return listTodos;
	}
	
	private TodoItem getDummyTodoItem() {
		return new TodoItem(1, "test todo", "this is a test", false);
	}

	private TodoItem getUpdatedDummyTodoItem() {
		return new TodoItem(1, "updated - test todo", "updated - this is a test", false);
	}
}
