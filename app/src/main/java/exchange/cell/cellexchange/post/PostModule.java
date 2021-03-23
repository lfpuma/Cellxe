package exchange.cell.cellexchange.post;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import exchange.cell.cellexchange.CellExchangeApplication;
import exchange.cell.cellexchange.scope.ActivityScope;

/**
 * Created by Alexander on 09.10.17.
 */
@Module
public interface PostModule {

    @Binds
    PostView postView(PostActivity postActivity);


}
