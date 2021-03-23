package exchange.cell.cellexchange.post;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Alexander on 09.10.17.
 */
@Module
public interface NewPostModule {

    @Binds
    NewPostView newPostView(NewPostActivity newPostActivity);


}
