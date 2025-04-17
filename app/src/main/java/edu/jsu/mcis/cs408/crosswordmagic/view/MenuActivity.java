package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.ActivityMenuBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.CrosswordMagicModel;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;

public class MenuActivity extends AppCompatActivity implements AbstractView {

    private final String TAG = "MenuActivity";

    private ActivityMenuBinding binding;

    private RecyclerViewAdapter adapter;

    private CrosswordMagicController controller;

    private PuzzleListItemClickHandler itemClick = new PuzzleListItemClickHandler();

    private int selectedPuzzleId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        adapter = new RecyclerViewAdapter(this, new PuzzleListItem[]{});
        binding.output.setHasFixedSize(true);
        binding.output.setLayoutManager(new LinearLayoutManager(this));
        binding.output.setAdapter(adapter);

        controller = new CrosswordMagicController();

        CrosswordMagicModel model = new CrosswordMagicModel(this);

        controller.addModel(model);
        controller.addView(this);

        controller.getMenuList();

        binding.downloadBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("puzzleid", selectedPuzzleId);
            startActivity(i);
        });
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        Object value = evt.getNewValue();

        if (name.equals(CrosswordMagicController.MENU_LIST_PROPERTY)) {
            PuzzleListItem[] items = (PuzzleListItem[]) value;
            adapter.setMenuItems(items);
        }
    }

    public PuzzleListItemClickHandler getItemClick() {
        return itemClick;
    }

    private class PuzzleListItemClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = binding.output.getChildLayoutPosition(v);
            RecyclerViewAdapter adapter = (RecyclerViewAdapter) binding.output.getAdapter();
            if (adapter != null) {
                PuzzleListItem item = adapter.getMenuItem(position);
                selectedPuzzleId = item.getId();
                Log.i(TAG, "CLICKED: " + item.toString());
            }
        }
    }

}