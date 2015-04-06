package beldan.guidetofish.CaughtFish;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import beldan.guidetofish.R;

/**
 * Created by Dan on 21/03/15.
 */
public class ListView_Custom_Adapter extends BaseAdapter {
    Context context;
    List<ListView_RowItem> mListViewRowItems;

    public ListView_Custom_Adapter(Context context, List<ListView_RowItem> items) {
        this.context = context;
        this.mListViewRowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;
        TextView txtDesc;
        ImageView imageIcon;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tablecell_layout, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.tableCell_desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.tableCell_title);
            holder.imageIcon = (ImageView) convertView.findViewById(R.id.tableCell_icon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListView_RowItem listViewRowItem = (ListView_RowItem) getItem(position);

        holder.txtDesc.setText(listViewRowItem.getDate());
        holder.txtTitle.setText(listViewRowItem.getTitle());
        byte[] imageByte = listViewRowItem.getImage();
        if (imageByte != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            holder.imageIcon.setImageBitmap(bitmap);
        } else {
            holder.imageIcon.setImageResource(R.drawable.fish);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return mListViewRowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mListViewRowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListViewRowItems.indexOf(getItem(position));
    }

}
