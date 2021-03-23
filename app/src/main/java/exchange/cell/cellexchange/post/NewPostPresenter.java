package exchange.cell.cellexchange.post;

import android.net.Uri;
import android.util.Log;

import javax.inject.Inject;

import exchange.cell.cellexchange.info.CategoryModel;
import exchange.cell.cellexchange.info.ConditionModel;
import exchange.cell.cellexchange.info.InfoManager;
import exchange.cell.cellexchange.info.MakeModel;
import exchange.cell.cellexchange.info.PhoneModel;
import exchange.cell.cellexchange.info.SpecificationModel;
import exchange.cell.cellexchange.info.StockTypeModel;
import exchange.cell.cellexchange.presenter.BasePresenter;
import exchange.cell.cellexchange.scope.ActivityScope;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 09.10.17.
 */

@ActivityScope
public class NewPostPresenter extends BasePresenter<NewPostView> {

    private InfoManager infoManager;
    private PostManager postManager;

    @Inject
    public NewPostPresenter(NewPostView newPostView, InfoManager infoManager, PostManager postManager) {
        super(newPostView);
        this.infoManager = infoManager;
        this.postManager = postManager;
    }

    public void getCategories() {
        infoManager.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categoryModels -> {
                    if (isAttach()) getView().onGetCategories(categoryModels);
                }, Throwable::printStackTrace);
    }

    public void getMakes(int categoryId) {
        infoManager.getMakes(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(makeModels -> {
                    if (isAttach()) getView().onGetMakes(makeModels);
                }, Throwable::printStackTrace);
    }

    public void getModels(int makeId) {
        infoManager.getModels(makeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    if (isAttach()) getView().onGetModels(models);
                }, Throwable::printStackTrace);
    }

    public void getStockTypes() {
        infoManager.getStockTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockTypeModels -> {
                    if (isAttach()) getView().onGetStockTypes(stockTypeModels);
                }, Throwable::printStackTrace);
    }

    public void getConditions() {
        infoManager.getConditions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(conditionModels -> {
                    if (isAttach()) getView().onGetConditions(conditionModels);
                }, Throwable::printStackTrace);
    }

    public void getSpecifications() {
        infoManager.getSpecifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(specificationModels -> {
                    if (isAttach()) getView().onGetSpecifications(specificationModels);
                }, Throwable::printStackTrace);
    }

    public void createPost(int interestedInId, CategoryModel categoryModel, MakeModel makeModel, PhoneModel phoneModel, String modelNumber,
                           StockTypeModel stockTypeModel, String color, String storageCapacity, ConditionModel conditionModel,
                           SpecificationModel specificationModel, String qty, String description, Uri imageUri) {
        if (interestedInId == 0) {
            if (isAttach()) getView().onPostTypeNotFill();
            return;
        }
        if (categoryModel == null) {
            if (isAttach()) getView().onCategoryNotFill();
            return;
        }
        if (makeModel == null) {
            if (isAttach()) getView().onMakeNotFill();
            return;
        }
        if (phoneModel == null) {
            if (isAttach()) getView().onModelNotFill();
            return;
        }
        if (stockTypeModel == null) {
            if (isAttach()) getView().onStockTypeNotFill();
            return;
        }
        if (conditionModel == null) {
            if (isAttach()) getView().onConditionNotFill();
            return;
        }
        if (specificationModel == null) {
            if (isAttach()) getView().onSpecificationNotFill();
            return;
        }
        if (qty.isEmpty()) {
            if (isAttach()) getView().onQtyNotFill();
            return;
        }
        postManager.create(interestedInId, categoryModel.getId(), makeModel.getId(), phoneModel.getId(), modelNumber, stockTypeModel.getId(),
                color, storageCapacity, conditionModel.getId(), specificationModel.getId(), Integer.parseInt(qty), description, imageUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (isAttach()) getView().onShowProgress();
                })
                .doOnTerminate(() -> {
                    if (isAttach()) getView().onHideProgress();
                })
                .subscribe(postModel -> {
                    if (isAttach()) getView().onCreateSuccess(postModel);
                }, throwable -> {
                    throwable.printStackTrace();
                    if (isAttach()) getView().onCreateFailed();
                });
    }
}
