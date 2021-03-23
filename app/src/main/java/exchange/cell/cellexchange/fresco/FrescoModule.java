package exchange.cell.cellexchange.fresco;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import exchange.cell.cellexchange.CellExchangeApplication;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class FrescoModule {

    public static final int CACHE_IMG_VERSION = 1;
    public static final String CACHE_IMG_DIR = "cache-v1";
    public static final int MAX_CACHE_SIZE = 50 * ByteConstants.MB;
    public static final int MAX_CACHE_SIZE_LOW_DISK = 10 * ByteConstants.MB;
    public static final int MAX_CACHE_SIZE_VERY_LOW_DISK = 5 * ByteConstants.MB;

    @Singleton
    @Provides
    public DiskCacheConfig getDiscCacheConfig(CellExchangeApplication cellExchangeApplication) {
        return DiskCacheConfig.newBuilder(cellExchangeApplication)
                .setBaseDirectoryPath(cellExchangeApplication.getCacheDir())
                .setBaseDirectoryName(CACHE_IMG_DIR)
                .setMaxCacheSize(MAX_CACHE_SIZE)
                .setMaxCacheSizeOnLowDiskSpace(MAX_CACHE_SIZE_LOW_DISK)
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_CACHE_SIZE_VERY_LOW_DISK)
                .setVersion(CACHE_IMG_VERSION)
                .build();
    }

    @Singleton
    @Provides
    public ImagePipelineConfig getImagePipelineConfig(CellExchangeApplication cellExchangeApplication, DiskCacheConfig diskCacheConfig, OkHttpClient okHttpClient) {
        return OkHttpImagePipelineConfigFactory.newBuilder(cellExchangeApplication, okHttpClient)
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapMemoryCacheParamsSupplier(new LollipopBitmapMemoryCacheParamsSupplier((ActivityManager)
                        cellExchangeApplication.getSystemService(Context.ACTIVITY_SERVICE)))
                .build();
    }


}