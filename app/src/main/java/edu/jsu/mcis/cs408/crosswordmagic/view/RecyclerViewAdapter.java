package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.MenuItemBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private MenuItemBinding binding;

    private MenuActivity activity;
    private PuzzleListItem[] data;

    public RecyclerViewAdapter(MenuActivity activity, PuzzleListItem[] data) {
        super();
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = MenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.getRoot().setOnClickListener(activity.getItemClick());
        ViewHolder holder = new ViewHolder(binding.getRoot());
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setMenuItem(data[position]);
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public PuzzleListItem getMenuItem(int position) {
        return data[position];
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setMenuItems(PuzzleListItem[] items) {
        data = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private PuzzleListItem item;
        private TextView nameLabel;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public PuzzleListItem getMenuItem() {
            return item;
        }

        public void setMenuItem(PuzzleListItem item) {
            this.item = item;
        }

        public void bindData() {

            if (nameLabel == null) {
                nameLabel = itemView.findViewById(R.id.description);
            }
            StringBuilder sb = new StringBuilder("#");
            sb.append(item.getId()).append(": ").append(item.getName());
            nameLabel.setText(sb.toString());
        }

    }

}