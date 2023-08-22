package com.example.test.npa_flow.status_of_customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.CaseMap;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.databinding.ItemStatusDetailsOfCustomerBinding;
import com.example.test.helper_classes.Global;
import com.example.test.npa_flow.dpd.DPD_ResponseModel;
import com.example.test.npa_flow.status_of_customer.model.Activity;
import com.example.test.npa_flow.status_of_customer.model.ActivityDetail;
import com.example.test.roomDB.dao.MPinDao;
import com.example.test.roomDB.dao.UserNameDao;
import com.example.test.roomDB.database.LeadListDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatusOfCustomerDetailsAdapter extends RecyclerView.Adapter<StatusOfCustomerDetailsAdapter.MyViewHolderClass> {

    ArrayList<Activity> activityArrayList;

    public StatusOfCustomerDetailsAdapter(ArrayList<Activity> activityArrayList) {
        this.activityArrayList = activityArrayList;
    }

    @NonNull
    @Override
    public MyViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemStatusDetailsOfCustomerBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_status_details_of_customer, parent, false);
        return new MyViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderClass holder, int position) {

        Activity a = activityArrayList.get(position);


        Context context = holder.itemView.getContext();


        // format should be  -> 25th May 23,Friday,5:00pm
        if (a.getActivityDate() != null && a.getDay() != null && a.getActivityTime() != null) {
            holder.binding.txtStatusDetailsOfCustomer.setText(a.getActivityDate() + "," + a.getDay() + "," + a.getActivityTime()); //Activity Date and Activity Time Used here
        }



        if (a.getUserName() != null) {
            holder.binding.txtUserName.setText(a.getUserName());
        }



        holder.binding.ivDownArrowStatus.setOnClickListener(v -> {
            if (holder.binding.ivUpArrowStatus.getVisibility() == View.INVISIBLE) {
                holder.binding.ivDownArrowStatus.setVisibility(View.INVISIBLE);
                holder.binding.ivUpArrowStatus.setVisibility(View.VISIBLE);
                holder.binding.clStatusInfo.setVisibility(View.VISIBLE);
                // holder.binding.txtBottomMainStatusInfo.setVisibility(View.VISIBLE);


                //Schedule Date And Time in Status info
                if (a.getScheduleDate() != null && a.getScheduleTime() != null) {
                    holder.binding.txtScheduleDateStatusInfo.setVisibility(View.VISIBLE);
                    holder.binding.txtScheduleDateStatusInfo.setText("Scheduled Date and Time: " + a.getScheduleDate() + "," + a.getScheduleTime());
                }

                //AmountCollected
                if(null!=a.getAmountCollected() && !a.getAmountCollected().isEmpty()){
                    holder.binding.txtAmountCollected.setVisibility(View.VISIBLE);
                    holder.binding.txtAmountCollected.setText("Rs. "+a.getAmountCollected());
                }

                //BankName
               if(null!= a.getBankName() && !a.getBankName().isEmpty()){
                   holder.binding.txtBankName.setVisibility(View.VISIBLE);
                   holder.binding.txtBankName.setText("BankName: "+a.getBankName());
               }

               //ChequeNumber
                if(null!= a.getChequeNumber() && !a.getChequeNumber().isEmpty()){
                    holder.binding.txtChequeNumber.setVisibility(View.VISIBLE);
                    holder.binding.txtChequeNumber.setText("ChequeNumber: "+a.getChequeNumber());
                }

                //ChequeDate
                if(null!= a.getChequeDate() && !a.getChequeDate().isEmpty()){
                    holder.binding.txtChequeDate.setVisibility(View.VISIBLE);
                    holder.binding.txtChequeDate.setText("ChequeDate: "+a.getChequeDate());
                }

                //RelativeName
                if(null!=a.getRelativeName() && !a.getRelativeName().isEmpty() && !a.getRelativeName().equals("null")){
                  holder.binding.txtRelativeName.setVisibility(View.VISIBLE);
                  holder.binding.txtRelativeName.setText("Name: "+a.getRelativeName());
                }

                //RelativeContact
                if(null!=a.getRelativeContactNumber() && !a.getRelativeContactNumber().isEmpty() && !a.getRelativeContactNumber().equals("null")){
                    holder.binding.txtRelativeContact.setVisibility(View.VISIBLE);
                    holder.binding.txtRelativeContact.setText("Contact: "+a.getRelativeContactNumber());
                }

               //FO Name
                if(null!=a.getFoName() && !a.getFoName().isEmpty() && !a.getFoName().equals("null")){
                    holder.binding.txtFOName.setVisibility(View.VISIBLE);
                    holder.binding.txtFOName.setText("FO Name: "+a.getFoName());
                }

                //FO Date Of Visit Promised
                if(null!=a.getDateOfVisit() && !a.getDateOfVisit().isEmpty()){
                    holder.binding.txtDateOfVisitPromised.setVisibility(View.VISIBLE);
                    holder.binding.txtDateOfVisitPromised.setText("Date Of Visit: "+a.getDateOfVisit());
                }

                //Other (with reason)
                if(null!=a.getReason() && !a.getReason().isEmpty()){
                    holder.binding.txtReasonOthers.setVisibility(View.VISIBLE);
                    holder.binding.txtReasonOthers.setText("Reason: "+a.getReason());
                }


                // Status Info
                for (ActivityDetail details : a.getActivityDetails()) {

                    //Notes
                    if(null!=details.getAttemptNotes() && !details.getAttemptNotes().isEmpty()){
                        holder.binding.labelNotes.setVisibility(View.VISIBLE);
                        holder.binding.txtNotes.setVisibility(View.VISIBLE);
                        holder.binding.txtNotes.setText(details.getAttemptNotes());
                    }

                    checkAttemptFlowAndSetText(details,holder);


                   /* if (details.getAttemptFlow() != null) {
                        String attemptFlow = details.getAttemptFlow().toLowerCase();

                        //1)Spoke To The Customer / Did Not Spoke To The Customer / Did Not Visit The Customer / Visited The Customer
                        if (attemptFlow.contains("sttc")) {
                            holder.binding.txtHeadStatusInfo.setText(R.string.spoke_to_The_customer_status_info);
                        }
                        else if(attemptFlow.contains("dnvtc")){
                            holder.binding.txtHeadStatusInfo.setText(R.string.did_not_visited_the_customer);
                        }
                        else if(attemptFlow.contains("dnstc")){
                            holder.binding.txtHeadStatusInfo.setText(R.string.did_not_speak_to_ncustomer);
                        }
                        else if(attemptFlow.contains("vtc")){
                            holder.binding.txtHeadStatusInfo.setText(R.string.visited_the_customer);
                        }
                            // if No sttc OR dnstc OR dnvtc OR vtc then hide txtHeadStatusInfo
                        else{
                            holder.binding.txtHeadStatusInfo.setVisibility(View.GONE);
                        }



                        //2)Ready To Pay / Not Ready To Pay / Asked To Call Back Later / Visit Reschedule / Physical Visit Required
                        // Others / Invalid Number / Asked To Visit Later / NoResponseBusy / NotReachableSwitchedOff

                      if (attemptFlow.contains("nrtp")) {
                            holder.binding.txtMidStatusInfo1.setText(R.string.not_ready_to_pay_status_info);
                        }
                    else  if (attemptFlow.contains("rtp")) {
                            holder.binding.txtMidStatusInfo1.setText(R.string.ready_to_pay_status_info);
                        }
                    else if(attemptFlow.contains("nrb")){
                        holder.binding.txtMidStatusInfo1.setText(R.string.no_response_busy_);
                      }
                    else if (attemptFlow.contains("nrs")){
                          holder.binding.txtMidStatusInfo1.setText(R.string.not_reachable_switched_off_);
                      }

                    else  if (attemptFlow.contains("atcl")) {
                            holder.binding.txtMidStatusInfo1.setText(R.string.asked_to_call_back_later_status_info);
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText("Scheduled Date and Time: " + a.getScheduleDate() + "," + a.getScheduleTime());
                            holder.binding.txtScheduleDateStatusInfo.setVisibility(View.GONE);
                            //  holder.binding.txtScheduleTimeStatusInfo.setVisibility(View.GONE);
                        }
                    else if(attemptFlow.contains("atvl")){
                          holder.binding.txtMidStatusInfo1.setText(R.string.asked_to_visit_later);
                      }

                    else if(attemptFlow.contains("pvr")){
                          holder.binding.txtMidStatusInfo1.setText(R.string.physical_visit_required);
                      }

                    else  if(attemptFlow.contains("vr")){
                            holder.binding.txtMidStatusInfo1.setText(R.string.visit_rescheduled);
                        }
                     else  if(attemptFlow.contains("o") || attemptFlow.contains("oth")){
                            holder.binding.txtMidStatusInfo1.setText(R.string.others);
                        }
                     else if(attemptFlow.contains("inv")){
                          holder.binding.txtMidStatusInfo1.setText(R.string.number_is_invalid_);
                      }



                        //3)Send(Schedule) Visit For Collection / Send Link For Online Payment
                        // / FO Not Visited / Loan taken By Relative / Already Paid / Will Pay later
                        //Cash Amount Paid / Cheque Payment
                        // / Customer Not Available / Late For Visit / Others
                        //Not Taken Loan

                        if (attemptFlow.contains("svfc")) {
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.schedule_visit_for_collection_status_info);
                        } else if (attemptFlow.contains("slfop")) {
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.send_link_for_online_payment_status_info);
                        } else if (attemptFlow.contains("fnv")) {
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.fo_not_visited_status_info);
                        } else if (attemptFlow.contains("ltbr")) {
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.loan_taken_by_relative_status_info);
                        }
                        else if(attemptFlow.contains("cap")){
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.cash_payment);
                        }
                        else if(attemptFlow.contains("chp")){
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.cheque_payment);
                        }
                        else if (attemptFlow.contains("ap")) {
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.already_paid_status_info);
                        } else if (attemptFlow.contains("wpl")) {
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.will_pay_later_status_info);
                        }
                        else if(attemptFlow.contains("cna")){
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.customer_not_available);
                        }
                        else if(attemptFlow.contains("lfv")){
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.late_for_visit);
                        }
                        else if(attemptFlow.contains("o") || attemptFlow.contains("oth")){

                            //if flow=STTC-OTH then Hide txtMidStatusInfo2 cause it will also display Others
                            //to avoid displaying Others twice(2nd & 3rd row)
                            if(holder.binding.txtMidStatusInfo1.getText().equals("Others")){
                                holder.binding.txtMidStatusInfo2.setVisibility(View.GONE);
                            }

                            // display Others for 3rd row as in flow
                            else{
                                holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                                holder.binding.txtMidStatusInfo2.setText(R.string.others);
                            }

                        }
                        else if(attemptFlow.contains("ntl")){
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.not_taken_loan);
                        }




                        //4)Full Amt. Paid /Partial Amt. Paid/ Wil Pay Later / Will Pay Lump sump / Update / Update Schedule / Skip & Proceed
                        if (attemptFlow.contains("fap")) {
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.full_amount_paid_status_info);
                        } else if (attemptFlow.contains("pap")) {
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.partial_Amount_paid_status_info);
                        } else if (attemptFlow.contains("wpls")) {
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.will_pay_lump_sump_status_info);
                        } else if(attemptFlow.contains("update")) {
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.update);
                        } else if(attemptFlow.contains("us")){
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.update_schedule);
                        } else if (attemptFlow.contains("wpl")) {
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.will_pay_later_status_info);
                        }
                        else if(attemptFlow.contains("snp")){
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.skip_and_proceed);
                        }

                        //5) Schedule A Call / Schedule A Visit
                        if(attemptFlow.contains("sc") || attemptFlow.contains("sac")){
                            holder.binding.txtScheduleACallScheduleAVisit.setVisibility(View.VISIBLE);
                            holder.binding.txtScheduleACallScheduleAVisit.setText(R.string.schedule_a_call);
                        }
                        else if(attemptFlow.contains("sv") || attemptFlow.contains("sav")){
                            holder.binding.txtScheduleACallScheduleAVisit.setVisibility(View.VISIBLE);
                            holder.binding.txtScheduleACallScheduleAVisit.setText(R.string.schedule_a_visit);
                        }
                    }*/
                }
            }
        });

        holder.binding.ivUpArrowStatus.setOnClickListener(v -> {
            if (holder.binding.ivUpArrowStatus.getVisibility() == View.VISIBLE) {
                holder.binding.ivDownArrowStatus.setVisibility(View.VISIBLE);
                holder.binding.ivUpArrowStatus.setVisibility(View.INVISIBLE);
                holder.binding.clStatusInfo.setVisibility(View.GONE);
                // holder.binding.txtBottomMainStatusInfo.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return activityArrayList.size();
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
/*
      // to convert 24-05-2034 to 24 May 2023
    public static String convertDateFormat(String inputDate, String outputFormat) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = inputDateFormat.parse(inputDate);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat, Locale.getDefault());
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

*/

    public void checkAttemptFlowAndSetText(ActivityDetail details , MyViewHolderClass holder){

        System.out.println("checkAttemptFlowAndSetText() called");

        if (details.getAttemptFlow() != null) {
            String attemptFlow = details.getAttemptFlow();

            String[] flowSteps = attemptFlow.split("-");

            List<String> stepList = new ArrayList<>(Arrays.asList(flowSteps));  // Create a list from the array

            int position = 0; //initial value
            for (String step : stepList)
            {
                switch (step){

                    //1)Spoke To The Customer / Did Not Spoke To The Customer / Did Not Visit The Customer / Visited The Customer
                    case "STTC":
                        holder.binding.txtHeadStatusInfo.setText(R.string.spoke_to_The_customer_status_info);
                        break;
                    case "DNVTC":
                        holder.binding.txtHeadStatusInfo.setText(R.string.did_not_visited_the_customer);
                        break;
                    case "DNSTC" :
                        holder.binding.txtHeadStatusInfo.setText(R.string.did_not_speak_to_ncustomer);
                        break;
                    case "VTC":
                        holder.binding.txtHeadStatusInfo.setText(R.string.visited_the_customer);
                        break;

                    //2)Ready To Pay / Not Ready To Pay / Asked To Call Back Later / Visit Reschedule / Physical Visit Required
                    // Others / Invalid Number / Asked To Visit Later / NoResponseBusy / NotReachableSwitchedOff
                    // / Customer Not Available / Late For Visit / Others / Need To Close Visit

                    case "NRTP":
                        holder.binding.txtMidStatusInfo1.setText(R.string.not_ready_to_pay_status_info);
                        break;
                    case "RTP":
                        holder.binding.txtMidStatusInfo1.setText(R.string.ready_to_pay_status_info);
                        break;
                    case "NRB":
                        holder.binding.txtMidStatusInfo1.setText(R.string.no_response_busy_);
                        break;
                    case "NRS":
                        holder.binding.txtMidStatusInfo1.setText(R.string.not_reachable_switched_off_);
                        break;
                    case "ATCL":
                        holder.binding.txtMidStatusInfo1.setText(R.string.asked_to_call_back_later_status_info);
                        break;
                    case "ATVL":
                        holder.binding.txtMidStatusInfo1.setText(R.string.asked_to_visit_later);
                        break;
                    case "PVR":
                        holder.binding.txtMidStatusInfo1.setText(R.string.physical_visit_required);
                        break;
                    case "VR":
                        holder.binding.txtMidStatusInfo1.setText(R.string.visit_rescheduled);
                        break;
                    case "CNA":
                        holder.binding.txtMidStatusInfo1.setText(R.string.customer_not_available);
                        break;
                    case "LFV":
                        holder.binding.txtMidStatusInfo1.setText(R.string.late_for_visit);
                        break;
                    case "NTCV":
                        holder.binding.txtMidStatusInfo1.setText(R.string.need_to_close_visit_statusInfo);
                        break;
                    case "OTH":
                    case "O":
                        holder.binding.txtMidStatusInfo1.setText(R.string.others);


                        /*// OTH used in 2nd and 3rd flow

                        if (position == 1) //OTH in 2nd position (STTC-OTH)
                        {
                            holder.binding.txtMidStatusInfo1.setText(R.string.others);
                            // For the 2nd position, hide txtMidStatusInfo2 to avoid displaying Others twice(2nd & 3rd row)
                            holder.binding.txtMidStatusInfo2.setVisibility(View.GONE);
                        } else if (position == 2) //OTH in 3rd position (STTC-NRTP-OTH)
                        {
                            // For the 3rd position, display Others in txtMidStatusInfo2
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.others);
                        }*/

                        break;
                    case "INV" :
                        holder.binding.txtMidStatusInfo1.setText(R.string.number_is_invalid_);
                        break;

                    //3)Send(Schedule) Visit For Collection / Send Link For Online Payment
                    // / FO Not Visited / Loan taken By Relative / Already Paid / Will Pay later
                    //Cash Amount Paid / Cheque Payment
                    //Lack Of Funds/ PaymentAlreadyMade / Not Taken Loan / Will Pay LumpSum(managed in 4th flow)
                    // Submit

                    case "SVFC":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.schedule_visit_for_collection_status_info);
                        break;
                    case "SLFOP":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.send_link_for_online_payment_status_info);
                        break;
                    case "FNV":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.fo_not_visited_status_info);
                        break;
                    case "LTBR":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.loan_taken_by_relative_status_info);
                        break;
                    case "CAP":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.cash_payment);
                        break;
                    case "CHP":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.cheque_payment);
                        break;
                    case "AP":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.already_paid_status_info);
                        break;
                    case "WPL":
                        // WPL in 3rd and 4th flow
                       /* holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.will_pay_later_status_info);

                        //toggle Visibility
                        int txtMidStatusInfo3VisibilityWillPayLater = holder.binding.txtMidStatusInfo2.getText().equals("Will Pay Later") ? View.GONE : View.VISIBLE;
                        holder.binding.txtMidStatusInfo3.setVisibility(txtMidStatusInfo3VisibilityWillPayLater);
                        holder.binding.txtMidStatusInfo3.setText(R.string.will_pay_later_status_info);*/

                        if(position==2){ //WPL for 3rd Position
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.will_pay_later_status_info);
                        }
                       else if(position==3){ //WPL for 4th Position
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.will_pay_later_status_info);
                        }
                        break;
                        case "NTL":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.not_taken_loan);
                        break;
                    case "LOF":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.lack_of_funds);
                        break;
                    case "PAM":
                    case "CPM": // ClaimsPaymentMade = PaymentAlreadyMade
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.payment_already_made);
                        break;
                    case "SU":
                        holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.submit);
                     break;
                        //4)Full Amt. Paid /Partial Amt. Paid/ Wil Pay Later(managed in 3rdFlow) / Will Pay Lump sump / Update / Update Schedule / Skip & Proceed
                    case "FAP":
                        holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo3.setText(R.string.full_amount_paid_status_info);
                        break;
                    case "PAP":
                        holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo3.setText(R.string.partial_Amount_paid_status_info);
                        break;
                    case "WPLS":

                        //WPLS in 3rd & 4th Flow
                       /* holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo2.setText(R.string.will_pay_lump_sump_status_info);

                        //toggle visibility
                        int txtMidStatusInfo3VisibilityWillPayLumpSum = holder.binding.txtMidStatusInfo2.getText().equals("Will Pay Lump Sum") ? View.GONE : View.VISIBLE;
                        holder.binding.txtMidStatusInfo3.setVisibility(txtMidStatusInfo3VisibilityWillPayLumpSum);
                        holder.binding.txtMidStatusInfo3.setText(R.string.will_pay_lump_sump_status_info);*/

                        if(position==2){ //WPLS for 3rd Position
                            holder.binding.txtMidStatusInfo2.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo2.setText(R.string.will_pay_lump_sump_status_info);
                        }
                        else if(position==3){ //WPLS for 4th Position
                            holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                            holder.binding.txtMidStatusInfo3.setText(R.string.will_pay_lump_sump_status_info);
                        }
                        break;
                    case "UPDATE":
                         holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                         holder.binding.txtMidStatusInfo3.setText(R.string.update);
                         break;
                    case "US":
                        holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo3.setText(R.string.update_schedule);
                        break;
                    case "SNP":
                        holder.binding.txtMidStatusInfo3.setVisibility(View.VISIBLE);
                        holder.binding.txtMidStatusInfo3.setText(R.string.skip_and_proceed);
                        break;

                    //5) Schedule A Call / Schedule A Visit
                    case "SC":
                        holder.binding.txtScheduleACallScheduleAVisit.setVisibility(View.VISIBLE);
                        holder.binding.txtScheduleACallScheduleAVisit.setText(R.string.schedule_a_call);
                        break;
                    case "SV":
                        holder.binding.txtScheduleACallScheduleAVisit.setVisibility(View.VISIBLE);
                        holder.binding.txtScheduleACallScheduleAVisit.setText(R.string.schedule_a_visit);
                        break;

                }
                 position++; //for WPL & WPLS
            }


        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public ArrayList setData(ArrayList<Activity> data) {
        if (data.isEmpty()) {
            activityArrayList = new ArrayList();
        }
        activityArrayList = data;
        notifyDataSetChanged();

        return activityArrayList;
    }


    class MyViewHolderClass extends RecyclerView.ViewHolder {

        private ItemStatusDetailsOfCustomerBinding binding;

        public MyViewHolderClass(ItemStatusDetailsOfCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
