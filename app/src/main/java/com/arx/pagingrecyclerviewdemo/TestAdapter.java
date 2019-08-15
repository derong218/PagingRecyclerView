package com.arx.pagingrecyclerviewdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Objects;

/**
 * @author Zeng Derong (derong218@gmail.com)
 * on  2019-8-3 11:45
 */
public class TestAdapter extends ListAdapter<Test, TestAdapter.MyViewHolder> {
    private static final String TAG = TestAdapter.class.getSimpleName();

    public TestAdapter() {

        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<Test>() {
            @Override
            public boolean areItemsTheSame(@NonNull Test oldItem, @NonNull Test newItem) {
                return Objects.equals(oldItem.getName(), newItem.getName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Test oldItem, @NonNull Test newItem) {
                return Objects.equals(oldItem.getName(), newItem.getName());
            }
        }).build());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setText(getItem(position).getName());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final Button button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
    }


}
