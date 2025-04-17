package edu.jsu.mcis.cs408.crosswordmagic.controller;

import android.util.Log;

public class CrosswordMagicController extends AbstractController {

    public static final String TEST_PROPERTY = "TestProperty";

    public static final String GRID_DIMENSION_PROPERTY = "GridDimension";
    public static final String GRID_LETTERS_PROPERTY = "GridLetters";
    public static final String GRID_NUMBERS_PROPERTY = "GridNumbers";
    public static final String GRID_GUESS_PROPERTY = "GridGuess";
    public static final String PUZZLE_CLUES_PROPERTY = "PuzzleClues";
    public static final String PUZZLE_LIST_PROPERTY = "PuzzleList";
    public static final String MENU_LIST_PROPERTY = "MenuList";
    public static final String PUZZLE_ID_PROPERTY = "PuzzleId";

    public void getTestProperty(String value) {
        getModelProperty(TEST_PROPERTY);
    }

    public void getGridDimensions() {
        getModelProperty(GRID_DIMENSION_PROPERTY);
    }

    public void getGridLetters() {
        getModelProperty(GRID_LETTERS_PROPERTY);
    }

    public void getGridNumbers() {
        getModelProperty(GRID_NUMBERS_PROPERTY);
    }

    public void getPuzzleClues() {
        getModelProperty(PUZZLE_CLUES_PROPERTY);
    }

    public void getPuzzleList() {
        getModelProperty(PUZZLE_LIST_PROPERTY);
    }

    public void makeGuess(int box, String guess) {
        String[] g = new String[] {String.valueOf(box), guess};
        setModelProperty(GRID_GUESS_PROPERTY, g);
    }

    public void getMenuList() {
        getModelProperty(MENU_LIST_PROPERTY);
    }

    public void setPuzzleId(Integer id) {
        setModelProperty(PUZZLE_ID_PROPERTY, id);
    }

}