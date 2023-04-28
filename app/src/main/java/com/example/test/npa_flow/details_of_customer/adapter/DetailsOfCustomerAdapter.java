package com.example.test.npa_flow.details_of_customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemDetailsOfCustomerBinding;
import com.example.test.databinding.ItemDpdBinding;
import com.example.test.fragments_activity.BalanceInterestCalculationActivity;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.WebViewActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomerActivity;
import com.example.test.npa_flow.details_of_customer.DetailsOfCustomer_ResponseModel;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.dpd.adapter.DPD_Adapter;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsOfCustomerAdapter extends RecyclerView.Adapter<DetailsOfCustomerAdapter.MyViewHolderClass> {

    ArrayList<DetailsOfCustomer_ResponseModel> detailsOfCustomer_responseModelArrayList;

    public DetailsOfCustomerAdapter(ArrayList<DetailsOfCustomer_ResponseModel> detailsOfCustomer_responseModelArrayList) {
        this.detailsOfCustomer_responseModelArrayList = detailsOfCustomer_responseModelArrayList;
    }

    //For Calculating Balance Interest
    Double Total_due;
    Double Interest_rate;

    String TotalDue;
    String InterestRate;
    String BalanceInterestResult;

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDetailsOfCustomerBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_details_of_customer, parent, false);
        return new MyViewHolderClass(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        DetailsOfCustomer_ResponseModel a = detailsOfCustomer_responseModelArrayList.get(position);
        Context context = holder.itemView.getContext();

        holder.binding.labelDetailName.setText(a.getLable());
        holder.binding.txtDetailName.setText(a.getValue());

        //for Name
        if (a.getLable().contentEquals("Name") || a.getSequence() == 1) {
            holder.binding.edtDetail.setVisibility(View.GONE);
        }

        //for Amount Paid
        if (a.getLable().contentEquals("Amount Paid") || a.getSequence() == 16) {

            holder.binding.txtDetailName.setVisibility(View.VISIBLE);
            holder.binding.edtDetail.setOnFocusChangeListener((v, hasFocus) -> {

                holder.binding.btnDetail.setVisibility(View.VISIBLE);
                holder.binding.btnDetail.setText(R.string.save);

            });

            holder.binding.btnDetail.setOnClickListener(v -> {

                   String Amount_Paid = holder.binding.edtDetail.getText().toString();

                   Global.saveStringInSharedPref(context, "Amount_Paid", Amount_Paid); //save Amount Paid in SharedPreference


            });


            if (!Global.getStringFromSharedPref(context, "Amount_Paid").isEmpty()) {

                String Amount_Paid_From_SharedPreference = Global.getStringFromSharedPref(context, "Amount_Paid");
                holder.binding.edtDetail.setText(Amount_Paid_From_SharedPreference);

            }

        }

        //for Total Payable as on (Total Due + Balance Interest)
        if (a.getLable().contentEquals("Total Payable as on") || a.getSequence() == 15) {

            if (a.getValue() != null || !a.getValue().isEmpty()) {  //if Total Payable is Coming from API
                holder.binding.txtDetailName.setText(a.getValue());
                holder.binding.edtDetail.setVisibility(View.GONE);
            }

            if (a.getValue().isEmpty() || a.getValue().contentEquals("") || a.getValue() == null) { // If Total Payable not coming from API

                if (TotalDue != null && BalanceInterestResult != null && !BalanceInterestResult.isEmpty() && !TotalDue.isEmpty()) {
                    Double TotalPayableAsOn = Double.parseDouble(TotalDue) + Double.parseDouble(BalanceInterestResult);
                    holder.binding.txtDetailName.setText(String.valueOf(TotalPayableAsOn));
                }

                if (BalanceInterestResult == null || TotalDue == null) {
                    holder.binding.txtDetailName.setText("");//Empty
                }

            }

        }


        //for Button
        if (Objects.equals(a.getButton(), "Y")) {
            holder.binding.btnDetail.setVisibility(View.VISIBLE);
            holder.binding.btnDetail.setText(a.getButtonLable().toString());

        }

        //For Total Due and Interest Rate to Calculate in Balance Interest Calculation Activity
        if (a.getLable().contentEquals("Total Due") || a.getSequence() == 12) {
            Total_due = Double.parseDouble(a.getValue());
            TotalDue = Total_due.toString();
            holder.binding.btnDetail.setVisibility(View.GONE);
        }

        if (a.getLable().contentEquals("Interest Rate") || a.getSequence() == 13) {
            Interest_rate = Double.parseDouble(a.getValue());
            InterestRate = Interest_rate.toString();
            holder.binding.btnDetail.setVisibility(View.GONE);
        }


        //for Balance Interest Result
        if (a.getLable().contentEquals("Balance Interest as on") || a.getSequence() == 14) {

            if (a.getValue() != null || !a.getValue().isEmpty()) {  // if BalanceInterest is coming from API
                holder.binding.txtDetailName.setText(a.getValue());
            }


            if (a.getValue().contentEquals("") || a.getValue() == null) { //if BalanceInterest Not coming from API

                if (Global.getStringFromSharedPref(context, "BalanceInterestResult") != null) {

                    BalanceInterestResult = Global.getStringFromSharedPref(context, "BalanceInterestResult");
                    holder.binding.txtDetailName.setText(BalanceInterestResult);

                }

            }

        }

     /*
        //Button Clicks
        holder.binding.btnDetail.setOnClickListener(v->{

            if(a.getButtonLable().toString().contentEquals("Capture")){
                Intent i = new Intent(context,WebViewActivity.class);
                context.startActivity(i);
            }

            if(a.getButtonLable().toString().contentEquals("Calculate")){

                //Pass Total Due and Interest Rate to Calculate Balance Interest

                Intent i = new Intent(context,BalanceInterestCalculationActivity.class);
                i.putExtra("TotalDue",TotalDue);
                i.putExtra("InterestRate",InterestRate);
                context.startActivity(i);
            }

        });

*/

        // for EditText
        if (Objects.equals(a.getEditable(), "Y")) {
            holder.binding.edtDetail.setVisibility(View.VISIBLE);
            holder.binding.txtDetailName.setVisibility(View.INVISIBLE);
        }

        //for separation line between Personal and Account Details
        if (Objects.equals(a.getLable(), "Father's Name")) {
            holder.binding.viewLine.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return detailsOfCustomer_responseModelArrayList.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<DetailsOfCustomer_ResponseModel> data) {
        if (data.isEmpty()) {
            detailsOfCustomer_responseModelArrayList = new ArrayList();
        }
        detailsOfCustomer_responseModelArrayList = data;
        notifyDataSetChanged();

        return detailsOfCustomer_responseModelArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemDetailsOfCustomerBinding binding;

        public MyViewHolderClass(ItemDetailsOfCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
