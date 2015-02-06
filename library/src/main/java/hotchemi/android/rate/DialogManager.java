package hotchemi.android.rate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

final class DialogManager {
    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

    private DialogManager() {
    }

    static Dialog create(final Context context, final boolean isShowNeutralButton,
                         final OnClickButtonListener listener, final View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rate_dialog_title);
        builder.setMessage(R.string.rate_dialog_message);
        if (view != null) builder.setView(view);
        builder.setPositiveButton(R.string.rate_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String packageName = context.getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW, UriHelper.getGooglePlay(packageName));
                if (UriHelper.isPackageExists(context, GOOGLE_PLAY_PACKAGE_NAME)) {
                    intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
                }
                context.startActivity(intent);
                PreferenceHelper.setAgreeShowDialog(context, false);
                if (listener != null) listener.onClickButton(which);
            }
        });
        if (isShowNeutralButton) {
            builder.setNeutralButton(R.string.rate_dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceHelper.setRemindInterval(context);
                    if (listener != null) listener.onClickButton(which);
                }
            });
        }
        builder.setNegativeButton(R.string.rate_dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceHelper.setAgreeShowDialog(context, false);
                if (listener != null) listener.onClickButton(which);
            }
        });
        return builder.create();
    }

    static Dialog create(final Context context,
                         final OnClickButtonListener listener, final int layoutId,
                         final int buttonOkId, final int buttonLaterId, final int buttonCancelId,
                         final String buttonOkText, final String buttonLaterText, final String buttonCancelText) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.rate_dialog_title);
        builder.setMessage(R.string.rate_dialog_message);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout layout = (LinearLayout) inflater.inflate(layoutId, null);
        builder.setView(layout);

        final AlertDialog alert = builder.create();

        if (buttonOkId > 0) {
            Button btn1 = (Button) layout.findViewById(buttonOkId);
            if (buttonOkText != null)
                btn1.setText(buttonOkText);
            else
                btn1.setText(R.string.rate_dialog_ok);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String packageName = context.getPackageName();
                    Intent intent = new Intent(Intent.ACTION_VIEW, UriHelper.getGooglePlay(packageName));
                    if (UriHelper.isPackageExists(context, GOOGLE_PLAY_PACKAGE_NAME)) {
                        intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
                    }
                    context.startActivity(intent);
                    PreferenceHelper.setAgreeShowDialog(context, false);
                    if (listener != null) listener.onClickButton(0);

                    alert.cancel();
                }
            });

        }
        if (buttonLaterId > 0) {
            Button btn2 = (Button) layout.findViewById(buttonLaterId);

            if (buttonLaterText != null)
                btn2.setText(buttonLaterText);
            else
                btn2.setText(R.string.rate_dialog_cancel);

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PreferenceHelper.setRemindInterval(context);
                    if (listener != null) listener.onClickButton(1);
                    alert.cancel();
                }
            });
        }
        if (buttonCancelId > 0) {
            Button btn3 = (Button) layout.findViewById(buttonCancelId);

            if (buttonCancelText != null)
                btn3.setText(buttonCancelText);
            else
                btn3.setText(R.string.rate_dialog_no);

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PreferenceHelper.setAgreeShowDialog(context, false);
                    if (listener != null) listener.onClickButton(2);
                    alert.cancel();
                }
            });
        }

        return alert;
    }

}