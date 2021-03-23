package exchange.cell.cellexchange.post;

import java.util.List;

import exchange.cell.cellexchange.info.CategoryModel;
import exchange.cell.cellexchange.info.ConditionModel;
import exchange.cell.cellexchange.info.MakeModel;
import exchange.cell.cellexchange.info.PhoneModel;
import exchange.cell.cellexchange.info.SpecificationModel;
import exchange.cell.cellexchange.info.StockTypeModel;

/**
 * Created by Alexander on 09.10.17.
 */

public interface NewPostView {

    void onGetCategories(List<CategoryModel> categoryModels);

    void onGetMakes(List<MakeModel> makeModels);

    void onGetModels(List<PhoneModel> models);

    void onGetStockTypes(List<StockTypeModel> stockTypeModels);

    void onGetConditions(List<ConditionModel> conditionModels);

    void onGetSpecifications(List<SpecificationModel> specificationModels);

    void onShowProgress();

    void onHideProgress();

    void onCreateSuccess(PostModel postModel);

    void onCreateFailed();

    void onPostTypeNotFill();

    void onCategoryNotFill();

    void onMakeNotFill();

    void onModelNotFill();

    void onConditionNotFill();

    void onSpecificationNotFill();

    void onQtyNotFill();

    void onStockTypeNotFill();

}
