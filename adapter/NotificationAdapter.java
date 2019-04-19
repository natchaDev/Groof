package th.co.octagoninteractive.groof.adapter;

/**
 * Created by mac on 8/23/18.
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.model.Bill;
import th.co.octagoninteractive.groof.model.Notification;
import th.co.octagoninteractive.groof.ui.activity.MainActivity;

public class NotificationAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Notification> Notifications;


    public NotificationAdapter(Context context, ArrayList<Notification> Notifications) {
        this.mContext = context;
        this.Notifications = Notifications;
    }

    @Override
    public int getCount() {
        return Notifications.size();
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
        final Notification notification = Notifications.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_row_notification, null);
        }
        final TextView tvTitle = (TextView)convertView.findViewById(R.id.tv_noti_title);
        final TextView tv_Detail = (TextView)convertView.findViewById(R.id.tv_noti_detail);
        final ImageView ic_noti = (ImageView)convertView.findViewById(R.id.ic_noti);
        final LinearLayout main_item_row = (LinearLayout)convertView.findViewById(R.id.main_item_row);

        if(notification.isRead()){
            ic_noti.setImageResource(R.drawable.ic_noti_read);
        }
        tvTitle.setText(notification.getTitle());
        tv_Detail.setText(notification.getBody());





        return convertView;
    }


//    private void nextToMain(){
//        Intent intent = new Intent(mContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        ActivityOptions options =
//                ActivityOptions.makeCustomAnimation(mContext, R.anim.fade_in, R.anim.fade_out);
//        mContext.startActivity(intent, options.toBundle());
//        ((Activity)mContext).finish();
//    }

}