package exchange.cell.cellexchange.profile;

import dagger.Binds;
import dagger.Module;
import exchange.cell.cellexchange.scope.ActivityScope;

/**
 * Created by Alexander on 06.10.17.
 */
@Module
public interface ProfileModule {

    @Binds
    @ActivityScope
    ProfileView profileView(ProfileActivity profileActivity);

}
