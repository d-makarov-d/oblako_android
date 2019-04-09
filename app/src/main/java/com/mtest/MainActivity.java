package com.mtest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.scalified.fab.ActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private CustomAdapter mAdapter;
    private ArrayList<Project> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new CustomAdapter(this);
        updateList();

        setListAdapter(mAdapter);

        setContentView(R.layout.activity_main);

        ActionButton actionButton = (ActionButton) findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                String[] projectNames = new String[projects.size()];
                for (int i = 0; i < projectNames.length; i++){
                    projectNames[i] = projects.get(i).getTitle();
                }
                Bundle bundle = new Bundle();
                bundle.putStringArray("projects", projectNames);
                intent.putExtras(bundle);
                MainActivity.this.startActivityForResult(intent, 1);
            }
        });

        syncDB();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || resultCode == 0) {

        }else {
            String[] ans = data.getExtras().getStringArray("todo");
            Project selected = findProject(ans[0]);
            if (selected != null) selected.addTodo(ans[1]);
            updateList();
        }
    }

    private Project findProject(String name){
        Project ans = null;
        for (Project project : projects){
            if (project.getTitle().equals(name)) {
                ans = project;
                break;
            }
        }
        return ans;
    }

    private void updateList(){
        mAdapter.flush();
        for (Project project : projects){
            mAdapter.addSectionHeaderItem(project.getTitle());
            for (Todo todo : project.getTodos()){
                mAdapter.addItem(todo);
            }
        }
    }

    boolean syncDB(){
        final MainActivity context = this;
        Ion.with(this)

                .load(getString(R.string.request))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {

                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result != null) {
                            projects.clear();
                            for (final JsonElement projectJsonElement : result) {
                                JsonObject object = projectJsonElement.getAsJsonObject();
                                Project project = new Project(
                                        object.get("id").getAsString(),
                                        object.get("title").getAsString(),
                                        context);
                                for (JsonElement todo : object.getAsJsonArray("todos")){
                                    JsonObject todo_obj = todo.getAsJsonObject();
                                    project.addTodo(
                                            todo_obj.get("text").getAsString(),
                                            todo_obj.get("id").getAsString(),
                                            todo_obj.get("isCompleted").getAsBoolean());
                                }
                                projects.add(project);
                            }
                            updateList();
                        }
                    }
                });
        return true;
    }
}
