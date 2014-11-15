package ini.cmu.com.fitfood;

/**
 * Created by krutikakamilla on 11/15/14.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtTime;
        TextView txtRating;
        TextView txtRepurl;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.simplerow, null);
            holder = new ViewHolder();
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt);
            holder.txtRating = (TextView) convertView.findViewById(R.id.txtrating);
           holder.txtRepurl = (TextView) convertView.findViewById(R.id.txt1);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtname);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtRating.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.txtTime.setText(rowItem.getTime());
        holder.txtRepurl.setText("");
       // holder.txtRepurl.setText(rowItem.getrecpurl());
        holder.imageView.setImageBitmap(rowItem.getImageId());

        return convertView;
    }
}
