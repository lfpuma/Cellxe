package exchange.cell.cellexchange.register;

import dagger.Binds;
import dagger.Module;
import exchange.cell.cellexchange.scope.ActivityScope;

/**
 * Created by Alexander on 04.10.17.
 */
@Module
public interface RegisterModule {

    @ActivityScope
    @Binds
    RegisterView registerView(RegisterSecondStageActivity registerActivity);

}
