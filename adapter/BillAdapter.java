package th.co.octagoninteractive.groof.adapter;

/**
 * Created by mac on 8/23/18.
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.model.Bill;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.ui.activity.MainActivity;

public class BillAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Bill> Bills;
    private String mType;


    public BillAdapter(Context context, ArrayList<Bill> Bills, String type) {
        this.mContext = context;
        this.Bills = Bills;
        this.mType=type;
    }

    @Override
    public int getCount() {
        return Bills.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Bill Bill = Bills.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_power_row, null);
        }
        final TextView tvMonth = convertView.findViewById(R.id.tv_month);
        final TextView tvExpense = convertView.findViewById(R.id.tv_expense);
        final TextView tvSaving = convertView.findViewById(R.id.tv_saving);
        final TextView tvPower = convertView.findViewById(R.id.tv_power);
        final Button btnAddPower = convertView.findViewById(R.id.btn_add_power);

        if(mType.equals("total")){
            btnAddPower.setVisibility(View.GONE);
        }

        if(Bill.getExpense()>0){
            btnAddPower.setTextColor(Color.parseColor("#33fafbfc"));

            if(GroofPreference.instance().getTheme().equals("night")){
                btnAddPower.setBackgroundResource(R.drawable.bg_btn_submit_gray_night);
            }
            else {
                btnAddPower.setBackgroundResource(R.drawable.bg_btn_submit_gray);
            }
        }

        btnAddPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance().prepairedExpense(Bill.getBillID());
                MainActivity.instance().showPopupPower(((Activity)mContext),"ค่าไฟ","กรุณากรอกค่าไฟ");
            }
        });



        tvMonth.setText(Bill.getMonth());
        tvExpense.setText(String.format("%.0f",Bill.getExpense()));
        tvSaving.setText(String.format("%.0f",Bill.getSaving()));
        tvPower.setText(String.format("%.0f",Bill.getPower()));

        return convertView;
    }

}