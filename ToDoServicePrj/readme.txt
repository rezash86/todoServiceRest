Please follow the steps for running the project

1- Use Maven to build the project
2- Run project by springboot
3- The path for rest services 
	- GET ALL      : http://localhost:8080/todoRest/api/todo/ --> gets all the todos.
	- GET By Id    : http://localhost:8080/todoRest/api/todo/{id} -- {id} the id of todo... 
	- GET by title : http://localhost:8080/todoRest/api/todo/title/?title=meeting with Steve -- QueryParam {title}
	- POST   : http://localhost:8080/todoRest/api/todo/
			BODY:
			{
	        	"title": "test to do",
    	    	"description": "this is a test",
        		"completed": false
    		}
    		
    -PUT     : http://localhost:8080/todoRest/api/todo/
    		BODY:
			{
	        	"title": "test to do",
    	    	"description": "this is a test",
        		"completed": false
    		} 
    		
    -DELETE BY Id : http://localhost:8080/todoRest/api/todo/{id}
    
    -DELETE ALL : http://localhost:8080/todoRest/api/todo/		
    
    
* After running project by springboot, class 'ToDoServicePrjApplicationTests' can be run as a java application
* This project contains Microtests in 'ToDoServiceRestMicroTest' and 'ToDoServiceMicroTest'