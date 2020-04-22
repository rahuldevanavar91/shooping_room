package com.android.shopping.ui.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.shopping.R;
import com.android.shopping.util.EveryHourWork;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String EVERY_HOUR_WORK = "every_hour_work";
    private Toolbar mToolbar;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mToolbar = findViewById(R.id.tool_bar);
      //  setToobar();
        setPeriodicWork();
    }

    private void setToobar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController controller = navHostFragment.getNavController();
        appBarConfiguration =
                new AppBarConfiguration.Builder(controller.getGraph()).build();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setPeriodicWork() {
        PeriodicWorkRequest everyHourRequest =
                new PeriodicWorkRequest.Builder(EveryHourWork.class,
                        1, TimeUnit.HOURS)
                        .addTag(EVERY_HOUR_WORK)
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                EVERY_HOUR_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                everyHourRequest
        );

    }
}
