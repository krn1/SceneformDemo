package raghu.co.ar.utils;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ViewUtils {
    public static android.graphics.Point getScreenCenter(AppCompatActivity activity) {
        View vw = activity.findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2);
    }
}
