package com.zengye.weixin;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.zengye.weixin.fragment.TabFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, TabFragment.OnFragmentInteractionListener {

    private List<TabFragment> tabFragments = new ArrayList<TabFragment>();
    private ViewPager viewPager;
    private ChangeColorView view1;
    private ChangeColorView view2;
    private ChangeColorView view3;
    private ChangeColorView view4;
    private List<ChangeColorView> changeColorViews = new ArrayList<ChangeColorView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowShowingAlways();

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        view1 = (ChangeColorView) findViewById(R.id.icon1);
        view2 = (ChangeColorView) findViewById(R.id.icon2);
        view3 = (ChangeColorView) findViewById(R.id.icon3);
        view4 = (ChangeColorView) findViewById(R.id.icon4);

        view1.setIconAlpha(1.0f);

        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        view4.setOnClickListener(this);

        changeColorViews.add(view1);
        changeColorViews.add(view2);
        changeColorViews.add(view3);
        changeColorViews.add(view4);

        TabFragment tabFragment1 = TabFragment.newInstance("first");
        TabFragment tabFragment2 = TabFragment.newInstance("second");
        TabFragment tabFragment3 = TabFragment.newInstance("third");
        TabFragment tabFragment4 = TabFragment.newInstance("fourth");

        tabFragments.add(tabFragment1);
        tabFragments.add(tabFragment2);
        tabFragments.add(tabFragment3);
        tabFragments.add(tabFragment4);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ChangeColorView left = changeColorViews.get(position);
                    ChangeColorView right = changeColorViews.get(position + 1);
                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                changeColorViews.get(position).setIconAlpha(0f);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        /**
         * 反射设置菜单显示图标
         */
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        for (ChangeColorView changeColorView : changeColorViews) {
            changeColorView.setIconAlpha(0f);
        }

        switch (id) {
            case R.id.icon1:
                changeColorViews.get(0).setIconAlpha(1f);
                viewPager.setCurrentItem(0);
                break;
            case R.id.icon2:
                changeColorViews.get(1).setIconAlpha(1f);

                viewPager.setCurrentItem(1);

                break;
            case R.id.icon3:
                changeColorViews.get(2).setIconAlpha(1f);

                viewPager.setCurrentItem(2);

                break;
            case R.id.icon4:
                changeColorViews.get(3).setIconAlpha(1f);
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabFragments.size();
        }
    }


}
