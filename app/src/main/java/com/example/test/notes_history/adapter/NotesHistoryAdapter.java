package com.example.test.notes_history.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemNotesHistoryBinding;
import com.example.test.helper_classes.Global;
import com.example.test.notes_history.NotesHistoryResponseModel;

import java.util.ArrayList;

public class NotesHistoryAdapter extends RecyclerView.Adapter<NotesHistoryAdapter.MyViewHolderClass> {

     ArrayList<NotesHistoryResponseModel> notesHistoryResponseModelArrayList;

    public NotesHistoryAdapter(ArrayList<NotesHistoryResponseModel> notesHistoryResponseModelArrayList) {
        this.notesHistoryResponseModelArrayList = notesHistoryResponseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotesHistoryBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_notes_history, parent, false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        NotesHistoryResponseModel a = notesHistoryResponseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        if(a.getNotes()!=null){
            holder.binding.txtNotesHistory.setText(a.getNotes());

        }

        if(a.getDate()!=null && a.getTime()!=null){
            holder.binding.txtNotesHistoryDateTime.setText(a.getDate()+" "+a.getTime());
        }

        if(a.getUserName()!=null){
            holder.binding.txtNotesHistoryUserName.setText(a.getUserName());
        }

    }

    @Override
    public int getItemCount() {
        return notesHistoryResponseModelArrayList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        holder.setIsRecyclable(true);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolderClass holder) {
        super.onViewDetachedFromWindow(holder);
        holder.setIsRecyclable(false);
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<NotesHistoryResponseModel> data) {
        if (data.isEmpty()) {
            notesHistoryResponseModelArrayList = new ArrayList();
        }
        notesHistoryResponseModelArrayList = data;
        notifyDataSetChanged();

        return notesHistoryResponseModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemNotesHistoryBinding binding;

        public MyViewHolderClass(ItemNotesHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
