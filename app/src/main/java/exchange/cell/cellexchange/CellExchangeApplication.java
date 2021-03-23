package exchange.cell.cellexchange;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import exchange.cell.cellexchange.info.InfoManager;
import exchange.cell.cellexchange.user.UserManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Alexander on 01.10.17.
 */

public class CellExchangeApplication extends Application implements HasActivityInjector, HasServiceInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Inject
    ImagePipelineConfig imagePipelineConfig;
    @Inject
    InfoManager infoManager;
    @Inject
    UserManager userManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build());

        DaggerAppComponent.builder()
                .app(this)
                .build()
                .inject(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/roboto.regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Fresco.initialize(this, imagePipelineConfig);

//        FirebaseApp.initializeApp(this);

//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
//            e.printStackTrace();
//        });

        infoManager.syncInfoWithCache();

//        if (userManager.isLoggedIn()) {
//            userManager.sendFirebaseToken(FirebaseInstanceId.getInstance().getToken())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(userModel -> {}, Throwable::printStackTrace);
//        }



    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }
}
