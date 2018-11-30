package jp.gr.java_conf.mitchibu.myapplication;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import jp.gr.java_conf.mitchibu.myapplication.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {
	private NavController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
		setSupportActionBar(binding.toolBar);

		NavHostFragment host = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
		controller = NavHostFragment.findNavController(Objects.requireNonNull(host));
		NavigationUI.setupActionBarWithNavController(this, controller);
	}

	@Override
	public boolean onSupportNavigateUp() {
		return controller.navigateUp() || super.onSupportNavigateUp();
	}
}
