package exchange.cell.cellexchange.info;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import exchange.cell.cellexchange.api.ApiModule;
import retrofit2.Retrofit;

/**
 * Created by Alexander on 06.10.17.
 */
@Module
public class InfoApiModule {

    @Provides
    @Singleton
    public InfoApiService infoApiService(Retrofit.Builder builder) {
        return builder.baseUrl(ApiModule.BASE_URL + "info/").build().create(InfoApiService.class);
    }


}
