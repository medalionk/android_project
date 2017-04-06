package ee.ut.demo.helpers;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by Bilal Abdullah on 4/6/2017.
 */

public class Parse {
    public static String imageUrl(String imageUrl){
        if(!imageUrl.startsWith("http:")){
            imageUrl = "http:" + imageUrl;
        }
        return imageUrl;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static String splitJoinString(String text, String sep, String comb){
        String[] texts = text.split(sep);
        if (texts.length > 1){
            return texts[0] + comb + texts[1].replaceAll("^\\s+|\\s+$", "");
        }

        return text;
    }
}
