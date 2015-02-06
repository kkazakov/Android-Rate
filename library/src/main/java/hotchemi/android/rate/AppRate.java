package hotchemi.android.rate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.Date;

public class AppRate {

    private static AppRate singleton;

    private int installDate = 10;

    private int launchTimes = 10;

    private int remindInterval = 1;

    private int eventsTimes = -1;

    private boolean isShowNeutralButton = true;

    private boolean isDebug = false;

    private Context context;

    private View view;

    private int layoutId;

    private int customButtonOkId;
    private int customButtonLaterId;
    private int customButtonCancelId;


    private String customButtonOkText;
    private String customButtonLaterText;
    private String customButtonCancelText;

    private OnClickButtonListener listener;

    private AppRate(Context context) {
        this.context = context.getApplicationContext();
    }

    public static AppRate with(Context context) {
        if (singleton == null) {
            synchronized (AppRate.class) {
                if (singleton == null) {
                    singleton = new AppRate(context);
                }
            }
        }
        return singleton;
    }

    public AppRate setLaunchTimes(int launchTimes) {
        this.launchTimes = launchTimes;
        return this;
    }

    public AppRate setInstallDays(int installDate) {
        this.installDate = installDate;
        return this;
    }

    public AppRate setRemindInterval(int remindInterval) {
        this.remindInterval = remindInterval;
        return this;
    }

    public AppRate setShowNeutralButton(boolean isShowNeutralButton) {
        this.isShowNeutralButton = isShowNeutralButton;
        return this;
    }

    public AppRate setEventsTimes(int eventsTimes) {
        this.eventsTimes = eventsTimes;
        return this;
    }

    public AppRate clearAgreeShowDialog() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        return this;
    }

    public AppRate setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public AppRate setView(View view) {
        this.view = view;
        return this;
    }
    public AppRate setViewLayout(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public AppRate setOkButtonId(int btnId) {
        this.customButtonOkId = btnId;
        return this;
    }
    public AppRate setLaterButtonId(int btnId) {
        this.customButtonLaterId = btnId;
        return this;
    }
    public AppRate setNoButtonId(int btnId) {
        this.customButtonCancelId = btnId;
        return this;
    }

    public AppRate setOkButtonText(String btnText) {
        this.customButtonOkText = btnText;
        return this;
    }
    public AppRate setLaterButtonText(String btnText) {
        this.customButtonLaterText = btnText;
        return this;
    }
    public AppRate setNoButtonText(String btnText) {
        this.customButtonCancelText = btnText;
        return this;
    }

    public AppRate setOnClickButtonListener(OnClickButtonListener listener) {
        this.listener = listener;
        return this;
    }

    public void monitor() {
        if (PreferenceHelper.isFirstLaunch(context)) {
            PreferenceHelper.setInstallDate(context);
        }
        PreferenceHelper.setLaunchTimes(context, PreferenceHelper.getLaunchTimes(context) + 1);
    }

    public static boolean showRateDialogIfMeetsConditions(Activity activity) {
        boolean isMeetsConditions = singleton.isDebug || singleton.shouldShowRateDialog();
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    public static void openPlayStore(Activity activity) {
        String packageName = activity.getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW, UriHelper.getGooglePlay(packageName));
        if (UriHelper.isPackageExists(activity, DialogManager.GOOGLE_PLAY_PACKAGE_NAME)) {
            intent.setPackage(DialogManager.GOOGLE_PLAY_PACKAGE_NAME);
        }
        activity.startActivity(intent);
    }

    public static boolean passSignificantEvent(Activity activity) {
        boolean isMeetsConditions = singleton.isDebug || singleton.isOverEventPass();
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        } else {
            Context context = activity.getApplicationContext();
            int eventTimes = PreferenceHelper.getEventTimes(context);
            PreferenceHelper.setEventTimes(context, ++eventTimes);
        }
        return isMeetsConditions;
    }

    public static boolean passSignificantEventAndConditions(Activity activity) {
        boolean isMeetsConditions = singleton.isDebug || (singleton.isOverEventPass() && singleton.shouldShowRateDialog());
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        } else {
            Context context = activity.getApplicationContext();
            int eventTimes = PreferenceHelper.getEventTimes(context);
            PreferenceHelper.setEventTimes(context, ++eventTimes);
        }
        return isMeetsConditions;
    }

    public void showRateDialog(Activity activity) {
        if(!activity.isFinishing()) {
            if (layoutId == 0) {
                DialogManager.create(activity, isShowNeutralButton, listener, view).show();
            } else {
                DialogManager.create(activity, listener, layoutId, customButtonOkId, customButtonLaterId, customButtonCancelId,
                        customButtonOkText, customButtonLaterText, customButtonCancelText).show();
            }
        }
    }

    public boolean isOverEventPass() {
        return eventsTimes != -1 && PreferenceHelper.getEventTimes(context) > eventsTimes;
    }

    public boolean shouldShowRateDialog() {
        return PreferenceHelper.getIsAgreeShowDialog(context) &&
                isOverLaunchTimes() &&
                isOverInstallDate() &&
                isOverRemindDate();
    }

    private boolean isOverLaunchTimes() {
        return PreferenceHelper.getLaunchTimes(context) >= launchTimes;
    }

    private boolean isOverInstallDate() {
        return isOverDate(PreferenceHelper.getInstallDate(context), installDate);
    }

    private boolean isOverRemindDate() {
        return isOverDate(PreferenceHelper.getRemindInterval(context), remindInterval);
    }

    private boolean isOverDate(long targetDate, int threshold) {
        return new Date().getTime() - targetDate >= threshold * 24 * 60 * 60 * 1000;
    }

}