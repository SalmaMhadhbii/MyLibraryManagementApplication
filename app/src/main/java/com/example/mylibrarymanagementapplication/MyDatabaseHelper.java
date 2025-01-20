package com.example.mylibrarymanagementapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "BookLibrary.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PAGES = "book_pages";
    private static final String COLUMN_DATE = "date_added";


    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //creation de table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Requête SQL pour créer la table avec les colonnes définies
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PAGES + " INTEGER, " +
                COLUMN_DATE + " INTEGER);";
        db.execSQL(query);// Exécution de la requête SQL pour créer la table
    }

    // Méthode appelée lors de la mise à jour de la base de données (par exemple, changement de version)
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Méthode pour ajouter un livre à la base de données
    void addBook(String title, String author, int pages){
        SQLiteDatabase db = this.getWritableDatabase(); // Obtient une instance en écriture de la base de données
        ContentValues cv = new ContentValues(); // Crée un objet ContentValues pour stocker les valeurs à insérer

        // Ajout des valeurs dans ContentValues
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_DATE, System.currentTimeMillis()); // Stocke l'horodatage actuel (date d'ajout)

        // Insère les valeurs dans la table et vérifie si l'insertion a échoué
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show(); // Échec de l'insertion
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show(); // Succès de l'insertion
        }
    }

    // Méthode pour lire toutes les données de la table
    Cursor readAllData(){
        // Requête pour sélectionner toutes les données de la table
        String query = "SELECT * FROM " + TABLE_NAME;

        // Obtient une instance en lecture seule de la base de données
        SQLiteDatabase db = this.getReadableDatabase();

        // Déclare un objet Cursor pour stocker le résultat de la requête
        Cursor cursor = null;

        // Exécute la requête et obtient les résultats sous forme de curseur
        if(db != null){
            cursor = db.rawQuery(query, null); // Exécute la requête et assigne le résultat au curseur
        }//renvoie un Cursor qui pointe vers la première ligne des résultats.

        return cursor; // Retourne le curseur contenant les résultats
    }

    void updateData(String row_id, String title, String author, String pages){
        SQLiteDatabase db = this.getWritableDatabase();
        // Récupère une instance modifiable de la base de données en utilisant getWritableDatabase().
        // Cette instance permet de modifier la base de données, comme pour effectuer des mises à jour.


        ContentValues cv = new ContentValues();
        // Crée un objet ContentValues pour stocker les paires clé-valeur des données que l'on souhaite mettre à jour dans la base de données.
        // Chaque clé correspond à un nom de colonne, et la valeur est la nouvelle donnée pour cette colonne.

        // Ajoute les nouvelles valeurs dans ContentValues, associant les colonnes aux valeurs à mettre à jour.
        cv.put(COLUMN_TITLE, title); // Met à jour le titre du livre
        cv.put(COLUMN_AUTHOR, author); // Met à jour l'auteur du livre
        cv.put(COLUMN_PAGES, pages); // Met à jour le nombre de pages du livre

        // Effectue une mise à jour dans la table, en utilisant "_id=?" comme condition WHERE pour identifier la ligne à mettre à jour.
        // new String[]{row_id} contient la valeur de l'ID de la ligne que vous souhaitez mettre à jour. Cette valeur sera utilisée à la place du ? dans la condition "_id=?". Cela permet de cibler une ligne précise en fonction de son ID. Ici, row_id est l'ID de la ligne à mettre à jour.
        // La méthode update() renvoie le nombre de lignes affectées. Si aucune ligne n'est modifiée, cela retournera -1.
        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

        // Vérifie si la mise à jour a échoué (result = -1) ou a réussi.
        if(result == -1){
            // Affiche un message d'erreur si la mise à jour a échoué.
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            // Affiche un message de succès si la mise à jour a réussi.
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }


    // Méthode pour supprimer une ligne de la table en fonction de son ID
    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase(); // Obtient une instance en écriture de la base de données
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id}); // Supprime la ligne avec l'ID donné
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show(); // Échec de la suppression
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show(); // Succès de la suppression
        }
    }


    // Méthode pour obtenir le dernier livre ajouté, en fonction de la date
    public String getLatestBook() {
        SQLiteDatabase db = this.getReadableDatabase(); // Obtient une instance en lecture seule de la base de données
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " DESC LIMIT 1"; // Sélectionne le dernier livre ajouté
        Cursor cursor = db.rawQuery(query, null);

        // Si aucune donnée n'est trouvée, retourne un message par défaut
        if (cursor.getCount() == 0) return "No books found.";

        cursor.moveToFirst(); // Se déplace vers la première ligne du curseur
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE));

        // Formate la date d'ajout en un format lisible
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date(date));

        cursor.close(); // Ferme le curseur
        return "Latest Book: Title: " + title + " Author: " + author + " Date Added: " + formattedDate;
    }

    // Method to get the latest date timestamp from the table
    public long getLatestDateTimestamp() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(" + COLUMN_DATE + ") as latest_date FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        long latestTimestamp = 0;
        if (cursor.moveToFirst()) {
            latestTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow("latest_date"));
        }
        cursor.close();
        return latestTimestamp;
    }

}
