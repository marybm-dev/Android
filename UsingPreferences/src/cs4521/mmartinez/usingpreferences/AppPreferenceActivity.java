package cs4521.mmartinez.usingpreferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferenceActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//-- load the preferences from an XML file--
		addPreferencesFromResource(R.xml.mypreferences);
	}

}
