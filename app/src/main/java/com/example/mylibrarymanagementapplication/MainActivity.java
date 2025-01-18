package com.example.mylibrarymanagementapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;


    MyDatabaseHelper myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        // Navigate to AddActivity when add_button is clicked
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        //A MyDatabaseHelper instance (myDB) is created to interact with the database.
        myDB = new MyDatabaseHelper(MainActivity.this);
        //Four ArrayList objects are initialized to hold data for each book's ID, title, author, and number of pages.
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();

        // This method (storeDataInArrays()) is called to fetch data from the database and fill the ArrayLists (book_id, book_title, book_author, and book_pages).
        storeDataInArrays();

        //A custom adapter (CustomAdapter) is created and set to the RecyclerView. This adapter will take the data (the book info from the ArrayLists) and populate the RecyclerView items.
        customAdapter = new CustomAdapter(MainActivity.this,this, book_id, book_title, book_author, book_pages);
        recyclerView.setAdapter(customAdapter);
        //A LinearLayoutManager is used to arrange the items in a vertical list.
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    //storeDataInArrays() is responsible for fetching data from a database and storing it in array lists so that the data can later be displayed in a RecyclerView (or similar component).
    //cursor=set of rows from a database query result.
    //A query like SELECT * FROM books would return a Cursor that points to the first row.
    //When you call cursor.moveToNext(), it moves the cursor to the second row, and so on.
    //You can then access the data in the current row, like cursor.getString(1) to get the book_title.
    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this,"No data", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                // Retrieves the value of the first column (assumed to be the book_id) from the current row and adds it to the book_id array list.
                book_id.add(cursor.getString(0));
                //Similarly, this retrieves the second column (assumed to be book_title) from the current row and adds it to the book_title array list.
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear the current data lists to avoid duplicating items
        book_id.clear();
        book_title.clear();
        book_author.clear();
        book_pages.clear();
        // Re-fetch data from the database
        storeDataInArrays();
        // Notify the adapter that the data has changed
        customAdapter.notifyDataSetChanged();
    }

}