package com.todo.springboot.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import com.todo.springboot.model.TodoItem;

@Service("todoService")
public class ToDoServiceImpl implements ToDoService {

	private static final AtomicLong counter = new AtomicLong();

	private static List<TodoItem> todoItems;

	static {
		todoItems = populateDummyTodoItems();
	}

	@Override
	public TodoItem findById(long id) {
		for (TodoItem todItem : todoItems) {
			if (todItem.getId() == id) {
				return todItem;
			}
		}
		return null;
	}

	@Override
	public TodoItem findByTitle(String title) {
		for (TodoItem todItem : todoItems) {
			if (todItem.getTitle().equalsIgnoreCase(title)) {
				return todItem;
			}
		}
		return null;
	}

	@Override
	public TodoItem saveTodoItem(TodoItem todoItem) {
		todoItem.setId(counter.incrementAndGet());
		todoItems.add(todoItem);
		return todoItem;
	}

	@Override
	public TodoItem updateTodoItem(TodoItem todoItem) {
		TodoItem findById = findById(todoItem.getId());
		if(findById !=null) {
			Optional<TodoItem> findFirst = todoItems.stream().filter(t-> t.getId() == findById.getId()).findFirst();
			if(findFirst.isPresent()) {
				int index = todoItems.indexOf(findFirst.get());
				todoItems.set(index, todoItem);
				return todoItem;
			}
			else {
				return null;
			}
		}
		return null;
	}

	@Override
	public void deleteTodoItemById(long id) {
		for (Iterator<TodoItem> iterator = todoItems.iterator(); iterator.hasNext();) {
			TodoItem todoItem = iterator.next();
			if (todoItem.getId() == id) {
				iterator.remove();
			}
		}
	}

	@Override
	public List<TodoItem> findAllTodoItems() {
		return todoItems;
	}

	@Override
	public void deleteAllTodoItems() {
		todoItems.clear();
	}

	@Override
	public boolean isTodoItemExist(TodoItem todoItem) {
		return findByTitle(todoItem.getTitle()) != null;
	}

	private static List<TodoItem> populateDummyTodoItems() {
		List<TodoItem> todoItems = new ArrayList<TodoItem>();
		todoItems.add(new TodoItem(counter.incrementAndGet(), "meeting with Steve", "meet in the office", false));
		todoItems.add(
				new TodoItem(counter.incrementAndGet(), "go to the grocery store", "buy meat, milk and eggs", false));
		todoItems.add(
				new TodoItem(counter.incrementAndGet(), "pay the phone bill", "pay by credit card both phones", true));
		todoItems.add(new TodoItem(counter.incrementAndGet(), "CarWash", "special cleaning", false));
		return todoItems;
	}

}
