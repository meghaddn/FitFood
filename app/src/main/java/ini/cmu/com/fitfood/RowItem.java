package ini.cmu.com.fitfood;

import android.graphics.Bitmap;

/**
 * Created by krutikakamilla on 11/15/14.
 */
public class RowItem {
    private Bitmap imageId;
    private String title;
    private String rating;
    private String time;
     String recpurl;

    public RowItem(Bitmap imageId, String title, String rating,String time,String recpurl) {
        this.imageId = imageId;
        this.title = title;
        this.rating = rating;
        this.time = time;
        this.recpurl = recpurl;
    }
    public Bitmap getImageId() {
        return imageId;
    }
    public void setImageId(Bitmap imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return rating;
    }
    public void setDesc(String desc) {
        this.rating = rating;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getrecpurl() {
        return recpurl;
    }
    public void setrecpurl(String recpurl) {
        this.recpurl = recpurl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + rating;
    }
}
