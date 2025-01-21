package com.example.mylibrarymanagementapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "BookLibrary.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PAGES = "book_pages";
    private static final String COLUMN_DATE = "date_added"; // New column for date

    private static final String COLUMN_STATUS = "status"; // Nouvelle colonne renommée



    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PAGES + " INTEGER, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_STATUS + " TEXT DEFAULT 'Not Reserved');"; // Valeur par défaut 'Not Reserved'
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) { // Supposons que la version actuelle est maintenant 3
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_STATUS + " TEXT DEFAULT 'Not Reserved';");
        }
    }

    void addBook(String title, String author, int pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_DATE, System.currentTimeMillis());
        // Pas besoin de spécifier COLUMN_STATUS, il sera automatiquement défini à 'Not Reserved'

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }


    public void reserveBook(String title, String author, int pages) {
        // Ici, vous devrez insérer les données du livre réservé dans la base de données
        // Exemple d'insertion SQL :

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("author", author);
        contentValues.put("pages", pages);

        // Insérer la réservation dans une table spécifique (ex. "reserved_books")
        long result = db.insert("reserved_books", null, contentValues);

        if (result == -1) {
            // Si l'insertion échoue, afficher un message d'erreur
            Log.e("Database Error", "Failed to reserve book");
        } else {
            // Si l'insertion réussit, afficher un message de succès
            Log.d("Database", "Book reserved successfully");
        }
    }

    public Cursor getReservedBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Récupérer toutes les réservations
        return db.rawQuery("SELECT * FROM reserved_books", null);
    }


    Cursor readAllData(){
        //a query to select all the data from db table
        String query = "SELECT * FROM " + TABLE_NAME;

        // Get a readable instance of the database.
        SQLiteDatabase db = this.getReadableDatabase();

        // Declare a Cursor object to hold the result of the query.
        Cursor cursor = null;

        // If the database instance is not null, execute the query.
        if(db != null){
            cursor = db.rawQuery(query, null); // Execute the query and assign the result to the cursor.
        }

        // Return the cursor containing the data.
        return cursor;
    }

    void updateData(String row_id, String title, String author, String pages){
        //retrieves a writable database instance using getWritableDatabase(). It allows modifications to the database, such as updates.
        SQLiteDatabase db = this.getWritableDatabase();
        //store the key-value pairs of the data that you want to update in the database. Each key corresponds to a column name, and the value is the new data for that column.
        ContentValues cv = new ContentValues();
        //COLUMN_TITLE, COLUMN_AUTHOR, and COLUMN_PAGES represent the column names in the database table.
        //title, author, and pages are the values being updated.
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);

        //"_id=?": The WHERE clause, indicating which row(s) to update. Here, it specifies that the row with the ID equal to row_id should be updated.
        //new String[]{row_id}: An array of arguments for the WHERE clause (in this case, row_id).
        //update returns the number of rows affected, and the result is stored in result.
        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get the latest book added based on the date
    public String getLatestBook() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0) return "No books found.";

        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date(date));

        cursor.close();
        return "Latest Book:Title: " + title + "Author: " + author + "Date Added: " + formattedDate;
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
