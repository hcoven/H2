package com.h2.fitness.h2fitness.alarm.ui;


import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.PopupMenu;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.alarm.AActivity;
import com.h2.fitness.h2fitness.alarm.Actions;
import com.h2.fitness.h2fitness.alarm.App;

import trikita.anvil.Anvil;
import trikita.anvil.DSL;
import trikita.jedux.Action;

import static trikita.anvil.BaseDSL.visibility;
import static trikita.anvil.DSL.CENTER;
import static trikita.anvil.DSL.CENTER_VERTICAL;
import static trikita.anvil.DSL.FILL;
import static trikita.anvil.DSL.LEFT;
import static trikita.anvil.DSL.WRAP;
import static trikita.anvil.DSL.allCaps;
import static trikita.anvil.DSL.backgroundColor;
import static trikita.anvil.DSL.checked;
import static trikita.anvil.DSL.dip;
import static trikita.anvil.DSL.frameLayout;
import static trikita.anvil.DSL.gravity;
import static trikita.anvil.DSL.isPortrait;
import static trikita.anvil.DSL.layoutGravity;
import static trikita.anvil.DSL.linearLayout;
import static trikita.anvil.DSL.margin;
import static trikita.anvil.DSL.max;
import static trikita.anvil.DSL.onCheckedChange;
import static trikita.anvil.DSL.onClick;
import static trikita.anvil.DSL.onSeekBarChange;
import static trikita.anvil.DSL.orientation;
import static trikita.anvil.DSL.padding;
import static trikita.anvil.DSL.progress;
import static trikita.anvil.DSL.size;
import static trikita.anvil.DSL.text;
import static trikita.anvil.DSL.textColor;
import static trikita.anvil.DSL.textSize;
import static trikita.anvil.DSL.textView;
import static trikita.anvil.DSL.typeface;
import static trikita.anvil.DSL.v;
import static trikita.anvil.DSL.weight;
import static trikita.anvil.DSL.x;
import static trikita.anvil.DSL.y;

public class AlarmLayout {
    public static void view() {
        backgroundColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).backgroundColor);
        linearLayout(() -> {
            orientation(LinearLayout.VERTICAL);
            header();
            frameLayout(() -> {
                size(FILL, 0);
                weight(1f);
                if (App.getState().alarm().on()) {
                    alarmOnLayout();
                } else {
                    alarmOffLayout();
                }
            });
            bottomBar();
        });
    }

    private static void header() {
        linearLayout(() -> {
            size(FILL, WRAP);
            gravity(CENTER_VERTICAL);
            com.h2.fitness.h2fitness.alarm.ui.Theme.materialIcon(() -> {
                textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).secondaryTextColor);
                textSize(dip(32));
                padding(dip(15));
                text("\ue855"); // "Alarm" icon
            });
            textView(() -> {
                size(WRAP, WRAP);
                weight(1f);
                typeface("fonts/Roboto-Light.ttf");
                textSize(dip(20));
                textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).primaryTextColor);
                DSL.text(R.string.app_name);
            });
        });
    }

    private static void alarmOffLayout() {
        textView(() -> {
            size(FILL, FILL);
            padding(dip(20));
            gravity(LEFT | CENTER_VERTICAL);
            typeface("fonts/Roboto-Light.ttf");
            allCaps(true);
            textSize(dip(32));
            textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).primaryTextColor);
            DSL.text(R.string.tv_start_alarm_text);
            onClick(v -> App.dispatch(new Action<>(Actions.Alarm.ON)));
        });
    }

    private static void alarmOnLayout() {
        frameLayout(() -> {
            size(FILL, FILL);
            // On tablets leave some margin around the clock view to avoid gigantic circles
            if ((Anvil.currentView().getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                margin(dip(48));
            } else {
                margin(dip(8));
            }
            int w = Anvil.currentView().getWidth();
            int h = Anvil.currentView().getHeight();
            if (h == 0 || w == 0) {
                Anvil.currentView().post(Anvil::render);
            }

            int hourCircleSize;
            int minuteCircleSize;
            int amPmWidth;

            if (isPortrait()) {
                hourCircleSize = (int) (w * 1.1f * 0.62f);
                minuteCircleSize = (int) (hourCircleSize * 0.62f);
                amPmWidth = (int) (hourCircleSize * 0.62f * 0.62f);
            } else {
                hourCircleSize = (int) (h);
                minuteCircleSize = (int) (hourCircleSize * 0.62f);
                amPmWidth = (int) (hourCircleSize * 0.62f * 0.62f);
            }

            frameLayout(() -> {
                size(hourCircleSize, hourCircleSize);
                if (isPortrait()) {
                    x(w / 2 - hourCircleSize * 0.21f - hourCircleSize / 2);
                    y(h / 2 + hourCircleSize * 0.19f - hourCircleSize / 2);
                } else {
                    x(w / 2 - hourCircleSize * 0.38f - hourCircleSize / 2);
                    y(h / 2 + hourCircleSize * 0.00f - hourCircleSize / 2);
                }
                gravity(CENTER);
                v(com.h2.fitness.h2fitness.alarm.ui.ClockView.class, () -> {
                    size(FILL, FILL);
                    progress(App.getState().alarm().hours());
                    max(12);
                    onSeekBarChange((v, progress, fromUser) -> {
                        if (fromUser) {
                            App.dispatch(new Action<>(Actions.Alarm.SET_HOUR, progress));
                        }
                    });
                    Anvil.currentView().invalidate();
                });
                textView(() -> {
                    size(WRAP, WRAP);
                    int hours = App.getState().alarm().hours();
                    if (DateFormat.is24HourFormat(Anvil.currentView().getContext())) {
                        text(String.format("%02d", hours + (App.getState().alarm().am() ? 0 : 12)));
                    } else {
                        if (hours == 0) {
                            text("12");
                        } else {
                            text(String.format("%02d", hours));
                        }
                    }
                    layoutGravity(CENTER);
                    typeface("fonts/Roboto-Light.ttf");
                    textSize(hourCircleSize * 0.3f);
                    textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).primaryColor);
                });
            });

            frameLayout(() -> {
                size(minuteCircleSize, minuteCircleSize);
                if (isPortrait()) {
                    x(w / 2 - hourCircleSize * 0.25f + minuteCircleSize / 2);
                    y(h / 2 + hourCircleSize * 0.05f - hourCircleSize / 2 - minuteCircleSize / 2);
                } else {
                    x(w / 2 - hourCircleSize * 0.25f + minuteCircleSize / 2);
                    y(h / 2 + hourCircleSize * 0.28f - hourCircleSize / 2 - minuteCircleSize / 2);
                }
                gravity(CENTER);
                v(com.h2.fitness.h2fitness.alarm.ui.ClockView.class, () -> {
                    size(FILL, FILL);
                    progress(App.getState().alarm().minutes());
                    max(60);
                    onSeekBarChange((v, progress, fromUser) -> {
                        if (fromUser) {
                            if (App.getState().settings().snap()) {
                                progress = (int) (Math.round(progress / 5.0) * 5) % 60;
                            }
                            App.dispatch(new Action<>(Actions.Alarm.SET_MINUTE, progress));
                        }
                    });
                    Anvil.currentView().invalidate();
                });
                textView(() -> {
                    size(WRAP, WRAP);
                    text(String.format("%02d", App.getState().alarm().minutes()));
                    layoutGravity(CENTER);
                    typeface("fonts/Roboto-Light.ttf");
                    textSize(minuteCircleSize * 0.3f);
                    textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).primaryColor);
                });
            });

            v(com.h2.fitness.h2fitness.alarm.ui.AmPmSwitch.class, () -> {
                size(amPmWidth, (int) (amPmWidth / 1.5f));
                if (isPortrait()) {
                    x(w / 2 - hourCircleSize * 0.21f - amPmWidth * 3 / 4);
                    y(h / 2 + hourCircleSize * 0.05f - hourCircleSize / 2 - amPmWidth / 1.5f / 2);
                } else {
                    x(w / 2 - hourCircleSize * 0.25f + minuteCircleSize - amPmWidth / 2);
                    y(h / 2 + hourCircleSize * 0.25f - amPmWidth / 1.5f / 2);
                }
                checked(App.getState().alarm().am());
                onCheckedChange((CompoundButton buttonView, boolean isChecked) -> {
                    App.dispatch(new Action<>(Actions.Alarm.SET_AM_PM, isChecked));
                });
            });
        });
    }

    private static void bottomBar() {
        linearLayout(() -> {
            size(FILL, dip(62));
            backgroundColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).backgroundTranslucentColor);

            com.h2.fitness.h2fitness.alarm.ui.Theme.materialIcon(() -> {
                text("\ue857"); // ALARM OFF
                textSize(dip(32));
                textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).secondaryTextColor);
                padding(dip(15));
                visibility(App.getState().alarm().on());
                onClick(v -> App.dispatch(new Action<>(Actions.Alarm.OFF)));
            });

            textView(() -> {
                size(0, FILL);
                weight(1f);
                margin(dip(10), 0);
                typeface("fonts/Roboto-Light.ttf");
                textSize(dip(16));
                textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).primaryTextColor);
                gravity(CENTER | CENTER_VERTICAL);
                text(formatAlarmTime(Anvil.currentView().getContext()));
            });

            com.h2.fitness.h2fitness.alarm.ui.Theme.materialIcon(() -> {
                text("\ue5d4"); // "more vert"
                textSize(dip(32));
                textColor(com.h2.fitness.h2fitness.alarm.ui.Theme.get(App.getState().settings().theme()).secondaryTextColor);
                padding(dip(15));
                onClick(AlarmLayout::showSettingsMenu);
            });
        });
    }

    private static String formatAlarmTime(Context c) {
        if (!App.getState().alarm().on()) {
            return "";
        }
        long t = App.getState().alarm().nextAlarm().getTimeInMillis() - System.currentTimeMillis() - 1;
        t = t / 60 / 1000;
        int m = (int) (t % 60);
        int h = (int) (t / 60);

        String minSeq = (m == 0) ? "" :
                (m == 1) ? c.getString(R.string.minute) :
                        c.getString(R.string.minutes, Long.toString(m));

        String hourSeq = (h == 0) ? "" :
                (h == 1) ? c.getString(R.string.hour) :
                        c.getString(R.string.hours, Long.toString(h));

        int index = ((h > 0) ? 1 : 0) | ((m > 0) ? 2 : 0);

        String[] formats = c.getResources().getStringArray(R.array.alarm_set);
        return String.format(formats[index], hourSeq, minSeq);
    }

    private static void showSettingsMenu(View v) {
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.getMenuInflater().inflate(R.menu.overflow_popup, menu.getMenu());
        menu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_settings) {
                ((AActivity) v.getContext()).openSettings();
//            } else if (item.getItemId() == R.id.menu_feedback) {
//                Context c = v.getContext();
//                Intent intent = new Intent(Intent.ACTION_SENDTO,
//                        Uri.fromParts("mailto", "adm.trikita@gmail.com", null));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Talalarmo");
//                v.getContext().startActivity(Intent.createChooser(intent, c.getString(R.string.leave_feedback)));
//            }
            }
            return true;
        });
        menu.show();
    }
}
