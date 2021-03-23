package exchange.cell.cellexchange.register;

import java.util.List;

import exchange.cell.cellexchange.info.CountryModel;
import exchange.cell.cellexchange.info.FreeZoneModel;
import exchange.cell.cellexchange.info.RegionModel;

/**
 * Created by Alexander on 04.10.17.
 */

public interface RegisterView {

    void onGetCountries(List<CountryModel> countryModels);
    void onGetRegions(List<RegionModel> regionModels);
    void onGetFreeZones(List<FreeZoneModel> freeZoneModels);
    void onShowProgress();
    void onHideProgress();
    void onRegistrationSuccess();
    void onRegistrationFailed();
    void onFullNameNotFill();
    void onMobileNoFill();
    void onCountryNotFill();
    void onRegionNotFill();
    void onFreeZoneNotFill();

}
