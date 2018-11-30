package jp.gr.java_conf.mitchibu.myapplication.test;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LifecycleExecutor {
	private final Map<Lifecycle.State, List<Runnable>> runnableMap = new HashMap<>();
	private final Lifecycle lifecycle;
	private final Handler handler;

	public LifecycleExecutor(LifecycleOwner owner) {
		handler = new Handler(Looper.getMainLooper());
		lifecycle = owner.getLifecycle();
		lifecycle.addObserver(new LifecycleObserver() {
			@OnLifecycleEvent(Lifecycle.Event.ON_ANY)
			void onAny(LifecycleOwner owner, Lifecycle.Event event) {
				Lifecycle lifecycle = owner.getLifecycle();
				Set<Map.Entry<Lifecycle.State, List<Runnable>>> entries = runnableMap.entrySet();
				for(Map.Entry<Lifecycle.State, List<Runnable>> entry : entries) {
					Lifecycle.State state = entry.getKey();
					if(lifecycle.getCurrentState().isAtLeast(state)) {
						List<Runnable> runnableList = entry.getValue();
						for(Runnable runnable : runnableList) {
							runnable.run();
						}
						runnableList.clear();
						runnableMap.remove(state);
					}
				}
				if(event == Lifecycle.Event.ON_DESTROY) {
					runnableMap.clear();
					lifecycle.removeObserver(this);
				}
			}
		});
	}

	public void execute(final Lifecycle.State state, final Runnable runnable) {
		if(Looper.getMainLooper().getThread().equals(Thread.currentThread())) {
			if(lifecycle.getCurrentState().isAtLeast(state)) {
				runnable.run();
			} else {
				List<Runnable> runnableList = runnableMap.get(state);
				if(runnableList == null) {
					runnableMap.put(state, runnableList = new ArrayList<>());
				}
				runnableList.add(runnable);
			}
		} else {
			handler.post(new Runnable() {
				@Override
				public void run() {
					execute(state, runnable);
				}
			});
		}
	}

	public void cancel(Runnable runnable) {
		Set<Map.Entry<Lifecycle.State, List<Runnable>>> entries = runnableMap.entrySet();
		for(Map.Entry<Lifecycle.State, List<Runnable>> entry : entries) {
			List<Runnable> runnableList = entry.getValue();
			Iterator<Runnable> it = runnableList.iterator();
			while(it.hasNext()) {
				if(it.next() == runnable) {
					it.remove();
				}
			}
			if(runnableList.isEmpty()) {
				runnableMap.remove(entry.getKey());
			}
		}
	}
}
