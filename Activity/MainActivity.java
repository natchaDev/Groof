package th.co.octagoninteractive.groof.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.octagoninteractive.groof.R;
import butterknife.ButterKnife;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.preference.SaveSharedPreference;
import th.co.octagoninteractive.groof.repository.remote.dao.BillUpdateDao;
import th.co.octagoninteractive.groof.repository.remote.dao.NotificationItemsDao;
import th.co.octagoninteractive.groof.repository.remote.dao.NotificationItemsData;
import th.co.octagoninteractive.groof.service.HttpManager;
import th.co.octagoninteractive.groof.ui.fragment.DashboardFragment;
import th.co.octagoninteractive.groof.ui.fragment.GraphFragment;
import th.co.octagoninteractive.groof.ui.fragment.NotificationDetailFragment;
import th.co.octagoninteractive.groof.ui.fragment.NotificationFragment;
import th.co.octagoninteractive.groof.ui.fragment.PowerFragment;
import th.co.octagoninteractive.groof.ui.fragment.SiteFragment;
import th.co.octagoninteractive.groof.util.CustomTypefaceSpan;
import th.co.octagoninteractive.groof.util.ExitApp;


public class MainActivity extends FragmentActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    FragmentTransaction ft;
    @BindView(R.id.menu_graph)
    RelativeLayout menu_graph;
    @BindView(R.id.menu_home)
    RelativeLayout menu_home;
    @BindView(R.id.menu_site)
    RelativeLayout menu_site;
    @BindView(R.id.menu_power)
    RelativeLayout menu_power;
    @BindView(R.id.menu_alert)
    RelativeLayout menu_alert;
    @BindView(R.id.menu_graph_night)
    RelativeLayout menu_graph_night;
    @BindView(R.id.menu_home_night)
    RelativeLayout menu_home_night;
    @BindView(R.id.menu_site_night)
    RelativeLayout menu_site_night;
    @BindView(R.id.menu_power_night)
    RelativeLayout menu_power_night;
    @BindView(R.id.menu_alert_night)
    RelativeLayout menu_alert_night;
    @BindView(R.id.btn_toggle_bar)
    LinearLayout btn_toggle_bar;
    @BindView(R.id.menu_night)
    LinearLayout menu_night;
    @BindView(R.id.menu)
    LinearLayout menu;
    @BindView(R.id.tv_title_toolbar)
    TextView tv_title_toolbar;
    @BindView(R.id.ic_groof_toolbar)
    ImageView ic_groof_toolbar;
    @BindView(R.id.main_content)
    LinearLayout main_content;
    @BindView(R.id.navigation)
    NavigationView navigationView;
    private TextView tv_num_alert, tv_num_alert_night;
    private LinearLayout ll_num_alert, ll_num_alert_night;
    private DrawerLayout drawerLayout;
    private int mSelectedId,  curPosition;
    private final Handler mDrawerHandler = new Handler();
    private Boolean notiDetail=false;
    private Handler handler;
    private String mNotification, mNotificationID;

    private static MainActivity instance;

    public static final boolean isInstanciated() {
        return instance != null;
    }

    public static final MainActivity instance() {
        if (instance != null)
            return instance;
        throw new RuntimeException("MainActivity not instantiated yet");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        handler=new Handler();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            mNotification = b.get("mNotification").toString();
            if(b.get("mNotificationID")!=null) {
                mNotificationID = b.get("mNotificationID").toString();
            }

        }

        instance = this;
        initInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initInstance(){

        genaratedTheme();
        setDefalutFragment();
        applyFontMenuNavigation();
        navigationView.setNavigationItemSelectedListener(this);
        menu_home.setOnClickListener(this);
        menu_graph.setOnClickListener(this);
        menu_site.setOnClickListener(this);
        menu_home_night.setOnClickListener(this);
        menu_graph_night.setOnClickListener(this);
        menu_site_night.setOnClickListener(this);
        menu_power.setOnClickListener(this);
        menu_power_night.setOnClickListener(this);
        menu_alert.setOnClickListener(this);
        menu_alert_night.setOnClickListener(this);
        menu_home.setSelected(true);
        menu_home_night.setSelected(true);
        btn_toggle_bar.setOnClickListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);

        ll_num_alert = menu_alert.findViewById(R.id.ll_num_alert);
        ll_num_alert_night =  menu_alert_night.findViewById(R.id.ll_num_alert);
        tv_num_alert = menu_alert.findViewById(R.id.tv_num_alert);
        tv_num_alert_night = menu_alert_night.findViewById(R.id.tv_num_alert);
        updateAlertItems();
        View hView =  navigationView.getHeaderView(0);
        TextView tv_username = (TextView)hView.findViewById(R.id.tv_username);
        tv_username.setText(GroofPreference.instance().getName());

        if(SaveSharedPreference.isUserLoggedIN()) {
            if (mNotification != null) {
                if (mNotification.equals("true")) {
                    clearSelectedMenu();
                    switchFragment(new NotificationDetailFragment(), 5);
                    menu_alert.setSelected(true);
                    menu_alert_night.setSelected(true);
                    tv_title_toolbar.setText(R.string.title_menu_alert);
                    tv_title_toolbar.setVisibility(View.VISIBLE);
                    ic_groof_toolbar.setVisibility(View.GONE);
                }
            }
        }

    }

    public void openNotiDetail(){
        notiDetail=true;
    }

    public void closeNotiDetail(){
        notiDetail=false;
    }

    private void genaratedTheme(){

        if(GroofPreference.instance().getTheme().equals("night")){
            main_content.setBackgroundResource(R.drawable.bg_main_groof_night);
            tv_title_toolbar.setTextColor(getResources().getColor(R.color.title_color_night));
            menu_night.setVisibility(View.VISIBLE);
            menu.setVisibility(View.GONE);
            ic_groof_toolbar.setImageResource(R.drawable.ic_groof_night);
        }
    }

    private void applyFontMenuNavigation(){

        NavigationView navView = findViewById(R.id.navigation);
        Menu m = navView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        updateAlertItems();
        if (id == R.id.menu_home || id == R.id.menu_home_night) {
            if(curPosition!=1){
                clearSelectedMenu();
                switchFragment(new DashboardFragment(), 1);
                menu_home.setSelected(true);
                menu_home_night.setSelected(true);
                tv_title_toolbar.setVisibility(View.GONE);
                ic_groof_toolbar.setVisibility(View.VISIBLE);
            }
        }
        else if(id == R.id.menu_graph || id == R.id.menu_graph_night) {
            if(curPosition!=2) {
                clearSelectedMenu();
                switchFragment(new GraphFragment(), 2);
                menu_graph.setSelected(true);
                menu_graph_night.setSelected(true);
                tv_title_toolbar.setText(R.string.title_menu_graph);
                tv_title_toolbar.setVisibility(View.VISIBLE);
                ic_groof_toolbar.setVisibility(View.GONE);
            }
        }
        else if(id==R.id.menu_power || id == R.id.menu_power_night) {
            if(curPosition!=3) {
                clearSelectedMenu();
                switchFragment(new PowerFragment(),3);
                menu_power.setSelected(true);
                menu_power_night.setSelected(true);
                tv_title_toolbar.setText(R.string.title_menu_power);
                tv_title_toolbar.setVisibility(View.VISIBLE);
                ic_groof_toolbar.setVisibility(View.GONE);
            }
        }
        else if(id==R.id.menu_site || id == R.id.menu_site_night) {
            if(curPosition!=4) {
                clearSelectedMenu();
                switchFragment(new SiteFragment(), 4);
                menu_site.setSelected(true);
                menu_site_night.setSelected(true);
                tv_title_toolbar.setText(R.string.title_menu_site);
                tv_title_toolbar.setVisibility(View.VISIBLE);
                ic_groof_toolbar.setVisibility(View.GONE);
            }
        }
        else if(id==R.id.menu_alert || id == R.id.menu_alert_night){
            if(curPosition!=5){
                clearSelectedMenu();
                switchFragment(new NotificationFragment(),5);
                menu_alert.setSelected(true);
                menu_alert_night.setSelected(true);
                tv_title_toolbar.setText(R.string.title_menu_alert);
                tv_title_toolbar.setVisibility(View.VISIBLE);
                ic_groof_toolbar.setVisibility(View.GONE);
            }
        }
        else if(id==R.id.btn_toggle_bar){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void clearSelectedMenu(){

        menu_home.setSelected(false);
        menu_graph.setSelected(false);
        menu_site.setSelected(false);
        menu_power.setSelected(false);
        menu_alert.setSelected(false);

        menu_home_night.setSelected(false);
        menu_graph_night.setSelected(false);
        menu_site_night.setSelected(false);
        menu_power_night.setSelected(false);
        menu_alert_night.setSelected(false);

        closeNotiDetail();
    }

    public void switchFragment(final Fragment fragment, int Position)   {

        updateAlertItems();

        int in , out;
        if(curPosition<Position){
            in = R.anim.enter_from_right;
            out = R.anim.exit_to_left;
        }else {
            in = R.anim.enter_from_left;
            out = R.anim.exit_to_right;
        }
        curPosition=Position;

        if(!fragment.isAdded()) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(in, out);
            ft.replace(R.id.contentContainerMain, fragment);
            ft.commit();
        }

        if(Position==3){
            clearSelectedMenu();
            menu_power.setSelected(true);
            menu_power_night.setSelected(true);
        }

    }

    private void reloadFragment(final Fragment fragment, int Position){
        updateAlertItems();
        int in , out;
        in = R.anim.fade_in;
        out = R.anim.fade_out;
        curPosition=Position;

        if(!fragment.isAdded()) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(in, out);
            ft.replace(R.id.contentContainerMain, fragment);
            ft.commit();
            GroofPreference.instance().closeProcessDialog();
        }
    }

    private void setDefalutFragment(){

        curPosition=1;
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.contentContainerMain, new DashboardFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        if(notiDetail){
            switchFragment(new NotificationFragment(), 5);
            clearSelectedMenu();
            menu_alert.setSelected(true);
            menu_alert_night.setSelected(true);
        }
        else if(curPosition!=1){
            switchFragment(new DashboardFragment(), 1);
            clearSelectedMenu();
            menu_home.setSelected(true);
            menu_home_night.setSelected(true);
            tv_title_toolbar.setVisibility(View.GONE);
            ic_groof_toolbar.setVisibility(View.VISIBLE);
        }
        else {
            ExitApp.now(this, R.string.double_for_close_app);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/db_heavent.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void navigate(final int itemId) {

        Fragment navFragment = null;
        switch (itemId) {
            case R.id.nav_1:
                goProfilePage();
                break;

            case R.id.nav_2:
                goDocumentsPage();
                break;

            case R.id.nav_3:
                goCameraSteamingPage();
                break;

            case R.id.nav_4:
                goContactPage();
                break;

            case R.id.nav_5:
                goSettingPage();
                break;

            case R.id.nav_6:
                GroofPreference.instance().showPopup(this,getString(R.string.popup_title_logout), getString(R.string.popup_message_logout),"logout");
                break;

        }
    }

    private void goProfilePage(){
        Intent intent = new Intent(this, ProfileActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
        this.finish();
    }

    private void goDocumentsPage(){
        Intent intent = new Intent(this, DocumentsActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
        this.finish();
    }

    private void goContactPage(){
        Intent intent = new Intent(this, ContactActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
        this.finish();
    }

    private void goSettingPage(){

        Intent intent = new Intent(this, SettingActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
        this.finish();
    }

    private void goCameraSteamingPage(){

        Intent intent = new Intent(this, CameraStreamingActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
        this.startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(mSelectedId);
            }
        }, 250);
        drawerLayout.closeDrawers();
        return false;
    }


    private Dialog alertDialog;

    public void showPopupPower(final Activity context, String title, String message) {

        alertDialog = new Dialog(context, R.style.CustomAlertDialogTheme);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.popup);

        TextView header = alertDialog.findViewById(R.id.tv_header);
        TextView detail = alertDialog.findViewById(R.id.tv_detail);
        Button btnOk = alertDialog.findViewById(R.id.btn_ok);
        ImageView ic_alert = alertDialog.findViewById(R.id.ic_alert);
        final EditText edt_expense= alertDialog.findViewById(R.id.edt_expense);

        ImageView btn_close = alertDialog.findViewById(R.id.btn_close);
        ic_alert.setVisibility(View.GONE);
        edt_expense.setVisibility(View.VISIBLE);
        header.setText(title);
        detail.setText(message);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                        if(TextUtils.isEmpty(edt_expense.getText())){
                            edt_expense.setError("กรอกค่าไฟ");
                        }
                        else {
                            createdExpense(context, edt_expense.getText().toString().trim());
                            alertDialog.dismiss();
                        }
            }
        });

        alertDialog.show();
    }

    private String bill_id;
    public void prepairedExpense(String bill_id){

        this.bill_id=bill_id;

    }

    private void createdExpense(final Activity context, String expense){

        String token = GroofPreference.instance().getAuthKey();
        Call<BillUpdateDao> call;
        call = HttpManager.getInstance().getService().updatedBill(token,bill_id,expense);
        call.enqueue(new Callback<BillUpdateDao>() {

            @Override
            public void onResponse(Call<BillUpdateDao> call, Response<BillUpdateDao> response) {
                if(response.isSuccessful()){

                    reloadFragment(new PowerFragment(),3);
                    Toast.makeText(context, "บันทึกค่าไฟเรียบร้อยแล้ว",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(context, "บันทึกค่าไม่สำเร็จ กรุณาลองใหม่อีกครั้ง",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BillUpdateDao> call, Throwable t) {
                Log.i("onFailure",t.toString());
                Toast.makeText(context, "ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้ กรุณาลองใหม่อีกครั้ง",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateAlertItems(){
        updateAlertData();
    }

    private void updateAlertData(){

        final String aut = GroofPreference.instance().getAuthKey();
        final String entity_id = GroofPreference.instance().getEntity_id();

        Call<NotificationItemsDao> call;
        call = HttpManager.getInstance().getService().getNotificationItems(aut, entity_id);
        call.enqueue(new Callback<NotificationItemsDao>() {

            @Override
            public void onResponse(Call<NotificationItemsDao> call, Response<NotificationItemsDao> response) {
                if(response.isSuccessful()){

                    NotificationItemsData notificationItemsData = response.body().getData();
                    if(notificationItemsData!=null){

                        if(notificationItemsData.getRead().equals("0")){
                            ll_num_alert.setVisibility(View.GONE);
                            ll_num_alert_night.setVisibility(View.GONE);
                        }
                        else{
                            ll_num_alert.setVisibility(View.VISIBLE);
                            tv_num_alert.setText(notificationItemsData.getRead());

                            ll_num_alert_night.setVisibility(View.VISIBLE);
                            tv_num_alert_night.setText(notificationItemsData.getRead());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationItemsDao> call, Throwable t) {
                Log.i("onFailure",t.toString());
            }
        });
    }

}