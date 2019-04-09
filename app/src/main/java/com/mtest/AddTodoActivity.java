package com.mtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTodoActivity extends AppCompatActivity {

    Spinner spinner;
    EditText text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        final SelectCustomAdapter adapter = new SelectCustomAdapter(this,
                R.layout.spinner_item,
                this.getIntent().getExtras().getStringArray("projects"));

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setText(adapter.getObj(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        text = findViewById(R.id.editText);

        Button cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });

        Button submit = findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArray("todo",
                        new String[]{
                                spinner.getSelectedItem().toString(),
                                text.getText().toString()});
                intent.putExtras(bundle);
                setResult(1, intent);
                finish();
            }
        });
    }
}
