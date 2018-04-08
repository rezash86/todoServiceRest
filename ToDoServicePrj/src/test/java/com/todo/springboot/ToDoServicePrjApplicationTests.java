package com.todo.springboot;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.todo.springboot.model.TodoItem;

//This test needs to be run after running the project with springboot
public class ToDoServicePrjApplicationTests {

	public static final String REST_SERVICE_URI = "http://localhost:8080/todoRest/api/";
    
    /* GET */
    @SuppressWarnings("unchecked")
    private static void listAllToDoItems(){
        System.out.println("Testing listAllToDoItems API-----------");
         
        RestTemplate restTemplate = new RestTemplate();
        List<LinkedHashMap<String, Object>> todoItemsMap = restTemplate.getForObject(REST_SERVICE_URI+"/todo/", List.class);
         
        if(todoItemsMap!=null){
            for(LinkedHashMap<String, Object> map : todoItemsMap){
                System.out.println("ToDoItem : id="+map.get("id")+", Title="+map.get("title")+", Desciption="+map.get("description")+", IsCompleted="+map.get("completed"));;
            }
        }else{
            System.out.println("No ToDoItem exist----------");
        }
    }
     
    /* GET */
    private static void getToDoItem(){
        System.out.println("Testing getTodoItem API----------");
        RestTemplate restTemplate = new RestTemplate();
        TodoItem todoItem = restTemplate.getForObject(REST_SERVICE_URI+"/todo/1", TodoItem.class);
        System.out.println(todoItem);
    }
     
    /* GET by title */
    private static void getToDoItemByTitle(){
        System.out.println("Testing getTodoItem by title API----------");
        RestTemplate restTemplate = new RestTemplate();
        TodoItem todoItem = restTemplate.getForObject(REST_SERVICE_URI+"/title/todo/?title=meeting with Steve", TodoItem.class);
        System.out.println(todoItem);
    }
    
    /* POST */
    private static void createTodoItem() {
        System.out.println("Testing create ToDoItem API----------");
        RestTemplate restTemplate = new RestTemplate();
        TodoItem todoItem = new TodoItem(0, "wash car", "special service car wash", false);
        TodoItem todoItemInserted = restTemplate.postForObject(REST_SERVICE_URI+"/todo/", todoItem, TodoItem.class);
        System.out.println(todoItemInserted);
    }
 
    /* PUT */
    private static void updateTodoItem() {
        System.out.println("Testing update ToDoItem API----------");
        RestTemplate restTemplate = new RestTemplate();
        TodoItem todoItem  = new TodoItem(0, "wash car", "special service car wash", false);
        restTemplate.put(REST_SERVICE_URI+"/todo/1", todoItem);
        System.out.println(todoItem);
    }
 
    /* DELETE */
    private static void deleteTodoItem() {
        System.out.println("Testing delete ToDoItem API----------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(REST_SERVICE_URI+"/todo/3");
    }
 
    /* DELETE */
    private static void deleteAllTodoItems() {
        System.out.println("Testing all delete ToDoItems API----------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(REST_SERVICE_URI+"/todo/");
    }
 
    public static void main(String args[]){
    	listAllToDoItems();
    	getToDoItem();
    	getToDoItemByTitle();
    	createTodoItem();
        listAllToDoItems();
        updateTodoItem();
        listAllToDoItems();
        deleteTodoItem();
        listAllToDoItems();
        deleteAllTodoItems();
        System.out.println("No other todos... please run again the springboot");
    }

}
