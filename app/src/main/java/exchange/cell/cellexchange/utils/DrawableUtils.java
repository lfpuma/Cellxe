package exchange.cell.cellexchange.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * Created by Alexander on 13.10.17.
 */

public class DrawableUtils {



    public static Drawable getDrawableWithIntrinsicBounds(Context context, int res) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }
}
