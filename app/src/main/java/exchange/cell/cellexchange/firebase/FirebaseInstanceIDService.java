package exchange.cell.cellexchange.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import exchange.cell.cellexchange.user.UserManager;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexander on 05.07.17.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Inject
    UserManager userManager;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {

        String newToken = FirebaseInstanceId.getInstance().getToken();

        userManager.sendFirebaseToken(newToken)
                .subscribeOn(Schedulers.io())
                .subscribe(userModel -> {
                }, Throwable::printStackTrace);


    }
}
