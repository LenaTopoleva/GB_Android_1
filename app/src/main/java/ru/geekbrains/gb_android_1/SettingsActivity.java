package ru.geekbrains.gb_android_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private Switch nightModeSwitch;
    private Switch pressureSwitch;
    private Switch feelsLikeSwitch;
    SettingsActivityPresenter settingsActivityPresenter = SettingsActivityPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (settingsActivityPresenter.getIsNightModeSwitchOn()) {
//            setTheme(R.style.AppThemeDark);
//        } else {
//            setTheme(R.style.AppTheme);
//        }
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.settings);
        initViews();
        setCurrentSwitchState();
        setOnNightModeSwitchClickListener();
        setOnFeelsLikeSwitchClickListener();
        setOnPressureSwitchClickListener();
        showBackBtn();
    }

    private void initViews() {
        nightModeSwitch = findViewById(R.id.night_mode_switch);
        pressureSwitch = findViewById(R.id.pressure_switch);
        feelsLikeSwitch = findViewById(R.id.feelsLikeSwitch);
    }

    private void setOnNightModeSwitchClickListener(){
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CurrentDataContainer.isNightModeOn = isChecked;
                settingsActivityPresenter.changeNightModeSwitchStatus();
                Log.d("Night", "SettingsActivity RECREATE");
                recreate();
            }
        });

    }
    //
//        new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(SettingsActivity.this, "nightmode is in dev", Toast.LENGTH_SHORT).show();
//                settingsActivityPresenter.changeNightModeSwitchStatus();
//                recreate();
//            }
//        });
//    }
    private void setTheme(){
        if(CurrentDataContainer.isNightModeOn){
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    private void setOnPressureSwitchClickListener(){
        pressureSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsActivityPresenter.changePressureSwitchStatus();
            }
        });
    }

    private void setOnFeelsLikeSwitchClickListener(){
        feelsLikeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsActivityPresenter.changeFeelsLikeSwitchStatus();
            }
        });
    }

    // Показывает стрелку назад на панели действий
    private void showBackBtn() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

    // При нажатии на стрелку назад, возвращает на MainActivity (предыдущую в стеке), закрывая текущую активити и передаем выбранные параметры:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("currCity", getCurrentDataContainer());
            startActivity(intent);
            finish();
        }
        return true;
    }

    private CurrentDataContainer getCurrentDataContainer(){
        CurrentDataContainer newCdc = new CurrentDataContainer();
        CurrentDataContainer cdc = (CurrentDataContainer) getIntent().getSerializableExtra("currCity");
        newCdc.switchSettingsArray = settingsActivityPresenter.createSettingsSwitchArray();
        if (cdc.weekWeatherData != null) newCdc.weekWeatherData = cdc.weekWeatherData;
        if (cdc.citiesList.size() > 0) newCdc.citiesList = cdc.citiesList;
        newCdc.currCityName = cdc.currCityName;
        return newCdc;
    }

    public void setCurrentSwitchState(){
       boolean[] switchArr =  settingsActivityPresenter.getSettingsArray();
       if(switchArr != null){
           nightModeSwitch.setChecked(switchArr[0]);
           feelsLikeSwitch.setChecked(switchArr[1]);
           pressureSwitch.setChecked(switchArr[2]);
       }
    }
}
