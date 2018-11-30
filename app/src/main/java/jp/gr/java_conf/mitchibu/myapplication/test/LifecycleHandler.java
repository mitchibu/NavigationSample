package jp.gr.java_conf.mitchibu.myapplication.test;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class LifecycleHandler {
	private final Map<Lifecycle.State, List<Runnable>> stateRunnableMap = new HashMap<>();
	private final Map<Runnable, List<InnerRunnable>> innerRunnableMap = new HashMap<>();

	private final Lifecycle lifecycle;
	private final Handler handler;

	@SuppressWarnings("WeakerAccess")
	public LifecycleHandler(@NonNull LifecycleOwner owner) {
		handler = new Handler(Looper.getMainLooper());
		lifecycle = owner.getLifecycle();
		lifecycle.addObserver(new LifecycleObserver() {
			@OnLifecycleEvent(Lifecycle.Event.ON_ANY)
			void onAny(LifecycleOwner owner, Lifecycle.Event event) {
				Lifecycle lifecycle = owner.getLifecycle();
				synchronized(stateRunnableMap) {
					Set<Map.Entry<Lifecycle.State, List<Runnable>>> entries = stateRunnableMap.entrySet();
					for(Map.Entry<Lifecycle.State, List<Runnable>> entry : entries) {
						Lifecycle.State state = entry.getKey();
						if(lifecycle.getCurrentState().isAtLeast(state)) {
							List<Runnable> runnableList = entry.getValue();
							for(Runnable runnable : runnableList) {
								runnable.run();
							}
							runnableList.clear();
							stateRunnableMap.remove(state);
						}
					}
					if(event == Lifecycle.Event.ON_DESTROY) {
						stateRunnableMap.clear();
						lifecycle.removeObserver(this);
					}
				}
			}
		});
	}

	public void post(Lifecycle.State state, Runnable runnable) {
		post(state, runnable, 0);
	}

	@SuppressWarnings("WeakerAccess")
	public void post(final Lifecycle.State state, final Runnable runnable, long delay) {
		InnerRunnable inner = new InnerRunnable(state, runnable);
		synchronized(innerRunnableMap) {
			List<InnerRunnable> runnableList = innerRunnableMap.get(runnable);
			if(runnableList == null) {
				innerRunnableMap.put(runnable, runnableList = new ArrayList<>());
			}
			runnableList.add(inner);
		}
		handler.postDelayed(inner, delay);
	}

	public void removeCallbacks(Runnable runnable) {
		synchronized(innerRunnableMap) {
			List<InnerRunnable> runnableList = innerRunnableMap.remove(runnable);
			if(runnableList != null) {
				for(Runnable r : runnableList) {
					handler.removeCallbacks(r);
				}
			}
		}
		synchronized(stateRunnableMap) {
			Set<Map.Entry<Lifecycle.State, List<Runnable>>> entries = stateRunnableMap.entrySet();
			for(Map.Entry<Lifecycle.State, List<Runnable>> entry : entries) {
				List<Runnable> runnableList = entry.getValue();
				Iterator<Runnable> it = runnableList.iterator();
				while(it.hasNext()) {
					if(it.next() == runnable) {
						it.remove();
					}
				}
				if(runnableList.isEmpty()) {
					stateRunnableMap.remove(entry.getKey());
				}
			}
		}
	}

	private class InnerRunnable implements Runnable {
		final Lifecycle.State state;
		final Runnable runnable;

		InnerRunnable(Lifecycle.State state, Runnable runnable) {
			this.state = state;
			this.runnable = runnable;
		}

		@Override
		public void run() {
			synchronized(innerRunnableMap) {
				List<InnerRunnable> runnableList = innerRunnableMap.get(runnable);
				if(runnableList != null) {
					runnableList.remove(this);
					if(runnableList.isEmpty()) {
						innerRunnableMap.remove(runnable);
					}
				}
			}

			if(lifecycle.getCurrentState().isAtLeast(state)) {
				runnable.run();
			} else {
				synchronized(stateRunnableMap) {
					List<Runnable> runnableList = stateRunnableMap.get(state);
					if(runnableList == null) {
						stateRunnableMap.put(state, runnableList = new ArrayList<>());
					}
					runnableList.add(runnable);
				}
			}
		}
	}
}
