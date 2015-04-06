package beldan.guidetofish.CaughtFish;

import android.widget.ImageView;

/**
 * Created by Dan on 21/03/15.
 */
public class ListView_RowItem {
    private String title;
    private String date;
    private byte[] image;

    public ListView_RowItem(String title, String date, byte[] image) {
        this.title = title;
        this.date = date;
        this.image =image;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String desc) {
        this.date = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return title + "\n" + date;
    }
}
