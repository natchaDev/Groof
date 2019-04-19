package th.co.octagoninteractive.groof.preference;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.repository.remote.dao.BillUpdateDao;
import th.co.octagoninteractive.groof.repository.remote.dao.BillYearlyDao;
import th.co.octagoninteractive.groof.service.HttpManager;
import th.co.octagoninteractive.groof.ui.activity.LoginActivity;
import th.co.octagoninteractive.groof.ui.activity.MainActivity;
import th.co.octagoninteractive.groof.ui.fragment.PowerFragment;

/**
 * Created by mac on 8/24/18.
 */

public class GroofPreference {



    private static GroofPreference instance;

    private String Name;
    private String siteName;
    private String UserName;
    private String Entity_id;
    private String AuthKey;
    private String AppId= "170b8689056db18cd1e22a699ee2d804"; //for api weather don't edit
    private String Theme="day";
    private String day_db;
    private String month_db;
    private String year_db;
    private String day;
    private String month;
    private String year;
    private String cctvPath;
    private String demoUsername="demo";
    private String demoPassword="000000";
    private String createdStatus="failed";
    private String install_day;
    private String install_month;
    private String install_year;
    private String NotificationID;

    public static final synchronized GroofPreference instance() {
        if (instance == null) {
            instance = new GroofPreference();
        }
        return instance;
    }
        public void setNotificationID(String notificationID){
            NotificationID=notificationID;
        }

        public String getNotificationID(){
            return NotificationID;
        }

        public void setInstallDate(String install_date){
            if(install_date.length()>0) {

                String[] installDate = install_date.split("-");
                install_day = installDate[2];
                install_month = installDate[1];
                install_year = installDate[0];
            }
        }

        public String getInstall_day(){
            return install_day;
        }

        public String getInstall_month(){
            int installM = Integer.parseInt(install_month)-1;
            return installM+"";
    }

        public String getInstall_year(){
            return install_year;
        }

        public void setTheme(String theme){
            Theme=theme;
        }

        public void setName(String name) {
            Name = name;
        }

        public void setSiteName(String sitename) {
            siteName = sitename;
         }

        public void setUserName(String username) {
        UserName = username;
    }

        public void setAuthKey(String authKey) {
            AuthKey = "Bearer "+authKey;
        }

        public void setEntity_id(String entity_id) {
            Entity_id = entity_id;
        }

        public String getTheme(){
            return Theme;
        }

        public String getAppId(){
            return AppId;
        }

        public String getAuthKey(){
            return AuthKey;
        }

        public String getName(){
            return Name;
        }

        public String getSiteName(){
        return siteName;
    }

        public String getUserName() { return UserName; }

        public String getEntity_id(){
            return Entity_id;
        }

        public String getDay_db() {
        return day_db;
    }

        public void setDay_db(String day_db) {
        this.day_db = day_db;
    }

        public String getMonth_db() {
        return month_db;
    }

        public void setMonth_db(String month_db) {
        this.month_db = month_db;
    }

        public String getYear_db() {
        return year_db;
    }

        public void setYear_db(String year_db) {
        this.year_db = year_db;
    }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDemoUsername(){
            return demoUsername;
        }

        public String getDemoPassword(){
            return demoPassword;
        }

        public String getCreatedStatus(){
        return createdStatus;
    }

        public void setCCTVpath(String cctv_ddns, String cctv_username, String cctv_password) {
            cctvPath="rtsp://"+cctv_username+":"+cctv_password+"@"+cctv_ddns;
        }

        public String getCctvPath(){
            return cctvPath;
        }



    private Dialog alertDialog;

    public void showPopup(final Activity context, String title, String message, final String status) {

        alertDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.popup);

        TextView header = alertDialog.findViewById(R.id.tv_header);
        TextView detail = alertDialog.findViewById(R.id.tv_detail);
        Button btnOk = alertDialog.findViewById(R.id.btn_ok);
        ImageView ic_alert = alertDialog.findViewById(R.id.ic_alert);

        Button btn_submit = alertDialog.findViewById(R.id.btn_submit);
        Button btn_cancel = alertDialog.findViewById(R.id.btn_cancel);
        ImageView btn_close = alertDialog.findViewById(R.id.btn_close);
        LinearLayout ll_logout = alertDialog.findViewById(R.id.ll_logout);
        LinearLayout ll_submit = alertDialog.findViewById(R.id.ll_submit);

        if(status.equals("logout")){
            ll_submit.setVisibility(View.GONE);
            ll_logout.setVisibility(View.VISIBLE);
        }

        if(status.equals("fail")){
            ic_alert.setImageResource(R.drawable.ic_status_dialog_failed);
        }
        else if(status.equals("success") && status.equals("logout")){
            ic_alert.setImageResource(R.drawable.ic_status_dialog_success);
        }

        header.setText(title);
        detail.setText(message);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
               Intent intent = new Intent(context, LoginActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);
                context.startActivity(intent, options.toBundle());
                context.finish();
                SaveSharedPreference.clearUserAccount(context);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(status.equals("success")) {
                    alertDialog.dismiss();
                    context.finish();
                }
                else if(status.equals("fail")){
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }

    ProgressDialog dialog;
    public void showProcessDialog(Context context, String title){
        dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage("Loading Please Wait....");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    public void closeProcessDialog(){
        dialog.dismiss();
    }
}
