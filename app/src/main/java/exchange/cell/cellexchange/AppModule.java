package exchange.cell.cellexchange;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import exchange.cell.cellexchange.firebase.FirebaseInstanceIDService;
import exchange.cell.cellexchange.firebase.FirebaseMessagingService;
import exchange.cell.cellexchange.login.LoginActivity;
import exchange.cell.cellexchange.login.LoginModule;
import exchange.cell.cellexchange.post.NewPostActivity;
import exchange.cell.cellexchange.post.NewPostModule;
import exchange.cell.cellexchange.post.PostActivity;
import exchange.cell.cellexchange.post.PostModule;
import exchange.cell.cellexchange.post.PostsActivity;
import exchange.cell.cellexchange.post.PostsModule;
import exchange.cell.cellexchange.profile.ProfileActivity;
import exchange.cell.cellexchange.profile.ProfileModule;
import exchange.cell.cellexchange.register.RegisterActivity;
import exchange.cell.cellexchange.register.RegisterModule;
import exchange.cell.cellexchange.register.RegisterSecondStageActivity;
import exchange.cell.cellexchange.scope.ActivityScope;
import exchange.cell.cellexchange.splash.SplashActivity;
import exchange.cell.cellexchange.splash.SplashModule;

/**
 * Created by Alexander on 04.10.17.
 */
@Module(includes = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class})
public interface AppModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {SplashModule.class})
    SplashActivity mainActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {LoginModule.class})
    LoginActivity loginActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {RegisterModule.class})
    RegisterSecondStageActivity registerActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {PostsModule.class})
    PostsActivity postsActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ProfileModule.class})
    ProfileActivity profileActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {})
    FirebaseMessagingService firebaseMessagingServiceInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {})
    FirebaseInstanceIDService firebaseInstanceIDServiceInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {PostModule.class})
    PostActivity postActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = {NewPostModule.class})
    NewPostActivity newPostActivityInjector();
}
