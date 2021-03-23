package exchange.cell.cellexchange.post;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import exchange.cell.cellexchange.api.ApiModule;
import retrofit2.Retrofit;

/**
 * Created by Alexander on 06.10.17.
 */
@Module
public class PostApiModule {

    @Provides
    @Singleton
    public PostApiService postApiService(Retrofit.Builder builder) {
        return builder.baseUrl(ApiModule.BASE_URL + "posts/").build().create(PostApiService.class);
    }


}
