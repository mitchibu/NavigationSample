package jp.gr.java_conf.mitchibu.myapplication.ui.sub;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import jp.gr.java_conf.mitchibu.myapplication.R;
import jp.gr.java_conf.mitchibu.myapplication.ui.main.MainFragmentDirections;

public class SubFragment extends Fragment {

	private SubViewModel mViewModel;

	public static SubFragment newInstance() {
		return new SubFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.sub_fragment, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(SubViewModel.class);
		// TODO: Use the ViewModel
		getView().findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Navigation.findNavController(v).navigate(SubFragmentDirections.actionGlobalEndFragment());
			}
		});
	}

}
