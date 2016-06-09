package com.allen.iguangwai.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.allen.iguangwai.R;
import com.allen.iguangwai.adapter.FragmentAdapter;
import com.allen.iguangwai.async.QuantaAsync;
import com.allen.iguangwai.dao.ArticleDao;
import com.allen.iguangwai.fragment.MenuFragment;
import com.allen.iguangwai.fragment.TabFragment;
import com.allen.iguangwai.listener.AsyncArticleListListener;
import com.allen.iguangwai.listener.SubareaListener;
import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.model.User;
import com.allen.iguangwai.util.BaseTools;
import com.allen.iguangwai.widget.ExpandableView;

/*
 * 主界面
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

	// public static ArrayList<School> schoolList = new ArrayList<School>();
	/** 贴子列表 */
	public static ArrayList<Article> articleList = new ArrayList<Article>();
	public static QuantaAsync articleListAsync;

	// 左侧菜单
	public SlidingMenu menu;
	// 左上角头像
	private ImageView left_head;
	// 右上角
	private LinearLayout search;
	private LinearLayout publish;
	/** 四个Fragment */
	private TabFragment Tab1_fragment, Tab2_fragment, Tab3_fragment,
			Tab4_fragment;
	/** 展开图标 */
	private ImageView tab1_icon_expand, tab2_icon_expand, tab3_icon_expand,
			tab4_icon_expand;
	/** 展开图标的父类linerLayout,作为触摸区域 */
	private LinearLayout tab1_expand, tab2_expand, tab3_expand, tab4_expand;
	/** 可展开的view */
	private LinearLayout tab1_subAreaView, tab2_subAreaView, tab3_subAreaView,
			tab4_subAreaView;
	// 12个子分区按钮
	public static Button tab1_exam, tab1_share, tab1_answer, Tab2_dormitory,
			Tab2_food, Tab2_travel, Tab3_country, Tab3_school, Tab3_academy,
			Tab3_corporation, Tab4_guide, Tab4_solution;
	// 四个textview
	private TextView tab1tv, tab2tv, tab3tv, tab4tv;
	// 指示器
	private ImageView down_line;
	// viewpager
	private ViewPager viewPager;
	// fragment对象集合
	private ArrayList<Fragment> fragmentsList;
	// 记录当前选中的tab的index
	private int currentIndex = 0;
	// 指示器的偏移量
	private int offset = 0;
	// 屏幕宽度的四分之一
	private int screen1_4;
	private LinearLayout.LayoutParams lp;
	public static User user;// 当前用户
	private long mExitTime=System.currentTimeMillis();// 更新mExitTime
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = (User) getIntent().getSerializableExtra("user");
		user.setName("");
		user.setSignature("");
		// 实例化异步管理器
		articleListAsync = new QuantaAsync(this);

		// 实例化一个article数据库操作类，从数据库返回articleList
		ArticleDao articleDao = new ArticleDao(this);
		articleList = articleDao.getArticleList();

		findViews();// 根据id从xml找到对应的view
		initViews();
		setlisteners();
	}

	/** 初始化layout控件 */
	private void initViews() {

		initMenu();
		initViewPager();
		initAllTab();
	}

	private void findViews() {

		left_head = (ImageView) findViewById(R.id.head);
		publish = (LinearLayout) this.findViewById(R.id.publish_ly);
		search = (LinearLayout) findViewById(R.id.search);
		tab1tv = (TextView) findViewById(R.id.tab1_tv);
		tab2tv = (TextView) findViewById(R.id.tab2_tv);
		tab3tv = (TextView) findViewById(R.id.tab3_tv);
		tab4tv = (TextView) findViewById(R.id.tab4_tv);
		down_line = (ImageView) findViewById(R.id.down_line);
		lp = (LayoutParams) down_line.getLayoutParams();

		screen1_4 = BaseTools.getWindowsWidth(this) / 4; // 屏幕宽度的四分之一
		offset = (screen1_4 - down_line.getLayoutParams().width) / 2;// 计算down_line偏移量
		lp.leftMargin = offset;// 设置down_line初始位置

		// 找到tab的展开图标
		tab1_icon_expand = (ImageView) findViewById(R.id.tab1_icon_expand);
		tab2_icon_expand = (ImageView) findViewById(R.id.tab2_icon_expand);
		tab3_icon_expand = (ImageView) findViewById(R.id.tab3_icon_expand);
		tab4_icon_expand = (ImageView) findViewById(R.id.tab4_icon_expand);

		// 找到对应的展开图标的触摸区域
		tab1_expand = (LinearLayout) findViewById(R.id.tab1_expand);
		tab2_expand = (LinearLayout) findViewById(R.id.tab2_expand);
		tab3_expand = (LinearLayout) findViewById(R.id.tab3_expand);
		tab4_expand = (LinearLayout) findViewById(R.id.tab4_expand);
		// 找到对应tab要展开的view
		tab1_subAreaView = (LinearLayout) findViewById(R.id.tab1_subarea);
		tab2_subAreaView = (LinearLayout) findViewById(R.id.tab2_subarea);
		tab3_subAreaView = (LinearLayout) findViewById(R.id.tab3_subarea);
		tab4_subAreaView = (LinearLayout) findViewById(R.id.tab4_subarea);
		// 12个分区
		tab1_exam = (Button) findViewById(R.id.bt_subarea_exam);
		tab1_share = (Button) findViewById(R.id.bt_subarea_share);
		tab1_answer = (Button) findViewById(R.id.bt_subarea_answer);
		Tab2_dormitory = (Button) findViewById(R.id.bt_subarea_dormitory);
		Tab2_food = (Button) findViewById(R.id.bt_subarea_food);
		Tab2_travel = (Button) findViewById(R.id.bt_subarea_travel);
		Tab3_country = (Button) findViewById(R.id.bt_subarea_country);
		Tab3_school = (Button) findViewById(R.id.bt_subarea_school);
		Tab3_academy = (Button) findViewById(R.id.bt_subarea_academy);
		Tab3_corporation = (Button) findViewById(R.id.bt_subarea_corporation);
		Tab4_guide = (Button) findViewById(R.id.bt_subarea_guide);
		Tab4_solution = (Button) findViewById(R.id.bt_subarea_solution);
	}

	/** 初始化左侧菜单 */
	private void initMenu() {

		MenuFragment MenuFragment = new MenuFragment(this, user);
		setBehindContentView(R.layout.left_menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.id_left_menu_frame, MenuFragment).commit();
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// menu.setBehindWidth()
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);

	}

	/** 初始化viewPager */
	private void initViewPager() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.four_vp);
		fragmentsList = new ArrayList<Fragment>();
		Tab1_fragment = new TabFragment();
		fragmentsList.add(Tab1_fragment);
		Tab2_fragment = new TabFragment();
		fragmentsList.add(Tab2_fragment);
		Tab3_fragment = new TabFragment();
		fragmentsList.add(Tab3_fragment);
		Tab4_fragment = new TabFragment();
		fragmentsList.add(Tab4_fragment);

		viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
				fragmentsList));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

				if (position == 0) {// 0<->1
					lp.leftMargin = (int) (positionOffsetPixels / 4) + offset;
				} else if (position == 1) {// 1<->2
					lp.leftMargin = (int) (positionOffsetPixels / 4)
							+ screen1_4 + offset;
				} else if (position == 2) {// 1<->2
					lp.leftMargin = (int) (positionOffsetPixels / 4) + 2
							* screen1_4 + offset;
				}
				down_line.setLayoutParams(lp);
				currentIndex = position;
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				initIcon();
				setInvisible();
				ExpandableView.isExpand = false;// 设置为不展开
				switch (arg0) {

				case 0:
					tab1_expand.setVisibility(View.VISIBLE);
					break;
				case 1:
					tab2_expand.setVisibility(View.VISIBLE);
					break;
				case 2:
					tab3_expand.setVisibility(View.VISIBLE);
					break;
				case 3:
					tab4_expand.setVisibility(View.VISIBLE);
					break;
				}

			}

		});
	}

	/** 初始化tab界面 */
	private void initAllTab() {
		// TODO Auto-generated method stub
		setInvisible();
		tab1_expand.setVisibility(View.VISIBLE);// 设置第一页的展开图标为可见的
	}

	private void setlisteners() {
		publish.setOnClickListener(this);
		search.setOnClickListener(this);
		tab1tv.setOnClickListener(this);
		tab2tv.setOnClickListener(this);
		tab3tv.setOnClickListener(this);
		tab4tv.setOnClickListener(this);

		left_head.setOnClickListener(this);
		articleListAsync.setQuantaAsyncListener(new AsyncArticleListListener(
				this));
		// 12个分区
		tab1_exam.setOnClickListener(new SubareaListener(Tab1_fragment,
				tab1_exam));
		tab1_share.setOnClickListener(new SubareaListener(Tab1_fragment,
				tab1_share));
		tab1_answer.setOnClickListener(new SubareaListener(Tab1_fragment,
				tab1_answer));
		Tab2_dormitory.setOnClickListener(new SubareaListener(Tab2_fragment,
				Tab2_dormitory));
		Tab2_food.setOnClickListener(new SubareaListener(Tab2_fragment,
				Tab2_food));
		Tab2_travel.setOnClickListener(new SubareaListener(Tab2_fragment,
				Tab2_travel));
		Tab3_country.setOnClickListener(new SubareaListener(Tab3_fragment,
				Tab3_country));
		Tab3_school.setOnClickListener(new SubareaListener(Tab3_fragment,
				Tab3_school));
		Tab3_academy.setOnClickListener(new SubareaListener(Tab3_fragment,
				Tab3_academy));
		Tab3_corporation.setOnClickListener(new SubareaListener(Tab3_fragment,
				Tab3_corporation));
		Tab4_guide.setOnClickListener(new SubareaListener(Tab4_fragment,
				Tab4_guide));
		Tab4_solution.setOnClickListener(new SubareaListener(Tab4_fragment,
				Tab4_solution));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_ly:
			Intent intent1 = new Intent();
			intent1.setClass(MainActivity.this, PublishActivity.class);
			startActivity(intent1);
			break;
		case R.id.search:
			Intent intent2 = new Intent();
			intent2.setClass(MainActivity.this, SearchActivity.class);
			startActivity(intent2);
			break;
		case R.id.tab1_tv:

			viewPager.setCurrentItem(0);// 设置viewPager的当前页
			initIcon();
			setInvisible();
			ExpandableView.isExpand = false;// 设置为子分区view为不展开状态
			tab1_expand.setVisibility(View.VISIBLE);// 设置当前tab的展开图标为可见

			break;
		case R.id.tab2_tv:
			viewPager.setCurrentItem(1);

			initIcon();
			setInvisible();
			ExpandableView.isExpand = false;
			tab2_expand.setVisibility(View.VISIBLE);
			break;
		case R.id.tab3_tv:
			viewPager.setCurrentItem(2);

			initIcon();
			setInvisible();
			ExpandableView.isExpand = false;
			tab3_expand.setVisibility(View.VISIBLE);
			break;
		case R.id.tab4_tv:
			viewPager.setCurrentItem(3);
			initIcon();
			setInvisible();
			ExpandableView.isExpand = false;
			tab4_expand.setVisibility(View.VISIBLE);
			break;

		case R.id.head:
			if (menu.isMenuShowing()) {// 判断菜单是否展开
				menu.showContent();// 收起菜单
			} else {
				menu.showMenu();// 展开菜单
			}
			break;
		}
	}

	/** 设置主分区的所有展开图标和所有子分区view为不可见 */
	private void setInvisible() {
		// TODO Auto-generated method stub
		tab1_expand.setVisibility(View.INVISIBLE);
		tab2_expand.setVisibility(View.INVISIBLE);
		tab3_expand.setVisibility(View.INVISIBLE);
		tab4_expand.setVisibility(View.INVISIBLE);
		tab1_subAreaView.setVisibility(View.GONE);
		tab2_subAreaView.setVisibility(View.GONE);
		tab3_subAreaView.setVisibility(View.GONE);
		tab4_subAreaView.setVisibility(View.GONE);

	}

	/** 初始化主分区的展开图标为未展开状态的图片 */

	private void initIcon() {
		tab1_icon_expand.setImageResource(R.drawable.update_detail_down);
		tab2_icon_expand.setImageResource(R.drawable.update_detail_down);
		tab3_icon_expand.setImageResource(R.drawable.update_detail_down);
		tab4_icon_expand.setImageResource(R.drawable.update_detail_down);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (menu.isMenuShowing()) {// 判断抽屉是否展开
			menu.showContent();// 收起抽屉
			return false;
		}
		else {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();// 更新mExitTime
				} else {
				System.exit(0);// 否则退出程序
				}
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}}