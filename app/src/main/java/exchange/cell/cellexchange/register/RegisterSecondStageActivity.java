package exchange.cell.cellexchange.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.info.CountryModel;
import exchange.cell.cellexchange.info.FreeZoneModel;
import exchange.cell.cellexchange.info.RegionModel;
import fr.ganfra.materialspinner.MaterialSpinner;
import ua.com.onyx.titlevaluespinner.TitleValueSpinner;

/**
 * Created by Alexander on 04.10.17.
 */

public class RegisterSecondStageActivity extends BaseActivity implements RegisterView {

    @Inject
    RegisterPresenter registerPresenter;

    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_full_name)
    EditText etFullName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.spinner_country)
    MaterialSpinner spinnerCountry;
    @BindView(R.id.spinner_region)
    MaterialSpinner spinnerRegion;
    @BindView(R.id.switch_freezone)
    SwitchCompat switchFreezone;
    @BindView(R.id.spinner_freezone)
    MaterialSpinner spinnerFreeZone;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private CountryModel selectedCountry;
    private RegionModel selectedRegion;
    private FreeZoneModel selectedFreeZone;

    private ArrayAdapter<CountryModel> countryModelArrayAdapter;
    private ArrayAdapter<RegionModel> regionModelArrayAdapter;
    private ArrayAdapter<FreeZoneModel> freeZoneModelArrayAdapter;

    private String email, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second_stage);
        ButterKnife.bind(this);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        setSupportActionBar(toolbar);
        setTitle(R.string.sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerPresenter.getCountries();
        registerPresenter.getFreeZones();

        countryModelArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        countryModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionModelArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        regionModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freeZoneModelArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        freeZoneModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerCountry.setAdapter(countryModelArrayAdapter);
        spinnerRegion.setAdapter(regionModelArrayAdapter);
        spinnerFreeZone.setAdapter(freeZoneModelArrayAdapter);

        RxAdapterView.itemSelections(spinnerCountry)
                .filter(event -> countryModelArrayAdapter.getCount() > 0)
                .subscribe(position -> {
                    UIUtil.hideKeyboard(RegisterSecondStageActivity.this);
                    if (position != -1) {
                        selectedCountry = countryModelArrayAdapter.getItem(position);
                        if (selectedCountry != null)
                            registerPresenter.getRegions(selectedCountry.getId());
                    } else {
                        selectedCountry = null;
                        spinnerCountry.hideFloatingLabel();
                    }
                    selectedRegion = null;
                }, Throwable::printStackTrace);

        RxAdapterView.itemSelections(spinnerRegion)
                .filter(event -> regionModelArrayAdapter.getCount() > 0)
                .subscribe(position -> {
                    UIUtil.hideKeyboard(RegisterSecondStageActivity.this);
                    if (position != -1) {
                        selectedRegion = regionModelArrayAdapter.getItem(position);
                    } else {
                        selectedRegion = null;
                        spinnerRegion.hideFloatingLabel();
                    }
                }, Throwable::printStackTrace);

        RxAdapterView.itemSelections(spinnerFreeZone)
                .filter(event -> freeZoneModelArrayAdapter.getCount() > 0)
                .subscribe(position -> {
                    UIUtil.hideKeyboard(RegisterSecondStageActivity.this);
                    if (position != -1) {
                        selectedFreeZone = freeZoneModelArrayAdapter.getItem(position);
                    } else {
                        selectedFreeZone = null;
                        spinnerFreeZone.hideFloatingLabel();
                    }
                }, Throwable::printStackTrace);

        RxCompoundButton.checkedChanges(switchFreezone)
                .subscribe(checked -> spinnerFreeZone.setVisibility(checked ? View.VISIBLE : View.GONE),
                        Throwable::printStackTrace);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        registerPresenter.detach();
        super.onDestroy();
    }

    @OnClick(R.id.btn_register)
    public void onRegisterClick() {
        registerPresenter.register(email, password, etFullName.getText().toString(), selectedCountry,
                selectedRegion, etPhone.getText().toString(), switchFreezone.isChecked(), selectedFreeZone);
    }

    @Override
    public void onGetCountries(List<CountryModel> countryModels) {
        countryModelArrayAdapter.clear();
        countryModelArrayAdapter.addAll(countryModels);
    }

    @Override
    public void onGetRegions(List<RegionModel> regionModels) {
        spinnerRegion.setVisibility(View.VISIBLE);
        regionModelArrayAdapter.clear();
        regionModelArrayAdapter.addAll(regionModels);
    }

    @Override
    public void onGetFreeZones(List<FreeZoneModel> freeZoneModels) {
        freeZoneModelArrayAdapter.clear();
        freeZoneModelArrayAdapter.addAll(freeZoneModels);
    }

    @Override
    public void onShowProgress() {
        showProgress();
    }

    @Override
    public void onHideProgress() {
        hideProgress();
    }

    @Override
    public void onRegistrationSuccess() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_up)
                .setMessage(R.string.message_registration_success)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    setResult(RESULT_OK);
                    finish();
                })
                .show();
    }

    @Override
    public void onRegistrationFailed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_up)
                .setMessage(R.string.message_registration_failed)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onFullNameNotFill() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_up)
                .setMessage(R.string.message_full_name_cannot_be_blank)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onMobileNoFill() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_up)
                .setMessage(R.string.message_mobile_cannot_be_blank)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onCountryNotFill() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_up)
                .setMessage(R.string.message_country_cannot_be_blank)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onRegionNotFill() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_up)
                .setMessage(R.string.message_region_cannot_be_blank)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onFreeZoneNotFill() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.sign_up)
                .setMessage(R.string.message_freezone_cannot_be_blank)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
