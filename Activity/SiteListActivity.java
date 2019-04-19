package th.co.octagoninteractive.groof.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.adapter.SiteAdapter;
import th.co.octagoninteractive.groof.model.Site;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.repository.remote.dao.SiteListDao;
import th.co.octagoninteractive.groof.repository.remote.dao.SiteListInfo;
import th.co.octagoninteractive.groof.service.HttpManager;

/**
 * Created by mac on 8/23/18.
 */

public class SiteListActivity extends Activity {
    Context mContext;
    @BindView(R.id.SiteList) ListView SiteListView;
    @BindView(R.id.main_content) LinearLayout main_content;
    @BindView(R.id.tv_wait)
    TextView tv_wait;
    @BindView(R.id.tv_title_toolbar) TextView tv_title_toolbar;

    ArrayList<Site> siteListTmp = new ArrayList<Site>();
    ArrayList<Site> siteList = new ArrayList<Site>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitelist);
        ButterKnife.bind(this);
        mContext=this;
        initial();
    }

    private void initial(){

        genaratedTheme();
        getSiteDemo();
        tv_wait.setVisibility(View.VISIBLE);
    }

    private void genaratedTheme(){

        if(GroofPreference.instance().getTheme().equals("night")){
            main_content.setBackgroundResource(R.drawable.bg_main_groof_night);
            tv_title_toolbar.setTextColor(getResources().getColor(R.color.title_color_night));
        }
    }


    private void getSiteDemo() {

        String token= GroofPreference.instance().getAuthKey();
        Call<SiteListDao> call;
        call = HttpManager.getInstance().getService().getSiteList(token);

        call.enqueue(new Callback<SiteListDao>() {
            @Override
            public void onResponse(Call<SiteListDao> call, Response<SiteListDao> response) {
                if(response.isSuccessful()) {

                    List<SiteListInfo> dataSiteList = response.body().getData().getProject();

                    if(dataSiteList!=null){

                        for(int i=0; i<dataSiteList.size(); i++) {
                            Site siteItem = new Site();
                            siteItem.setName(dataSiteList.get(i).getName());
                            siteItem.setEntity_id(dataSiteList.get(i).getEntityId());
                            siteItem.setSite_name(dataSiteList.get(i).getSite_name());
                            siteListTmp.add(i, siteItem);
                            siteList.add(i, siteItem);
                        }

                        SiteListView.setAdapter(new SiteAdapter(mContext, siteList));
                        tv_wait.setVisibility(View.GONE);
                    }
                }
                else{
                    Toast.makeText(SiteListActivity.this, "Can't get SiteList data", Toast.LENGTH_SHORT).show();
                    tv_wait.setText("Connection timeout Please check your connection");
                }
            }

            @Override
            public void onFailure(Call<SiteListDao> call, Throwable t) {
                Log.d("onFailure", t.toString());
                tv_wait.setText("Please check your internet connection...");
            }
        });
    }
}
