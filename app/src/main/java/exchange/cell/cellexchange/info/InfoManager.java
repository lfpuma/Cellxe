package exchange.cell.cellexchange.info;

import android.content.res.AssetManager;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import exchange.cell.cellexchange.CellExchangeApplication;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Alexander on 06.10.17.
 */
@Singleton
public class InfoManager {

    private CellExchangeApplication cellExchangeApplication;
    private InfoApiService infoApiService;

    @Inject
    public InfoManager(CellExchangeApplication cellExchangeApplication, InfoApiService infoApiService) {
        this.cellExchangeApplication = cellExchangeApplication;
        this.infoApiService = infoApiService;

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            try {
                AssetManager assetManager = cellExchangeApplication.getAssets();
                if (realm.where(CountryModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(CountryModel.class,
                            assetManager.open("defaults/countries.json"));
                }
                if (realm.where(RegionModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(RegionModel.class,
                            assetManager.open("defaults/regions.json"));
                }
                if (realm.where(FreeZoneModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(FreeZoneModel.class,
                            assetManager.open("defaults/freezones.json"));
                }
                if (realm.where(CategoryModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(CategoryModel.class,
                            assetManager.open("defaults/categories.json"));
                }
                if (realm.where(MakeModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(MakeModel.class,
                            assetManager.open("defaults/makes.json"));
                }
                if (realm.where(PhoneModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(PhoneModel.class,
                            assetManager.open("defaults/models.json"));
                }
                if (realm.where(StockTypeModel.class).count() == 0){
                    realm.createOrUpdateAllFromJson(StockTypeModel.class,
                            assetManager.open("defaults/stock_types.json"));
                }
                if (realm.where(ConditionModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(ConditionModel.class,
                            assetManager.open("defaults/conditions.json"));
                }
                if (realm.where(SpecificationModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(SpecificationModel.class,
                            assetManager.open("defaults/specifications.json"));
                }
                if (realm.where(MakeToCategoryModel.class).count() == 0) {
                    realm.createOrUpdateAllFromJson(MakeToCategoryModel.class,
                            assetManager.open("defaults/make_to_categories.json"));
                }
                realm.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                realm.cancelTransaction();
            }
        }

    }

    public Flowable<List<CountryModel>> getCountries() {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(CountryModel.class).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<RegionModel>> getRegions(int countryId) {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(RegionModel.class).equalTo("countryId", countryId).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<FreeZoneModel>> getFreeZones() {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(FreeZoneModel.class).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<CategoryModel>> getCategories() {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(CategoryModel.class).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<MakeModel>> getMakes(int categoryId) {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            Flowable.fromIterable(realm.where(MakeToCategoryModel.class).equalTo("categoryId", categoryId).findAll())
                    .map(MakeToCategoryModel::getMakeId)
                    .toList()
                    .map(list -> list.toArray(new Integer[0]))
                    .subscribe(makeId -> {
                        RealmQuery<MakeModel> query = realm.where(MakeModel.class);
                        if (makeId != null && makeId.length > 0) {
                            query = query.in("id", makeId);
                        }
                        emitter.onNext(realm.copyFromRealm(query.findAllSorted("title")));
                    }, emitter::onError);
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<PhoneModel>> getModels(int makeId) {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(PhoneModel.class).equalTo("makeId", makeId).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<StockTypeModel>> getStockTypes() {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(StockTypeModel.class).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<ConditionModel>> getConditions() {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(ConditionModel.class).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<SpecificationModel>> getSpecifications() {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(SpecificationModel.class).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<List<MakeToCategoryModel>> getMakeToCategories() {
        return Flowable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                emitter.onNext(realm.copyFromRealm(realm.where(MakeToCategoryModel.class).findAll()));
            } catch (Exception e) {
                emitter.onError(e);
            }
            realm.close();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }



    public void syncInfoWithCache() {
        syncCountriesWithCache();
        syncRegionsWithCache();
        syncFreeZonesWithCache();
        syncCategoriesWithCache();
        syncMakesWithCache();
        syncPhoneModelsWithCache();
        syncStockTypesWithCache();
        syncConditionsWithCache();
        syncSpecificationsWithCache();
        syncMakeToCategoriesWithCache();
    }

    public void syncCountriesWithCache() {
        infoApiService.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(countryModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(CountryModel.class);
                        r.insertOrUpdate(countryModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void syncRegionsWithCache() {
        infoApiService.getRegions()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(regionModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(RegionModel.class);
                        r.insertOrUpdate(regionModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void syncFreeZonesWithCache() {
        infoApiService.getFreeZones()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(countryModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(FreeZoneModel.class);
                        r.insertOrUpdate(countryModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void syncCategoriesWithCache() {
        infoApiService.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(categoryModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(CategoryModel.class);
                        r.insertOrUpdate(categoryModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }
    public void syncMakesWithCache() {
        infoApiService.getMakes()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(makeModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(MakeModel.class);
                        r.insertOrUpdate(makeModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }
    public void syncPhoneModelsWithCache() {
        infoApiService.getModels()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(models -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(PhoneModel.class);
                        r.insertOrUpdate(models);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }
    public void syncStockTypesWithCache() {
        infoApiService.getStockTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(stockTypeModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(StockTypeModel.class);
                        r.insertOrUpdate(stockTypeModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void syncConditionsWithCache() {
        infoApiService.getConditions()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(conditionModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(ConditionModel.class);
                        r.insertOrUpdate(conditionModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void syncSpecificationsWithCache() {
        infoApiService.getSpecifications()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(specificationModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(SpecificationModel.class);
                        r.insertOrUpdate(specificationModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void syncMakeToCategoriesWithCache() {
        infoApiService.getMakeToCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(makeToCategoryModels -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        r.delete(MakeToCategoryModel.class);
                        r.insertOrUpdate(makeToCategoryModels);
                    });
                    realm.close();
                }, Throwable::printStackTrace);
    }





}
