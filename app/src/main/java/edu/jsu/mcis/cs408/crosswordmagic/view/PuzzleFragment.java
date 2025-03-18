package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.FragmentPuzzleBinding;

public class PuzzleFragment extends Fragment implements AbstractView {

    CrosswordMagicController controller;

    FragmentPuzzleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentPuzzleBinding.inflate(getLayoutInflater(), container, false);

       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.controller = ((MainActivity)getContext()).getController();
        controller.addView(this);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

        String name = evt.getPropertyName();
        String value = evt.getNewValue().toString();

        if (name.equals(CrosswordMagicController.GRID_GUESS_PROPERTY)) {
            Toast.makeText(this.getContext(), value, Toast.LENGTH_SHORT).show();
        }
    }
}
