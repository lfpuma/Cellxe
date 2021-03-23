package exchange.cell.cellexchange.login;

import dagger.Binds;
import dagger.Module;
import exchange.cell.cellexchange.scope.ActivityScope;

/**
 * Created by Alexander on 04.10.17.
 */
@Module
public interface LoginModule {

    @ActivityScope
    @Binds
    LoginView loginView(LoginActivity loginActivity);

}
