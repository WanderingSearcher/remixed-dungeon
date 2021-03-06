package com.nyrds.pixeldungeon.ml;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.nyrds.android.util.ModdingMode;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Preferences;
import com.watabou.pixeldungeon.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by mike on 09.03.2016.
 */
public class EventCollector {
    public static final String SAVE_ADS_EXPERIMENT = "SaveAdsExperiment2";

    static private FirebaseAnalytics mFirebaseAnalytics;
	static private boolean mDisabled = true;

	private static boolean analyticsUsable() {
		return Preferences.INSTANCE.getInt(Preferences.KEY_COLLECT_STATS,1) > 0;
	}

	static public void init() {
	    if(analyticsUsable()) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(RemixedDungeonApp.getContext());
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
            mDisabled = false;
        }
	}

	static public void logCountedEvent(String event, int threshold) {
		final String key = "CountedEvent_"+event;
		int count = Preferences.INSTANCE.getInt(key,0);
		count++;

		if(count==threshold) {
			logEvent(event);
		}
		Preferences.INSTANCE.put(key, count);
	}

	static public void logEvent(String event) {
		if (!mDisabled) {
			FirebaseCrashlytics.getInstance().log(event);

			Bundle params = new Bundle();
			mFirebaseAnalytics.logEvent(event, params);
		}
	}

	static public void logEvent(String event, double value) {
		if (!mDisabled) {
			FirebaseCrashlytics.getInstance().log(event);

			Bundle params = new Bundle();
			params.putDouble("dv", value);
			mFirebaseAnalytics.logEvent(event, params);
		}
	}

	static public void logEvent(String category, String event) {
		if (!mDisabled) {
			FirebaseCrashlytics.getInstance().log(category+":"+event);

			Bundle params = new Bundle();
			params.putString("event", event);
			mFirebaseAnalytics.logEvent(category, params);
		}
	}

	static public void levelUp(String character, long level) {
		if(!mDisabled && !ModdingMode.inMod()) {
			Bundle bundle = new Bundle();
			bundle.putString(FirebaseAnalytics.Param.CHARACTER, character);
			bundle.putLong(FirebaseAnalytics.Param.LEVEL, level);
			mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, bundle);
		}
	}

	static public void badgeUnlocked(String badgeId) {
		if(!mDisabled && !ModdingMode.inMod()) {
			Bundle bundle = new Bundle();
			bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, badgeId);
			mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle);
		}
	}

	static public void logEvent(String category, Map<String,String> eventData) {
		if (!mDisabled) {

			Bundle params = new Bundle();

			for (String key:eventData.keySet()) {
				params.putString(key, eventData.get(key));
			}

			mFirebaseAnalytics.logEvent(category, params);
		}
	}

	static public void logEvent(String category, String event, String label) {
		if (!mDisabled) {
			FirebaseCrashlytics.getInstance().log(category+":"+event+":"+label);

			Bundle params = new Bundle();
			params.putString("event", event);
			params.putString("label", label);
			mFirebaseAnalytics.logEvent(category, params);
		}
	}

	static public void logScene(final String scene) {
		if (!mDisabled) {
			Game.instance().runOnUiThread(() -> mFirebaseAnalytics.setCurrentScreen(Game.instance(), scene, null));
		}
	}

	static public void logException() {
		logException(new Exception(),1);
	}

	static public void logException(String desc) {
		logException(new Exception(desc),1);
	}

	static private void logException(Throwable e, int level) {
		if(!mDisabled) {
			StackTraceElement [] stackTraceElements = e.getStackTrace();
			e.setStackTrace(Arrays.copyOfRange(stackTraceElements,level,stackTraceElements.length));
			FirebaseCrashlytics.getInstance().recordException(e);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			ps.close();

			FirebaseCrashlytics.getInstance().log(Utils.EMPTY_STRING+System.currentTimeMillis()+":"+e.getMessage() + ":"+ baos.toString());
		}
	}

	static public void logException(Throwable e) {
		logException(e,0);
	}

	static public void logException(Throwable e, String desc) {
		if(!mDisabled) {
			FirebaseCrashlytics.getInstance().log(desc);
			logException(e, 0);
		}
	}

	public static void collectSessionData(String key, String value) {
		if(!mDisabled) {
			FirebaseCrashlytics.getInstance().setCustomKey(key, value);
		}
	}

    public static void disable() {
		mDisabled = true;
    }
}
