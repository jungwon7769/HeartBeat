package comjungwon7769heartbeat.github.heartbeat;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
	public static Context mainContext;
	FriendListFragment tab_frList;
	AlaramListFragment tab_msgFlagList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mainContext = this;

		TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
		tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_group_white_50dp));
		tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_mail_outline_white_50dp));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		final ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewPager);
		final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

	}

	public void frListRefresh(){
		this.tab_frList.dataRefresh();
	}

	public class PagerAdapter extends FragmentStatePagerAdapter {
		int nTabs;

		public PagerAdapter(FragmentManager fm, int ntab) {
			super(fm);
			this.nTabs = ntab;
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {
				case 0:
					tab_frList = new FriendListFragment();
					return tab_frList;
				case 1:
					tab_msgFlagList = new AlaramListFragment();
					return tab_msgFlagList;
			}
			return null;
		}

		@Override
		public int getCount() {
			return nTabs;
		}
	}
}
