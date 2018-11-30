package jp.gr.java_conf.mitchibu.myapplication.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import jp.gr.java_conf.mitchibu.myapplication.R;

public class MainFragment extends Fragment {

	private MainViewModel mViewModel;

	public static MainFragment newInstance() {
		return new MainFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
		// TODO: Use the ViewModel
		getView().findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainFragmentToSubFragment());
			}
		});
		//TextView t = getView().findViewById(R.id.message);
		//t.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-font-v5.3.1.1.ttf"));
		//t.setText(String.valueOf((char) 0xf000));
	}

}
