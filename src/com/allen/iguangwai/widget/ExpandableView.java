package com.allen.iguangwai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.allen.iguangwai.R;

//�Զ���ɶ�����ʾ�����ص�view��������ʾ�������������ĸ����ӷ���
public class ExpandableView extends LinearLayout {
	private Context context;
	/** ��չ����view */
	private LinearLayout tab1_subAreaView, tab2_subAreaView, tab3_subAreaView,
			tab4_subAreaView;
	/** չ��ͼ�� */
	private ImageView tab1_icon_expand, tab2_icon_expand, tab3_icon_expand,
			tab4_icon_expand;
	/** չ��ͼ��ĸ���linerLayout,��Ϊ�������� */
	private LinearLayout tab1_expand, tab2_expand, tab3_expand, tab4_expand;
	private int viewHeight = 0;
	public static boolean isExpand;
	private Animation animationDown;
	private Animation animationUp;

	public ExpandableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (this.viewHeight == 0) {
			this.tab1_subAreaView.measure(widthMeasureSpec, 0);
			this.viewHeight = this.tab1_subAreaView.getMeasuredHeight();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// this.mHandleView = (LinearLayout) this
		// .findViewById(R.id.collapse_value);
		tab1_subAreaView = (LinearLayout) this.findViewById(R.id.tab1_subarea);// �ҵ�tab1Ҫչ����view
		tab2_subAreaView = (LinearLayout) this.findViewById(R.id.tab2_subarea);
		tab3_subAreaView = (LinearLayout) this.findViewById(R.id.tab3_subarea);
		tab4_subAreaView = (LinearLayout) this.findViewById(R.id.tab4_subarea);
		// this.mIconExpand = (ImageView) this.findViewById(R.id.icon_value);
		tab1_icon_expand = (ImageView) findViewById(R.id.tab1_icon_expand);// �ҵ�tab1չ��ͼ��
		tab2_icon_expand = (ImageView) findViewById(R.id.tab2_icon_expand);
		tab3_icon_expand = (ImageView) findViewById(R.id.tab3_icon_expand);
		tab4_icon_expand = (ImageView) findViewById(R.id.tab4_icon_expand);
		// LayoutInflater inflater1 = LayoutInflater.from(context);
		// View convertView = inflater1.inflate(R.layout.activity_main,
		// null);
		tab1_expand = (LinearLayout) findViewById(R.id.tab1_expand);
		tab2_expand = (LinearLayout) findViewById(R.id.tab2_expand);
		tab3_expand = (LinearLayout) findViewById(R.id.tab3_expand);
		tab4_expand = (LinearLayout) findViewById(R.id.tab4_expand);
		
		// ���ݶ�Ӧ��view���ü�����
		tab1_expand.setOnClickListener(new ExpandListener(tab1_subAreaView,
				tab1_icon_expand));
		tab2_expand.setOnClickListener(new ExpandListener(tab2_subAreaView,
				tab2_icon_expand));
		tab3_expand.setOnClickListener(new ExpandListener(tab3_subAreaView,
				tab3_icon_expand));
		tab4_expand.setOnClickListener(new ExpandListener(tab4_subAreaView,
				tab4_icon_expand));
		// ImageView down1 = (ImageView) findViewById(R.id.down1);
		// down1.setOnClickListener(new ExpandListener());
		
		// ���ö�Ӧ��viewΪgone
		tab1_subAreaView.setVisibility(View.GONE);
		tab2_subAreaView.setVisibility(View.GONE);
		tab3_subAreaView.setVisibility(View.GONE);
		tab4_subAreaView.setVisibility(View.GONE);
	}

	private class ExpandListener implements View.OnClickListener {
		private View contentView;
		private ImageView tab_expand;

		public ExpandListener(View contentView, ImageView tab_expand) {
			this.contentView = contentView;
			this.tab_expand = tab_expand;
		}

		@Override
		public final void onClick(View paramView) {
			// clearAnimation��view�ķ���
			clearAnimation();
			if (!isExpand) {
				animationDown = new DropDownAnim(contentView, viewHeight, true);
				animationDown.setDuration(50); 
				startAnimation(animationDown);
				// mContentView.startAnimation(AnimationUtils.loadAnimation(
				// mContext, R.anim.animalpha));
				tab_expand.setImageResource(R.drawable.update_detail_up);
				isExpand = true;
			} else {
				isExpand = false;

				animationUp = new DropDownAnim(contentView, viewHeight, false);
				animationUp.setDuration(50); 

				startAnimation(animationUp);
				tab_expand.setImageResource(R.drawable.update_detail_down);
			}
		}
	}

	class DropDownAnim extends Animation {
		/** Ŀ��ĸ߶� */
		private int targetHeight;
		/** Ŀ��view */
		private View view;
		/** �Ƿ�����չ�� */
		private boolean down;

		/**
		 * ���췽��
		 * 
		 * @param targetview
		 *            ��Ҫ��չ�ֵ�view
		 * @param vieweight
		 *            Ŀ�ĸ�
		 * @param isdown
		 *            true:����չ����false:����
		 */
		public DropDownAnim(View targetview, int vieweight, boolean isdown) {
			this.view = targetview;
			this.targetHeight = vieweight;
			this.down = isdown;
		}

		// down��ʱ��interpolatedTime��0������1������newHeightҲ��0������targetHeight
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			int newHeight;
			if (down) {
				newHeight = (int) (targetHeight * interpolatedTime);
			} else {
				newHeight = (int) (targetHeight * (1 - interpolatedTime));
			}
			view.getLayoutParams().height = newHeight;
			view.requestLayout();
			if (view.getVisibility() == View.GONE) {
				view.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}
}
