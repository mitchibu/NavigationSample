package jp.gr.java_conf.mitchibu.myapplication.test;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.Objects;

public class SafeHandler extends Handler {
	public boolean postWithLifecycle(@NonNull FragmentActivity activity, Lifecycle.State state, @NonNull Runnable runnable) {
		return postWithLifecycle(activity, activity, state, runnable);
	}

	public boolean postWithLifecycle(@NonNull Fragment fragment, Lifecycle.State state, @NonNull Runnable runnable) {
		return postWithLifecycle(Objects.requireNonNull(fragment.getContext()), fragment, state, runnable);
	}

	private boolean postWithLifecycle(@NonNull Context context, @NonNull LifecycleOwner lifecycleOwner, final Lifecycle.State state, @NonNull final Runnable runnable) {
		if(!context.getMainLooper().equals(getLooper())) throw new IllegalThreadStateException();

		final Lifecycle lifecycle = lifecycleOwner.getLifecycle();
		return post(new Runnable() {
			@Override
			public void run() {
				if(lifecycle.getCurrentState().isAtLeast(state)) {
					runnable.run();
				} else {
					lifecycle.addObserver(new LifecycleObserver() {
						@OnLifecycleEvent(Lifecycle.Event.ON_ANY)
						void onAny(LifecycleOwner owner) {
							Lifecycle lifecycle = owner.getLifecycle();
							if(!lifecycle.getCurrentState().isAtLeast(state)) return;
							runnable.run();
							owner.getLifecycle().removeObserver(this);
						}
					});
				}
			}
		});
	}
}
