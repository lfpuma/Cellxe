package exchange.cell.cellexchange.post;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import exchange.cell.cellexchange.scope.ActivityScope;

/**
 * Created by Alexander on 06.10.17.
 */
@Module
public interface PostsModule {

    @Binds
    PostsView postsView(PostsActivity postsActivity);


}
