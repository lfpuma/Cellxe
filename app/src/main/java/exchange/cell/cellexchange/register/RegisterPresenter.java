package exchange.cell.cellexchange.register;

import javax.inject.Inject;

import exchange.cell.cellexchange.info.CountryModel;
import exchange.cell.cellexchange.info.FreeZoneModel;
import exchange.cell.cellexchange.info.InfoManager;
import exchange.cell.cellexchange.info.RegionModel;
import exchange.cell.cellexchange.presenter.BasePresenter;
import exchange.cell.cellexchange.user.UserManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 04.10.17.
 */

public class RegisterPresenter extends BasePresenter<RegisterView> {

    private UserManager userManager;
    private InfoManager infoManager;

    @Inject
    public RegisterPresenter(RegisterView registerView, UserManager userManager, InfoManager infoManager) {
        super(registerView);
        this.userManager = userManager;
        this.infoManager = infoManager;
    }


    public void getCountries() {
        infoManager.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countryModels -> {
                    if (isAttach()) getView().onGetCountries(countryModels);
                }, Throwable::printStackTrace);
    }

    public void getRegions(int countryId) {
        infoManager.getRegions(countryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(regionModels -> {
                    if (isAttach()) getView().onGetRegions(regionModels);
                }, Throwable::printStackTrace);
    }

    public void getFreeZones() {
        infoManager.getFreeZones()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(freeZoneModels -> {
                    if (isAttach()) getView().onGetFreeZones(freeZoneModels);
                }, Throwable::printStackTrace);
    }

    public void register(String email, String password, String fullName, CountryModel countryModel,
                         RegionModel regionModel, String mobile, boolean isFreeZone, FreeZoneModel freeZoneModel) {
        if (fullName.isEmpty()) {
            if (isAttach()) getView().onFullNameNotFill();
            return;
        }
        if (countryModel == null) {
            if (isAttach()) getView().onCountryNotFill();
            return;
        }
        if (regionModel == null) {
            if (isAttach()) getView().onRegionNotFill();
            return;
        }
        if (mobile.isEmpty()) {
            if (isAttach()) getView().onMobileNoFill();
            return;
        }
        if (isFreeZone && freeZoneModel == null) {
            if (isAttach()) getView().onFreeZoneNotFill();
            return;
        }
        userManager.register(email, password, fullName, countryModel.getId(), regionModel.getId(),
                mobile, isFreeZone, freeZoneModel != null && isFreeZone ? String.valueOf(freeZoneModel.getId()) : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowProgress();
                })
                .doOnTerminate(() -> {
                    if (isAttach()) getView().onHideProgress();
                })
                .subscribe(userModel -> {
                    if (isAttach()) getView().onRegistrationSuccess();
                }, throwable -> {
                    if (isAttach()) getView().onRegistrationFailed();
                    throwable.printStackTrace();
                });
    }

}
