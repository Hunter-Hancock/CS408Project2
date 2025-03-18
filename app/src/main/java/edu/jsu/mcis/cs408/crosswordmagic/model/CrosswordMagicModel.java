package edu.jsu.mcis.cs408.crosswordmagic.model;

import android.content.Context;
import android.util.Log;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.DAOFactory;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.PuzzleDAO;

public class CrosswordMagicModel extends AbstractModel {

    private final int DEFAULT_PUZZLE_ID = 1;

    private Puzzle puzzle;

    public CrosswordMagicModel(Context context) {

        DAOFactory daoFactory = new DAOFactory(context);
        PuzzleDAO puzzleDAO = daoFactory.getPuzzleDAO();

        this.puzzle = puzzleDAO.find(DEFAULT_PUZZLE_ID);
    }

    public void getGridDimension() {
        Integer[] dimension = new Integer[] {puzzle.getHeight(), puzzle.getWidth()};
        firePropertyChange(CrosswordMagicController.GRID_DIMENSION_PROPERTY, null, dimension);
    }

    public void getGridLetters() {
        firePropertyChange(CrosswordMagicController.GRID_LETTERS_PROPERTY, null, puzzle.getLetters());
    }

    public void getGridNumbers() {
        firePropertyChange(CrosswordMagicController.GRID_NUMBERS_PROPERTY, null, puzzle.getNumbers());
    }

    public void getPuzzleClues() {
        String[] clues = new String[] {puzzle.getCluesAcross(), puzzle.getCluesDown()};
        firePropertyChange(CrosswordMagicController.PUZZLE_CLUES_PROPERTY, null, clues);
    }

    public void setGridGuess(String[] g) {
        int box = Integer.parseInt(g[0]);
        String guess = g[1];

        WordDirection dir = puzzle.checkGuess(box, guess);
        if (dir == null) {
            firePropertyChange(CrosswordMagicController.GRID_GUESS_PROPERTY, null, "Incorrect");
            return;
        }

        puzzle.addWordToGuessed(box + dir.toString());
        firePropertyChange(CrosswordMagicController.GRID_GUESS_PROPERTY, null, "Correct");
    }

    public void getTestProperty() {

        String wordCount = (this.puzzle != null ? String.valueOf(puzzle.getSize()) : "none");
        firePropertyChange(CrosswordMagicController.TEST_PROPERTY, null, wordCount);

    }

}