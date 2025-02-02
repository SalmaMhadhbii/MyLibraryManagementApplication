package com.example.mylibrarymanagementapplication;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import java.util.ArrayList;

public class ReservedBooksActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    CustomAdapter customAdapter;
    MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_books);

        recyclerView = findViewById(R.id.recyclerView);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        myDB = new MyDatabaseHelper(this);

        storeReservedBooksDataInArrays();  // Charge les données des livres réservés

        customAdapter = new CustomAdapter(this, this, book_id, book_title, book_author, book_pages);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void storeReservedBooksDataInArrays() {
        Cursor cursor = myDB.readReservedBooks();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
        } else {
            Toast.makeText(this, "No reserved books found", Toast.LENGTH_SHORT).show();
        }

        cursor.close();  // N'oubliez pas de fermer le curseur
    }
}
