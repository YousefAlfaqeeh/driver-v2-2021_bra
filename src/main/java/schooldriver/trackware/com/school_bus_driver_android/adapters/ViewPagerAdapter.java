package schooldriver.trackware.com.school_bus_driver_android.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad Al-Najjar.
 * date: 8/27/16
 *
 * @version 1.0
 * @since 2.0
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private final List<String> mFragmentTitleList = new ArrayList<String>();
    private final List<String> mFragmentNameList = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    public void onResumeAllFragments() {
        for (int i = 0; i < mFragmentList.size(); i++) {
            mFragmentList.get(i).onResume();
        }
    }


    public void addFragment(Fragment fragment, String title, String fragmentName) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        mFragmentNameList.add(fragmentName);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public String getFragmentName(int position) {
        return mFragmentNameList.get(position);
    }
}