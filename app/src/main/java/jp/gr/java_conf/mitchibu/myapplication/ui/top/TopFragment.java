package jp.gr.java_conf.mitchibu.myapplication.ui.top;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.gr.java_conf.mitchibu.myapplication.R;
import jp.gr.java_conf.mitchibu.myapplication.databinding.TopFragmentBinding;

public class TopFragment extends Fragment {

	private TopViewModel model;

	public static TopFragment newInstance() {
		return new TopFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model = ViewModelProviders.of(this).get(TopViewModel.class).with(this);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		TopFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.top_fragment, container, false);
		binding.setLifecycleOwner(this);
		binding.setModel(model);
		return binding.getRoot();
	}
}
