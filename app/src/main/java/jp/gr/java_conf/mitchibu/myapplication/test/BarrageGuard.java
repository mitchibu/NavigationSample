package jp.gr.java_conf.mitchibu.myapplication.test;

import android.view.View;

public class BarrageGuard {
	public static View.OnClickListener onClickListener(final View.OnClickListener listener) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listener.onClick(view);
			}
		};
	}
}
