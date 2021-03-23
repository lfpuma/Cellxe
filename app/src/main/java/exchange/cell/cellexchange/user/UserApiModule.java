package exchange.cell.cellexchange.user;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import exchange.cell.cellexchange.api.ApiModule;
import retrofit2.Retrofit;

/**
 * Created by Alexander on 06.10.17.
 */
@Module
public class UserApiModule {

    @Provides
    @Singleton
    public UserApiService userApiService(Retrofit.Builder builder) {
        return builder.baseUrl(ApiModule.BASE_URL + "users/").build().create(UserApiService.class);
    }


}
