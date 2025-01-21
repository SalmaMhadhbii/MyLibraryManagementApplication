package com.example.mylibrarymanagementapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReserveActivity extends AppCompatActivity {
    Button reserve_button;
    String id, title, author, pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        // Initialiser les composants de l'interface utilisateur
        reserve_button = findViewById(R.id.reserve_button);

        // Récupérer les données de l'intent
        getAndSetIntentData();

        // Définir le titre de l'ActionBar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        // Ajouter un écouteur au bouton "Reserve"
        reserve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifiez si les données sont valides
                if (title.isEmpty() || author.isEmpty() || pages.isEmpty()) {
                    Toast.makeText(ReserveActivity.this, "No data to reserve!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int pagesCount = Integer.parseInt(pages); // Convertir en entier
                    MyDatabaseHelper myDB = new MyDatabaseHelper(ReserveActivity.this);

                    // Appeler la méthode reserveBook
                    myDB.reserveBook(title, author, pagesCount);

                    // Afficher un Toast après l'opération réussie
                    Toast.makeText(ReserveActivity.this, "Book reserved successfully", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    // Gestion des erreurs de conversion des pages
                    Toast.makeText(ReserveActivity.this, "Invalid number of pages!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Récupérer et définir les données de l'intent
    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title")
                && getIntent().hasExtra("author") && getIntent().hasExtra("pages")) {
            // Récupérer les données de l'intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }
}
