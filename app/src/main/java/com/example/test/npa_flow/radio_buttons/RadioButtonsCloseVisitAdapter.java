package com.example.test.npa_flow.radio_buttons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemRadioButtonBinding;
import com.example.test.fragment_visits_flow.Visit_NPA_NotAvailableActivity;
import com.example.test.fragment_visits_flow.VisitsFlowCallDetailsActivity;
import com.example.test.npa_flow.SubmitCompletionActivityOfCustomer;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;

import java.util.ArrayList;

public class RadioButtonsCloseVisitAdapter extends RecyclerView.Adapter<RadioButtonsCloseVisitAdapter.MyViewHolderClass> {

    ArrayList<RadioButtonReasons> radioButtonReasonsArrayList;

    private int selectedPosition = -1; // Initialize with an invalid position
    public static int closeVisitReasonGenericId = -1; // initial value will be invalid

    public RadioButtonsCloseVisitAdapter(ArrayList<RadioButtonReasons> radioButtonReasonsArrayList) {
        this.radioButtonReasonsArrayList = radioButtonReasonsArrayList;
    }

    @NonNull
    @Override
    public RadioButtonsCloseVisitAdapter.MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRadioButtonBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_radio_button, parent, false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioButtonsCloseVisitAdapter.MyViewHolderClass holder, int position) {

        RadioButtonReasons a = radioButtonReasonsArrayList.get(position);
        Context context = holder.itemView.getContext();

        if(null!=a.getDescription()){
            holder.binding.txtRadioButton.setText(a.getDescription());
        }

        // Set the state of the radio button based on the selected position
        holder.binding.radioButton.setChecked(selectedPosition == position);

        // Set an OnCheckedChangeListener for the radio button
        holder.binding.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if( a.getDescription()!=null && a.getGenericId()!=null &&  isChecked){

              //  No Outstanding Dues -> genericId = 16
             //  Customer Relocated -> genericId = 18
            // Payment Already Made -> genericId - 19
            // Others -> genericId - 20

                closeVisitReasonGenericId = a.getGenericId();

                //For - No Outstanding Dues & Customer Relocated
                if (((a.getGenericId().equals(16) || a.getGenericId().equals(18)))) {
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    Visit_NPA_NotAvailableActivity.isRadioButtonSelected = true;
                    VisitsFlowCallDetailsActivity.send_reason = a.getDescription();
                    Visit_NPA_NotAvailableActivity visit_npa_notAvailableActivity = (Visit_NPA_NotAvailableActivity) context;
                    visit_npa_notAvailableActivity.showDialogCloseAccount();

                }

                //For - Others
                if (a.getGenericId().equals(20)){
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    Visit_NPA_NotAvailableActivity.isRadioButtonSelected = true;
                    Visit_NPA_NotAvailableActivity visit_npa_notAvailableActivity = (Visit_NPA_NotAvailableActivity) context;
                    visit_npa_notAvailableActivity.showOthersDialog();

                }

                //For - Payment Already Made
                if(a.getGenericId().equals(19)){
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    Visit_NPA_NotAvailableActivity.isRadioButtonSelected = true;
                    VisitsFlowCallDetailsActivity.send_reason = a.getDescription();
                    Visit_NPA_NotAvailableActivity visit_npa_notAvailableActivity = (Visit_NPA_NotAvailableActivity) context;
                    visit_npa_notAvailableActivity.showUploadReceiptDialog(context);

                }

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

