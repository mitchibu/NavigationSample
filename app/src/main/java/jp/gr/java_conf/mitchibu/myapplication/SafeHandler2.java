package jp.gr.java_conf.mitchibu.myapplication;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class SafeHandler2 {
	public static void post(@NonNull LifecycleOwner lifecycleOwner, @NonNull Runnable runnable) {
	}

	public static void post(@NonNull Fragment fragment, @NonNull Runnable runnable) {
		post(Objects.requireNonNull(fragment.getContext()), fragment, runnable);
	}

	public static void post(@NonNull FragmentActivity activity, @NonNull Runnable runnable) {
		post(activity, activity, runnable);
	}

	public static void post(@NonNull Context context, @NonNull LifecycleOwner lifecycleOwner, @NonNull final Runnable runnable) {
		final WeakReference<Lifecycle> lifecycleRef = new WeakReference<>(lifecycleOwner.getLifecycle());
		Handler handler = new Handler(context.getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				Lifecycle lifecycle = lifecycleRef.get();
				if(lifecycle == null) return;

				if(lifecycle.getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
					runnable.run();
				} else {
					lifecycle.addObserver(new LifecycleObserver() {
						@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
						void onResumed(LifecycleOwner owner) {
							runnable.run();
							owner.getLifecycle().removeObserver(this);
						}
					});
				}
			}
		});
	}
}
