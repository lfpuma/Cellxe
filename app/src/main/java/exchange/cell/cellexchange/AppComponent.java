package exchange.cell.cellexchange;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import exchange.cell.cellexchange.api.ApiModule;
import exchange.cell.cellexchange.fresco.FrescoModule;
import exchange.cell.cellexchange.info.InfoApiModule;
import exchange.cell.cellexchange.post.PostApiModule;
import exchange.cell.cellexchange.user.UserApiModule;

/**
 * Created by Alexander on 04.10.17.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        ApiModule.class,
        UserApiModule.class,
        PostApiModule.class,
        FrescoModule.class,
        InfoApiModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder app(CellExchangeApplication application);
        AppComponent build();
    }

    void inject(CellExchangeApplication app);

}
