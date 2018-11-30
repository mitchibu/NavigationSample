package jp.gr.java_conf.mitchibu.myapplication.ui.top;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.view.View;

import androidx.navigation.Navigation;
import jp.gr.java_conf.mitchibu.myapplication.test.LifecycleExecutor;

public class TopViewModel extends ViewModel {
	LifecycleExecutor executor;

	public TopViewModel with(LifecycleOwner owner) {
		executor = new LifecycleExecutor(owner);
		return this;
	}

	public void onClick(final View view) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				executor.execute(Lifecycle.State.RESUMED, new Runnable() {
					@Override
					public void run() {
						Navigation.findNavController(view).navigate(TopFragmentDirections.actionTopFragmentToNavigation());
					}
				});
			}
		}, 2000);
	}
}
