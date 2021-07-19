package github.tornaco.practice.honeycomb.locker.ui.verify;

import android.os.Bundle;

import github.tornaco.android.thanos.BaseDefaultMenuItemHandlingAppCompatActivity;
import github.tornaco.practice.honeycomb.locker.R;

public class SecurityQuestionVerifyActivity extends BaseDefaultMenuItemHandlingAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_locker_security_question_verify_activity);
        showHomeAsUpNavigator();
    }
}
