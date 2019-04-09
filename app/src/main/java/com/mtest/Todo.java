package com.mtest;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

public class Todo {
    private String text;
    private boolean isComplete;
    private Project project;
    private String id;

    Todo(String id, String text, boolean isComplete, Project project){
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
        this.project = project;
    }

    Todo(String text, final Project project){
        this.text = text;
        this.isComplete = false;
        this.project = project;

        JsonObject params = new JsonObject();
        params.addProperty("text", text);
        params.addProperty("project_id", project.getId());

        Ion.with(project.getContext())
                .load("POST", project.getContext().getString(R.string.request))
                .setJsonObjectBody(params)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        ((MainActivity) project.getContext()).syncDB();
                    }
                });
    }

    void setComplete(Boolean complete){
        isComplete = complete;
        JsonObject params = new JsonObject();
        params.addProperty("todo_id", id);

        Ion.with(project.getContext())
                .load("PATCH", project.getContext().getString(R.string.request))
                .setJsonObjectBody(params)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        ((MainActivity) project.getContext()).syncDB();

                    }
                });
    }

    String getText(){
        return text;
    }

    boolean getComplete(){
        return isComplete;
    }
}
