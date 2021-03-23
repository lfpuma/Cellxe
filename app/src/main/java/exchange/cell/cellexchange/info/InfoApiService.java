package exchange.cell.cellexchange.info;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alexander on 06.10.17.
 */

public interface InfoApiService {

    @GET("countries/")
    Flowable<List<CountryModel>> getCountries();

    @GET("regions/")
    Flowable<List<RegionModel>> getRegionsForCountry(@Query("country_id") int countryId);

    @GET("regions/")
    Flowable<List<RegionModel>> getRegions();

    @GET("freeZones/")
    Flowable<List<FreeZoneModel>> getFreeZones();

    @GET("categories/")
    Flowable<List<CategoryModel>> getCategories();

    @GET("makes/")
    Flowable<List<MakeModel>> getMakes();

    @GET("models/")
    Flowable<List<PhoneModel>> getModels();

    @GET("stockTypes/")
    Flowable<List<StockTypeModel>> getStockTypes();

    @GET("conditions/")
    Flowable<List<ConditionModel>> getConditions();

    @GET("specifications/")
    Flowable<List<SpecificationModel>> getSpecifications();

    @GET("makeToCategories/")
    Flowable<List<MakeToCategoryModel>> getMakeToCategories();

}
