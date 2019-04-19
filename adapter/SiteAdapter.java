package th.co.octagoninteractive.groof.adapter;

/**
 * Created by mac on 8/23/18.
 */
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.model.Site;
import th.co.octagoninteractive.groof.preference.GroofPreference;
import th.co.octagoninteractive.groof.ui.activity.MainActivity;
import th.co.octagoninteractive.groof.ui.activity.SiteListActivity;
import th.co.octagoninteractive.groof.ui.activity.SplashActiviy;
import th.co.octagoninteractive.groof.ui.fragment.DashboardFragment;

public class SiteAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Site> Sites;



    public SiteAdapter(Context context, ArrayList<Site> Sites) {
        this.mContext = context;
        this.Sites = Sites;
    }

    @Override
    public int getCount() {
        return Sites.size();
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
        final Site Site = Sites.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_site_row, null);
        }
        final ImageView icMark = (ImageView)convertView.findViewById(R.id.ic_mark_site);
        final TextView tvName = (TextView)convertView.findViewById(R.id.tv_siteName);
        final TextView tvSiteAddress = (TextView)convertView.findViewById(R.id.tv_siteAddress);
        final LinearLayout main_item_row = (LinearLayout)convertView.findViewById(R.id.main_item_row);

        main_item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GroofPreference.instance().getTheme().equals("night")){
                    tvName.setTextColor(Color.parseColor("#00adee"));
                    tvSiteAddress.setTextColor(Color.parseColor("#00adee"));
                    icMark.setImageResource(R.drawable.ic_site_mark_night);
                }else{
                    tvName.setTextColor(Color.parseColor("#292663"));
                    tvSiteAddress.setTextColor(Color.parseColor("#292663"));
                    icMark.setImageResource(R.drawable.ic_site_mark_active);
                }
                GroofPreference.instance().setSiteName(Site.getName());
                GroofPreference.instance().setEntity_id(Site.getEntity_id());
                nextToMain();
            }
        });

        tvSiteAddress.setText(Site.getSite_name());
        tvName.setText(Site.getName());

        return convertView;
    }


    private void nextToMain(){
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(mContext, R.anim.fade_in, R.anim.fade_out);
        mContext.startActivity(intent, options.toBundle());
        ((Activity)mContext).finish();
    }

}