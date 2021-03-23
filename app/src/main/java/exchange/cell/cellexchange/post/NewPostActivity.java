package exchange.cell.cellexchange.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import exchange.cell.cellexchange.acitivty.BaseActivity;
import exchange.cell.cellexchange.R;
import exchange.cell.cellexchange.info.CategoryModel;
import exchange.cell.cellexchange.info.ConditionModel;
import exchange.cell.cellexchange.info.MakeModel;
import exchange.cell.cellexchange.info.PhoneModel;
import exchange.cell.cellexchange.info.SpecificationModel;
import exchange.cell.cellexchange.info.StockTypeModel;
import ua.com.onyx.titlevaluespinner.TitleValueSpinner;


/**
 * Created by Alexander on 01.10.17.
 */

public class NewPostActivity extends BaseActivity implements NewPostView {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_post_type)
    TitleValueSpinner spinnerPostType;
    @BindView(R.id.spinner_category)
    TitleValueSpinner spinnerCategory;
    @BindView(R.id.spinner_make)
    TitleValueSpinner spinnerMake;
    @BindView(R.id.spinner_model)
    TitleValueSpinner spinnerModel;
    @BindView(R.id.spinner_stock_type)
    TitleValueSpinner spinnerStockType;
    @BindView(R.id.spinner_condition)
    TitleValueSpinner spinnerCondition;
    @BindView(R.id.spinner_specification)
    TitleValueSpinner spinnerSpecification;
    @BindView(R.id.iv_attach_photo)
    ImageView ivAttachPhoto;
    @BindView(R.id.tv_add_photo)
    TextView tvAddPhoto;
    @BindView(R.id.et_model_number)
    EditText etModelNumber;
    @BindView(R.id.et_color)
    EditText etColor;
    @BindView(R.id.et_storage_capacity)
    EditText etStorageCapacity;
    @BindView(R.id.et_qty)
    EditText etQty;
    @BindView(R.id.et_description)
    EditText etDescription;

    @Inject
    NewPostPresenter newPostPresenter;

    private ArrayAdapter<String> postTypeArrayAdapter;
    private ArrayAdapter<CategoryModel> categoryModelArrayAdapter;
    private ArrayAdapter<MakeModel> makeModelArrayAdapter;
    private ArrayAdapter<PhoneModel> phoneModelArrayAdapter;
    private ArrayAdapter<StockTypeModel> stockTypeModelArrayAdapter;
    private ArrayAdapter<ConditionModel> conditionModelArrayAdapter;
    private ArrayAdapter<SpecificationModel> specificationModelArrayAdapter;

    private List<String> postTypes = new ArrayList<>();

    private int postTypeId = 0;
    private CategoryModel selectedCategory;
    private MakeModel selectedMake;
    private PhoneModel selectedPhoneModel;
    private StockTypeModel selectedStockType;
    private ConditionModel selectedCondition;
    private SpecificationModel selectedSpecification;

    private Uri attachedImage;

    private ImagePicker imagePicker = new ImagePicker();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(R.string.new_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postTypes = Arrays.asList(getResources().getStringArray(R.array.post_types));

        postTypeArrayAdapter = new ArrayAdapter<>(this, R.layout.title_value_spinner_adapter_layout, postTypes);
        categoryModelArrayAdapter = new ArrayAdapter<>(this, R.layout.title_value_spinner_adapter_layout);
        makeModelArrayAdapter = new ArrayAdapter<>(this, R.layout.title_value_spinner_adapter_layout);
        phoneModelArrayAdapter = new ArrayAdapter<>(this, R.layout.title_value_spinner_adapter_layout);
        stockTypeModelArrayAdapter = new ArrayAdapter<>(this, R.layout.title_value_spinner_adapter_layout);
        conditionModelArrayAdapter = new ArrayAdapter<>(this, R.layout.title_value_spinner_adapter_layout);
        specificationModelArrayAdapter = new ArrayAdapter<>(this, R.layout.title_value_spinner_adapter_layout);

        spinnerPostType.setAdapter(postTypeArrayAdapter);
        spinnerCategory.setAdapter(categoryModelArrayAdapter);
        spinnerMake.setAdapter(makeModelArrayAdapter);
        spinnerModel.setAdapter(phoneModelArrayAdapter);
        spinnerStockType.setAdapter(stockTypeModelArrayAdapter);
        spinnerCondition.setAdapter(conditionModelArrayAdapter);
        spinnerSpecification.setAdapter(specificationModelArrayAdapter);

        newPostPresenter.getCategories();
        newPostPresenter.getStockTypes();
        newPostPresenter.getConditions();
        newPostPresenter.getSpecifications();

        spinnerPostType.setOnSelectListener((titleValueSpinner, which) -> {
            postTypeId = which + 1;
            titleValueSpinner.setValue(postTypes.get(which));
        });

        spinnerCategory.setOnSelectListener((titleValueSpinner, which) -> {
            selectedCategory = categoryModelArrayAdapter.getItem(which);
            titleValueSpinner.setValue(selectedCategory.getTitle());
            newPostPresenter.getMakes(selectedCategory.getId());
        });

        spinnerMake.setOnSelectListener((titleValueSpinner, which) -> {
            selectedMake = makeModelArrayAdapter.getItem(which);
            titleValueSpinner.setValue(selectedMake.getTitle());
            newPostPresenter.getModels(selectedMake.getId());
        });

        spinnerModel.setOnSelectListener((titleValueSpinner, which) -> {
            selectedPhoneModel = phoneModelArrayAdapter.getItem(which);
            titleValueSpinner.setValue(selectedPhoneModel.getTitle());
        });

        spinnerStockType.setOnSelectListener((titleValueSpinner, which) -> {
            selectedStockType = stockTypeModelArrayAdapter.getItem(which);
            titleValueSpinner.setValue(selectedStockType.getTitle());
        });

        spinnerCondition.setOnSelectListener((titleValueSpinner, which) -> {
            selectedCondition = conditionModelArrayAdapter.getItem(which);
            titleValueSpinner.setValue(selectedCondition.getTitle());
        });

        spinnerSpecification.setOnSelectListener((titleValueSpinner, which) -> {
            selectedSpecification = specificationModelArrayAdapter.getItem(which);
            titleValueSpinner.setValue(selectedSpecification.getTitle());
        });


        RxTextView.afterTextChangeEvents(etDescription)
                .subscribe(event -> {
                    String text = event.editable().toString();
                    String[] words = text.split(" ");
                    if (words.length >= 160) {
                        event.editable().delete(text.length() - 1, text.length());
                    }
                }, Throwable::printStackTrace);


    }

    @OnClick(R.id.tv_add_photo)
    public void onAddPhotoClick() {
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {
            }

            @Override
            public void onCropImage(Uri imageUri) {
                super.onCropImage(imageUri);
                attachedImage = imageUri;
                tvAddPhoto.setVisibility(View.GONE);
                ivAttachPhoto.setVisibility(View.VISIBLE);
                ivAttachPhoto.setImageURI(imageUri);
            }

            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder.setAspectRatio(5, 3)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setRequestedSize(500, 500);
            }
        });
    }

    @OnClick(R.id.btn_create)
    public void onCreateClick() {
        newPostPresenter.createPost(postTypeId, selectedCategory, selectedMake, selectedPhoneModel,
                etModelNumber.getText().toString(), selectedStockType, etColor.getText().toString(),
                etStorageCapacity.getText().toString(), selectedCondition, selectedSpecification,
                etQty.getText().toString(), etDescription.getText().toString(), attachedImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetCategories(List<CategoryModel> categoryModels) {
        categoryModelArrayAdapter.clear();
        categoryModelArrayAdapter.addAll(categoryModels);
    }

    @Override
    public void onGetMakes(List<MakeModel> makeModels) {
        spinnerMake.setValue(null);
        makeModelArrayAdapter.clear();
        makeModelArrayAdapter.addAll(makeModels);
        spinnerModel.setValue(null);
        phoneModelArrayAdapter.clear();
    }

    @Override
    public void onGetModels(List<PhoneModel> models) {
        spinnerModel.setValue(null);
        phoneModelArrayAdapter.clear();
        phoneModelArrayAdapter.addAll(models);
    }

    @Override
    public void onGetStockTypes(List<StockTypeModel> stockTypeModels) {
        stockTypeModelArrayAdapter.clear();
        stockTypeModelArrayAdapter.addAll(stockTypeModels);
    }

    @Override
    public void onGetConditions(List<ConditionModel> conditionModels) {
        conditionModelArrayAdapter.clear();
        conditionModelArrayAdapter.addAll(conditionModels);
    }

    @Override
    public void onGetSpecifications(List<SpecificationModel> specificationModels) {
        specificationModelArrayAdapter.clear();
        specificationModelArrayAdapter.addAll(specificationModels);
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
    public void onCreateSuccess(PostModel postModel) {
        Intent intent = new Intent();
        intent.putExtra("post", postModel);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCreateFailed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.create)
                .setMessage(R.string.message_create_post_failed)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    public void onPostTypeNotFill() {
        Toast.makeText(this, "Post type is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryNotFill() {
        Toast.makeText(this, "Category is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMakeNotFill() {
        Toast.makeText(this, "Make is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onModelNotFill() {
        Toast.makeText(this, "Model is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStockTypeNotFill() {
        Toast.makeText(this, "Stock type is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConditionNotFill() {
        Toast.makeText(this, "Condition is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSpecificationNotFill() {
        Toast.makeText(this, "Specification is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQtyNotFill() {
        Toast.makeText(this, "Qty is not fill", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
