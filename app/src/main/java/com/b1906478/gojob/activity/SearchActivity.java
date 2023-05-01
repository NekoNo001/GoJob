package com.b1906478.gojob.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.b1906478.gojob.R;
import com.b1906478.gojob.databinding.ActivityFindBinding;
import com.b1906478.gojob.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUp();

        EditText editText = findViewById(R.id.txtSearch);
        Button btnSearch = findViewById(R.id.btnsearch);
        ListView listView = findViewById(R.id.listView);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> searchHistorySet = sharedPreferences.getStringSet("search_history", new HashSet<>());
        List<String> searchHistoryList = new ArrayList<>(searchHistorySet);
        Collections.sort(searchHistoryList, Collections.reverseOrder()); // reverse the list to show most recent search first
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchHistoryList);
        listView.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchActivity.this,findActivity.class);
                String searchString = editText.getText().toString();
                searchHistorySet.add(searchString);
                sharedPreferences.edit().putStringSet("search_history" , searchHistorySet).apply();
                i.putExtra("SearchString",searchString.toLowerCase());
                Toast.makeText(SearchActivity.this,editText.getText().toString(), Toast.LENGTH_SHORT).show();
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    btnSearch.performClick();
                    return true;
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Start find activity with selected search string
                String item = (String) adapterView.getItemAtPosition(position);
                Intent i = new Intent(SearchActivity.this, findActivity.class);
                i.putExtra("SearchString", item);
                startActivity(i);
            }
        });
    }



    private void setUp() {
        ImageView leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textView = findViewById(R.id.txtToolbar);
        textView.setText(R.string.notification);
    }
}