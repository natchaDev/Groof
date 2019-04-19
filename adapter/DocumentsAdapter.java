package th.co.octagoninteractive.groof.adapter;

/**
 * Created by mac on 8/23/18.
 */

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import th.co.octagoninteractive.groof.R;
import th.co.octagoninteractive.groof.model.Documents;


public class DocumentsAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Documents> Documents;


    public DocumentsAdapter(Context context, ArrayList<Documents> Documents) {
        this.mContext = context;
        this.Documents = Documents;
    }

    @Override
    public int getCount() {
        return Documents.size();
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
        final Documents doc = Documents.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_document_row, null);
        }

        final LinearLayout main_item_row = (LinearLayout) convertView.findViewById(R.id.main_item_row);
        final TextView tv_pdf_name = (TextView) convertView.findViewById(R.id.tv_pdf_name);

        tv_pdf_name.setText(doc.getName());

        main_item_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/viewerng/viewer?url="+doc.getUrl()));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try
                {
                    mContext.startActivity(i);
                }
                catch (ActivityNotFoundException ex)
                {
                    Toast.makeText(mContext,
                            "Can't open File PDF right now. Please try again later.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }


}