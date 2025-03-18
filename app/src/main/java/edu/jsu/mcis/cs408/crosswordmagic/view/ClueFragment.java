package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.FragmentClueBinding;

public class ClueFragment extends Fragment implements AbstractView {

    CrosswordMagicController controller;

    FragmentClueBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClueBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = ((MainActivity)getContext()).getController();
        controller.addView(this);

        controller.getPuzzleClues();
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

        String name = evt.getPropertyName();
        Object value = evt.getNewValue();

        if (name.equals(CrosswordMagicController.PUZZLE_CLUES_PROPERTY)) {
            String[] clues = (String[])value;
            Log.i("CLUE FRAGMENT", "CLUES ACROSS : " + clues[0]);
            Log.i("CLUE FRAGMENT", "CLUES DOWN: " + clues[1]);
            binding.aContainer.setText(clues[0]);
            binding.dContainer.setText(clues[1]);
        }
    }
}
