package org.ushastoe.fluffy.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color; // Импортируем класс Color
import android.os.Build;
import android.os.PatternMatcher;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.ushastoe.fluffy.fluffyConfig;

import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.S)
public class MonetHelper {
    private static final HashMap<String, Integer> ids = new HashMap<>() {{
        put("a1_0", android.R.color.system_accent1_0);
        put("a1_10", android.R.color.system_accent1_10);
        put("a1_50", android.R.color.system_accent1_50);
        put("a1_100", android.R.color.system_accent1_100);
        put("a1_200", android.R.color.system_accent1_200);
        put("a1_300", android.R.color.system_accent1_300);
        put("a1_400", android.R.color.system_accent1_400);
        put("a1_500", android.R.color.system_accent1_500);
        put("a1_600", android.R.color.system_accent1_600);
        put("a1_700", android.R.color.system_accent1_700);
        put("a1_800", android.R.color.system_accent1_800);
        put("a1_900", android.R.color.system_accent1_900);
        put("a1_1000", android.R.color.system_accent1_1000);
        put("a2_0", android.R.color.system_accent2_0);
        put("a2_10", android.R.color.system_accent2_10);
        put("a2_50", android.R.color.system_accent2_50);
        put("a2_100", android.R.color.system_accent2_100);
        put("a2_200", android.R.color.system_accent2_200);
        put("a2_300", android.R.color.system_accent2_300);
        put("a2_400", android.R.color.system_accent2_400);
        put("a2_500", android.R.color.system_accent2_500);
        put("a2_600", android.R.color.system_accent2_600);
        put("a2_700", android.R.color.system_accent2_700);
        put("a2_800", android.R.color.system_accent2_800);
        put("a2_900", android.R.color.system_accent2_900);
        put("a2_1000", android.R.color.system_accent2_1000);
        put("a3_0", android.R.color.system_accent3_0);
        put("a3_10", android.R.color.system_accent3_10);
        put("a3_50", android.R.color.system_accent3_50);
        put("a3_100", android.R.color.system_accent3_100);
        put("a3_200", android.R.color.system_accent3_200);
        put("a3_300", android.R.color.system_accent3_300);
        put("a3_400", android.R.color.system_accent3_400);
        put("a3_500", android.R.color.system_accent3_500);
        put("a3_600", android.R.color.system_accent3_600);
        put("a3_700", android.R.color.system_accent3_700);
        put("a3_800", android.R.color.system_accent3_800);
        put("a3_900", android.R.color.system_accent3_900);
        put("a3_1000", android.R.color.system_accent3_1000);
        put("n1_0", android.R.color.system_neutral1_0);
        put("n1_10", android.R.color.system_neutral1_10);
        put("n1_50", android.R.color.system_neutral1_50);
        put("n1_100", android.R.color.system_neutral1_100);
        put("n1_200", android.R.color.system_neutral1_200);
        put("n1_300", android.R.color.system_neutral1_300);
        put("n1_400", android.R.color.system_neutral1_400);
        put("n1_500", android.R.color.system_neutral1_500);
        put("n1_600", android.R.color.system_neutral1_600);
        put("n1_700", android.R.color.system_neutral1_700);
        put("n1_800", android.R.color.system_neutral1_800);
        put("n1_900", android.R.color.system_neutral1_900);
        put("n1_1000", android.R.color.system_neutral1_1000);
        put("n2_0", android.R.color.system_neutral2_0);
        put("n2_10", android.R.color.system_neutral2_10);
        put("n2_50", android.R.color.system_neutral2_50);
        put("n2_100", android.R.color.system_neutral2_100);
        put("n2_200", android.R.color.system_neutral2_200);
        put("n2_300", android.R.color.system_neutral2_300);
        put("n2_400", android.R.color.system_neutral2_400);
        put("n2_500", android.R.color.system_neutral2_500);
        put("n2_600", android.R.color.system_neutral2_600);
        put("n2_700", android.R.color.system_neutral2_700);
        put("n2_800", android.R.color.system_neutral2_800);
        put("n2_900", android.R.color.system_neutral2_900);
        put("n2_1000", android.R.color.system_neutral2_1000);
        put("monetRedDark", R.color.monetRedDark);
        put("monetRedLight", R.color.monetRedLight);
        put("monetRedCall", R.color.monetRedCall);
        put("monetGreenCall", R.color.monetGreenCall);
        // Нет необходимости добавлять специальные записи для _50 здесь
    }};
    private static final String ACTION_OVERLAY_CHANGED = "android.intent.action.OVERLAY_CHANGED";
    private static final OverlayChangeReceiver overlayChangeReceiver = new OverlayChangeReceiver();

    public static int getColor(String color) {
        try {
            String effectiveColorKey = color;
            boolean isFiftyPercentTransparent = effectiveColorKey.endsWith("_t");
            String baseColorKey = effectiveColorKey;

            if (isFiftyPercentTransparent) {
                baseColorKey = effectiveColorKey.substring(0, effectiveColorKey.length() - 2);
            }

            Integer id = ids.get(baseColorKey);

            if (id != null) {
                int baseColor = ApplicationLoader.applicationContext.getColor(id);

                if (isFiftyPercentTransparent) {
                    int colorT = Color.argb(fluffyConfig.transparency, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor));
                    Log.d("Fluffy", "Color: " + colorT);
                    return colorT;
                } else {
                    return baseColor;
                }
            }

            FileLog.e("Color ID not found for key: " + effectiveColorKey);
            return 0;

        } catch (Exception e) {
            FileLog.e("Error loading color " + color, e);
            return 0;
        }
    }

    private static class OverlayChangeReceiver extends BroadcastReceiver {

        public void register(Context context) {
            IntentFilter packageFilter = new IntentFilter(ACTION_OVERLAY_CHANGED);
            packageFilter.addDataScheme("package");
            packageFilter.addDataSchemeSpecificPart("android", PatternMatcher.PATTERN_LITERAL);
            context.registerReceiver(this, packageFilter);
        }

        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_OVERLAY_CHANGED.equals(intent.getAction())) {
                if (Theme.getActiveTheme().isMonet()) {
                    Theme.applyTheme(Theme.getActiveTheme());
                }
            }
        }
    }

    public static void registerReceiver(Context context) {
        overlayChangeReceiver.register(context);
    }

    public static void unregisterReceiver(Context context) {
        try {
            overlayChangeReceiver.unregister(context);
        } catch (IllegalArgumentException e) {
            FileLog.e(e);
        }
    }
}