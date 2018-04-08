package com.todo.springboot.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.todo.springboot.model.TodoItem;

@RunWith(MockitoJUnitRunner.class)
public class ToDoServiceMicroTest {

	List<TodoItem> items;

	@Mock
	private ToDoServiceImpl serviceToTest;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		serviceToTest = spy(new ToDoServiceImpl());
		items = populateDummyTodoItems();
	}

	@Test
	public void findById() {
		TodoItem findById = serviceToTest.findById(1);
		assertEquals(findById.getTitle(), items.get(0).getTitle());
	}

	@Test
	public void findById_negative() {
		TodoItem findById = serviceToTest.findById(100);
		assertNull(findById);
	}

	@Test
	public void findByTitle() {
		TodoItem findByTitle = serviceToTest.findByTitle("CarWash");
		assert (findByTitle.getTitle().toString().toUpperCase().equalsIgnoreCase("CARWASH"));
	}

	@Test
	public void findByTitle_negative() {
		TodoItem findByTitle = serviceToTest.findByTitle("restaurant");
		assertNull(findByTitle);
	}
	
	@Test
	public void findAllTodoItems() {
		List<TodoItem> findAllTodoItems = serviceToTest.findAllTodoItems();
		int size = findAllTodoItems.size();
		serviceToTest.saveTodoItem(new TodoItem(5, "test add", "n/a", true));
		serviceToTest.findAllTodoItems().size();
		
		assertEquals(size, serviceToTest.findAllTodoItems().size() - 1);
	}

	@Test
	public void saveTodoItem() {
		TodoItem saveTodoItem = serviceToTest.saveTodoItem(getDummyTodoItem());
		assertEquals(saveTodoItem.getTitle(), getDummyTodoItem().getTitle());
	}

	@Test
	public void updateTodoItem() {
		TodoItem todoItem = items.get(1);
		todoItem.setTitle("update title");
		TodoItem updateTodoItem = serviceToTest.updateTodoItem(todoItem);
		assertEquals(updateTodoItem.getTitle(), "update title");
	}

	@Test
	public void updateTodoItem_negative() {
		TodoItem todoItem = new TodoItem(10, "not a todo", "it does not exist", false);
		todoItem.setTitle("update title");
		TodoItem updateTodoItem = serviceToTest.updateTodoItem(todoItem);
		assertNull(updateTodoItem);
	}

	@Test
	public void deleteTodoItemById() {
		int sizeBeforeDelete = serviceToTest.findAllTodoItems().size();
		serviceToTest.deleteTodoItemById(3);
		int sizeAfterDelete = serviceToTest.findAllTodoItems().size();
		assertNotEquals(sizeBeforeDelete, sizeAfterDelete);
	}

	@Test
	public void isTodoItemExist() {
		TodoItem savedItem = serviceToTest.saveTodoItem(new TodoItem(6, "test add 2", "n/a", true));
		boolean todoItemExist = serviceToTest.isTodoItemExist(savedItem);
		assertEquals(todoItemExist, true);
	}

	private static List<TodoItem> populateDummyTodoItems() {
		List<TodoItem> todoItems = new ArrayList<TodoItem>();
		todoItems.add(new TodoItem(1, "meeting with Steve", "meet in the office", false));
		todoItems.add(new TodoItem(2, "go to the grocery store", "buy meat, milk and eggs", false));
		todoItems.add(new TodoItem(3, "pay the phone bill", "pay by credit card both phones", true));
		todoItems.add(new TodoItem(4, "CarWash", "special cleaning", false));
		return todoItems;
	}

	private TodoItem getDummyTodoItem() {
		return new TodoItem(1, "test todo", "this is a test", false);
	}
}
