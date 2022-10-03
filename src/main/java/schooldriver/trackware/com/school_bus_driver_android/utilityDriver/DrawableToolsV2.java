package schooldriver.trackware.com.school_bus_driver_android.utilityDriver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.AppCompatDrawableManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.OnActionDoneListener;


/**
 * Created by M.Bader on 2/18/2018.
 */

public class DrawableToolsV2 {


//    public static Drawable getTintedDrawableOfColorResId(@NonNull Context context, @NonNull Bitmap inputBitmap,  int colorResId) {
//        return getTintedDrawable(context, new BitmapDrawable(context.getResources(), inputBitmap), ContextCompat.getColor(context, colorResId));
//    }
//
//    public static Drawable getTintedDrawable(@NonNull Context context, @NonNull Bitmap inputBitmap,  int color) {
//        return getTintedDrawable(context, new BitmapDrawable(context.getResources(), inputBitmap), color);
//    }

    public static Drawable getTintedDrawable(@NonNull Context context, @NonNull Drawable inputDrawable, int color) {
        if (color == -1 || color == 0) {
            return inputDrawable;
        }
        Drawable wrapDrawable = DrawableCompat.wrap(inputDrawable);
        DrawableCompat.setTint(wrapDrawable, color);
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        return wrapDrawable;
    }

    public static Drawable getTintedDrawable(Context context, @DrawableRes int drawableResId, int color) {
        @SuppressLint("RestrictedApi")
        Drawable inputDrawable = AppCompatDrawableManager.get().getDrawable(context, drawableResId);
        Drawable wrapDrawable = DrawableCompat.wrap(inputDrawable);
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    public static Drawable getTintListDrawable(Context context, @DrawableRes int drawableResId, ColorStateList colorStateList) {
        @SuppressLint("RestrictedApi")
        Drawable inputDrawable = AppCompatDrawableManager.get().getDrawable(context, drawableResId);
        Drawable wrapDrawable = DrawableCompat.wrap(inputDrawable);
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN);
        DrawableCompat.setTintList(wrapDrawable, colorStateList);
        return wrapDrawable;
    }

//    public static Drawable getTintDrawable(Context context, @DrawableRes int drawableResId, int color) {
//        @SuppressLint("RestrictedApi")
//        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableResId);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            drawable.setTintMode(PorterDuff.Mode.SRC_ATOP);
////        }
//        drawable = DrawableCompat.wrap(drawable).getConstantState().newDrawable();
//        DrawableCompat.setTint(drawable, color);
//        return drawable;
//    }
//
//    public static Drawable getTintListDrawable(Context context, @DrawableRes int drawableResId, ColorStateList colorStateList) {
//        @SuppressLint("RestrictedApi")
//        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableResId);
//        drawable = DrawableCompat.wrap(drawable).getConstantState().newDrawable();
//        DrawableCompat.setTintList(drawable, colorStateList);
//        return drawable;
//    }


    public static RequestOptions GLIDE_SCALE_CENTER_INSIDE = RequestOptions.centerInsideTransform();
    public static RequestOptions GLIDE_SCALE_CIRCLE_CROP = RequestOptions.circleCropTransform();
    public static RequestOptions GLIDE_SCALE_CENTER_CROP = RequestOptions.centerCropTransform();
    public static RequestOptions GLIDE_SCALE_CENTER_FIT = RequestOptions.fitCenterTransform();

//    RequestOptions circleGlideLoading = new RequestOptions().circleCrop().placeholder(R.drawable.circle_shadow);

//    @SuppressLint("CheckResult")
//    public static void loadUrlImageInToImageView(String url, ImageView img_view, OnActionDoneListener onImageLoaded) {
//        RequestBuilder<Drawable> requestBuilder;
//        requestBuilder = Glide.with(img_view).load(url);
//        requestBuilder.listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                if (onImageLoaded != null)
//                    onImageLoaded.OnActionDone(null);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                if (onImageLoaded != null)
//                    onImageLoaded.OnActionDone(resource);
//                return false;
//            }
//        });
//        requestBuilder.submit();
//        requestBuilder.apply(RequestOptions.centerInsideTransform());
//        requestBuilder.into(img_view);
//
//    }
//
//    @SuppressLint("CheckResult")
//    public static void loadDrawableImageInToImageView(int imageId, ImageView img_view, OnActionDoneListener onImageLoaded) {
//        RequestBuilder<Drawable> requestBuilder;
//        requestBuilder = Glide.with(img_view).load(imageId);
//        requestBuilder.listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                if (onImageLoaded != null)
//                    onImageLoaded.OnActionDone(null);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                if (onImageLoaded != null)
//                    onImageLoaded.OnActionDone(resource);
//                return false;
//            }
//        });
//        requestBuilder.submit();
//        requestBuilder.apply(RequestOptions.centerInsideTransform());
//        requestBuilder.into(img_view);
//
//    }

    //    public static RequestBuilder<Drawable> loadFromUrl(Context context, String url) {
////
//        return Glide.with(context).load(url).apply(RequestOptions.centerInsideTransform());
////
//    }
//

    public static <Y extends ImageView> Y loadDrawable(Y imageView, String imageURL, RequestOptions requestOptions) {

        Glide.with(imageView)
                .load(imageURL)
                .apply(requestOptions)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        return imageView;
    }

    public static <Y extends ImageView> Y loadDrawable(Y imageView, String imageURL) {

        Glide.with(imageView)
                .load(imageURL)
                .apply(GLIDE_SCALE_CENTER_FIT)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        return imageView;
    }


    public static <Y extends ImageView> Y loadDrawable(Y imageView, @DrawableRes int drawableId, RequestOptions requestOptions) {
        Glide.with(imageView)
                .load(drawableId)
                .apply(requestOptions)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        return imageView;
    }

    public static <Y extends ImageView> Y loadDrawable(Y imageView, @DrawableRes int drawableId) {
        Glide.with(imageView)
                .load(drawableId)
//                .apply(GLIDE_SCALE_CIRCLE_CROP)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        return imageView;
    }

    public static <Y extends ImageView> Y loadDrawable(Y imageView, String imageUrl, OnActionDoneListener onImageLoaded) {
        RequestBuilder<Drawable> requestBuilder;
        requestBuilder = Glide.with(imageView).load(imageUrl);
        requestBuilder.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (onImageLoaded != null)
                    onImageLoaded.OnActionDone(null);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (onImageLoaded != null)
                    onImageLoaded.OnActionDone(resource);
                return false;
            }
        });
        requestBuilder.apply(RequestOptions.centerInsideTransform()).submit();
        requestBuilder.into(imageView);
        return imageView;

    }


    public static <Y extends ImageView> Y loadDrawable(Y imageView, @DrawableRes int drawableId, OnActionDoneListener onImageLoaded) {
        RequestBuilder<Drawable> requestBuilder;
        requestBuilder = Glide.with(imageView).load(drawableId);
        requestBuilder.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (onImageLoaded != null)
                    onImageLoaded.OnActionDone(null);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (onImageLoaded != null)
                    onImageLoaded.OnActionDone(resource);
                return false;
            }
        });
        requestBuilder.apply(RequestOptions.centerInsideTransform()).submit();
        requestBuilder.into(imageView);
        return imageView;

    }

//
//    public static RequestBuilder<Drawable> loadFromUrl_ToCircleImage(Context context, String url) {
//        return Glide.with(context).load(url).apply(RequestOptions.circleCropTransform());
//    }
//
//    public static RequestBuilder<Drawable> loadFromDrawable_ToCircle(Context context, int imageId) {
//        return Glide.with(context).load(imageId).apply(RequestOptions.circleCropTransform());
//
//    }

}
