package com.example.test.npa_flow.radio_buttons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemRadioButtonBinding;
import com.example.test.databinding.ItemStatusDetailsOfCustomerBinding;
import com.example.test.npa_flow.NotSpokeToCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.status_of_customer.adapter.StatusOfCustomerDetailsAdapter;
import com.example.test.npa_flow.status_of_customer.model.Activity;

import java.util.ArrayList;

public class RadioButtonsReasonAdapter extends RecyclerView.Adapter<RadioButtonsReasonAdapter.MyViewHolderClass> {

    ArrayList<RadioButtonReasons> radioButtonReasonsArrayList;

    private int selectedPosition = -1; // Initialize with an invalid position


    public RadioButtonsReasonAdapter(ArrayList<RadioButtonReasons> radioButtonReasonsArrayList) {
        this.radioButtonReasonsArrayList = radioButtonReasonsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRadioButtonBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_radio_button, parent, false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        RadioButtonReasons a = radioButtonReasonsArrayList.get(position);
        Context context = holder.itemView.getContext();

        if(null!=a.getDescription()){
            holder.binding.txtRadioButton.setText(a.getDescription());
        }

        // Set the state of the radio button based on the selected position
        holder.binding.radioButton.setChecked(selectedPosition == position);

        // Set an OnCheckedChangeListener for the radio button
        holder.binding.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && null!= a.getDescription()) {

                NotSpokeToCustomerActivity.isRadioButtonSelected = true; // make it true to Proceed

                selectedPosition = holder.getAdapterPosition();
                DetailsOfCustomerActivity.send_reason = a.getDescription();
                notifyDataSetChanged(); // Refresh the view to update the radio buttons
            }
        });

    }

    @Override
    public int getItemCount() {
        return radioButtonReasonsArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<RadioButtonReasons> data) {
        if (data.isEmpty()) {
            radioButtonReasonsArrayList = new ArrayList();
        }
        radioButtonReasonsArrayList = data;
        notifyDataSetChanged();

        return radioButtonReasonsArrayList;
    }

    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemRadioButtonBinding binding;

        public MyViewHolderClass(ItemRadioButtonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
