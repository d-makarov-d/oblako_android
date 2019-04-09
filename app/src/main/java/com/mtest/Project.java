package com.mtest;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private ArrayList<Todo> todos = new ArrayList<>();
    private String title;
    private String id;
    private MainActivity context;

    Project(String id, String title, MainActivity context){
        this.id = id;
        this.title = title;
        this.context = context;
    }

    void addTodo(String name){
        Todo todo = new Todo(name, this);
        todos.add(todo);
    }

    void addTodo(String name, String todo_id, boolean isComplete){
        Todo todo = new Todo(todo_id, name, isComplete, this);
        todos.add(todo);
    }

    Context getContext(){
        return context;
    }
    List<Todo> getTodos(){
        return todos;
    }
    String getId() {
        return id;
    }

    String getTitle(){
        return title;
    }
}
