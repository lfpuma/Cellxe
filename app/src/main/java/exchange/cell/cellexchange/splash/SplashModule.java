package exchange.cell.cellexchange.splash;

import dagger.Binds;
import dagger.Module;
import exchange.cell.cellexchange.scope.ActivityScope;

/**
 * Created by Alexander on 04.10.17.
 */
@Module
public interface SplashModule {

    @ActivityScope
    @Binds
    SplashView splashView(SplashActivity splashActivity);

}
