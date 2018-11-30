package jp.gr.java_conf.mitchibu.myapplication.ui.end;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.gr.java_conf.mitchibu.myapplication.R;

public class EndFragment extends Fragment {

	private EndViewModel mViewModel;

	public static EndFragment newInstance() {
		return new EndFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.end_fragment, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(EndViewModel.class);
		// TODO: Use the ViewModel
	}

}
