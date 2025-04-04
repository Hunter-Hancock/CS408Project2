package edu.jsu.mcis.cs408.crosswordmagic.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.opencsv.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.model.Puzzle;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import edu.jsu.mcis.cs408.crosswordmagic.model.Word;
import edu.jsu.mcis.cs408.crosswordmagic.model.WordDirection;

public class PuzzleDAO {

    private final DAOFactory daoFactory;

    PuzzleDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /* add a new puzzle entry to the database */

    public int create(Puzzle newPuzzle) {

        /* use this method if there is NOT already a SQLiteDatabase open */

        SQLiteDatabase db = daoFactory.getWritableDatabase();
        int result = create(db, newPuzzle);
        db.close();
        return result;

    }

    public int create(SQLiteDatabase db, Puzzle newPuzzle) {

        int key = 0;

        /* use this method if there IS already a SQLiteDatabase open */

        String name = daoFactory.getProperty("sql_field_name");
        String description = daoFactory.getProperty("sql_field_description");
        String height = daoFactory.getProperty("sql_field_height");
        String width = daoFactory.getProperty("sql_field_width");

        ContentValues values = new ContentValues();
        values.put(name, newPuzzle.getName());
        values.put(description, newPuzzle.getDescription());
        values.put(height, newPuzzle.getHeight());
        values.put(width, newPuzzle.getWidth());

        key = (int)db.insert(daoFactory.getProperty("sql_table_puzzles"), null, values);

        return key;

    }

    /* return an existing puzzle entry from the database */

    public Puzzle find(int puzzleid) {

        /* use this method if there is NOT already a SQLiteDatabase open */

        SQLiteDatabase db = daoFactory.getWritableDatabase();
        Puzzle result = find(db, puzzleid);
        db.close();
        return result;

    }

    public Puzzle find(SQLiteDatabase db, int puzzleid) {

        /* use this method if there is NOT already a SQLiteDatabase open */

        Puzzle puzzle = null;

        String query = daoFactory.getProperty("sql_get_puzzle");
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(puzzleid)});

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();

            /* get data for puzzle */

            String idColumn = daoFactory.getProperty("sql_field_id");
            String nameColumn = daoFactory.getProperty("sql_field_name");
            String heightColumn = daoFactory.getProperty("sql_field_height");
            String widthColumn = daoFactory.getProperty("sql_field_width");

            String name = cursor.getString(cursor.getColumnIndexOrThrow(nameColumn));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow(heightColumn));
            int width = cursor.getInt(cursor.getColumnIndexOrThrow(widthColumn));

            HashMap<String, String> params = new HashMap<>();
            params.put(idColumn, String.valueOf(puzzleid));
            params.put(nameColumn, name);
            params.put(heightColumn, String.valueOf(height));
            params.put(widthColumn, String.valueOf(width));

            if ( !params.isEmpty() )
                puzzle = new Puzzle(params);

            /* get list of words (if any) to add to puzzle */

            WordDAO wordDao = daoFactory.getWordDAO();

            ArrayList<Word> words = wordDao.list(db, puzzleid);

            if ( !words.isEmpty() )
                puzzle.addWordsToPuzzle(words);

            cursor.close();

            /* get already-guessed words (if any) for puzzle */

            query = daoFactory.getProperty("sql_get_guesses");

            int boxColumnIndex = Integer.parseInt(daoFactory.getProperty("sql_guesses_box_column_index"));
            int directionColumnIndex = Integer.parseInt(daoFactory.getProperty("sql_guesses_direction_column_index"));

            cursor = db.rawQuery(query, new String[]{String.valueOf(puzzleid)});

            if (cursor.moveToFirst()) {

                cursor.moveToFirst();

                do {

                    Integer box = cursor.getInt(boxColumnIndex);
                    WordDirection direction = WordDirection.values()[cursor.getInt(directionColumnIndex)];

                    puzzle.addWordToGuessed(box + direction.toString());

                }
                while ( cursor.moveToNext() );

                cursor.close();

            }

        }

        return puzzle;

    }

    public PuzzleListItem[] list() {
        SQLiteDatabase db = daoFactory.getWritableDatabase();
        PuzzleListItem[] result = list(db);
        db.close();
        return result;
    }

    private PuzzleListItem[] list(SQLiteDatabase db) {
        ArrayList<PuzzleListItem> puzzle_list = new ArrayList<>();
        String query = daoFactory.getProperty("sql_get_puzzles");
        Cursor cursor = db.rawQuery(query, new String[]{});

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();


            do {
                String idColumn = daoFactory.getProperty("sql_field_id");
                String nameColumn = daoFactory.getProperty("sql_field_name");

                String name = cursor.getString(cursor.getColumnIndexOrThrow(nameColumn));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(idColumn));

                PuzzleListItem puzzle = new PuzzleListItem(id, name);

                puzzle_list.add(puzzle);

            }
            while ( cursor.moveToNext() );

            cursor.close();

        }

        return puzzle_list.toArray(new PuzzleListItem[]{});
    }

}