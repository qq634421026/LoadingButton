package online.aroenzhang.loadingbutton.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import online.aroenzhang.loadingbutton.R;

/**
 * Created by zhanghongyu on 2017/10/18.
 */

public class LoadButton extends View {
  public final int NORMAL = 1;
  public final int LOADING = 2;
  public final int PAUSE = 3;
  public final int SUCCESS = 4;
  public final int ERROR = 5;
  private int CURRENT_STUTAS = NORMAL;

  private Paint paint;
  private int mRadiu;
  private int smallRadiu;
  private float start = 0;
  private int currentLength;
  private Paint textPaint;
  private boolean isDrawText;
  private Path path;
  private ValueAnimator mStartingAnimator;
  private float mAnimatorValue = 0;
  private Paint pathPaint;
  private Paint rotePathPaint;
  private Path rotePath;
  private RectF smallCircle;
  private Paint picPaint;

  public LoadButton(Context context) {
    this(context, null);
  }

  public LoadButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoadButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private Animation collectAnimator;
  int i = 0;

  private void iniAnima() {

    mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
    mStartingAnimator.setRepeatCount(100);
    mStartingAnimator.setRepeatMode(ValueAnimator.RESTART);
    mStartingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mAnimatorValue = (float) valueAnimator.getAnimatedValue();
        invalidate();
      }
    });

    mStartingAnimator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
      }

      @Override public void onAnimationRepeat(Animator animation) {
        super.onAnimationRepeat(animation);
      }
    });

    collectAnimator = new Animation() {
      @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
        isDrawText = true;
        currentLength = (int) (toatlWidth - toatlWidth * interpolatedTime);
        if (currentLength <= 2 * mRadiu) {
          currentLength = 2 * mRadiu;
        }
        //Log.i("tag", "==========currentLength=" + currentLength);
        invalidate();
      }
    };
    collectAnimator.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {
        CURRENT_STUTAS = LOADING;
        mStartingAnimator.start();
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
    collectAnimator.setDuration(1000);
    collectAnimator.setInterpolator(new LinearInterpolator());
  }

  private void init() {
    picPaint = new Paint();
    paint = new Paint();
    paint.setColor(Color.GREEN);
    paint.setStyle(Paint.Style.FILL);
    paint.setAntiAlias(true);

    textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setAntiAlias(true);
    textPaint.setTextSize(50);
    textPaint.setStrokeWidth(5);
    setOnClickListener(new MyListener());

    pathPaint = new Paint();
    pathPaint.setColor(Color.WHITE);
    pathPaint.setAntiAlias(true);
    pathPaint.setStrokeWidth(9);
    pathPaint.setStyle(Paint.Style.STROKE);

    path = new Path();
    mMeasure = new PathMeasure();
    smallCircle = new RectF(toatlWidth / 2 - 40, mRadiu - 40, toatlWidth / 2 + 40, mRadiu + 40);
    path.addArc(smallCircle, -90, -359.9f);

    rotePathPaint = new Paint();
    rotePathPaint.setColor(Color.GRAY);
    rotePathPaint.setAntiAlias(true);
    rotePathPaint.setStrokeWidth(9);
    rotePathPaint.setStyle(Paint.Style.STROKE);

    rotePath = new Path();
    rotePath.addArc(smallCircle, -90, -359.9f);
  }

  private int centerWidth;
  private int centerHeight;

  private int toatlWidth;
  private int toatlHeight;
  private PathMeasure mMeasure;

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mRadiu = h / 2;
    smallRadiu = mRadiu - 10;
    centerWidth = w - 2 * mRadiu;
    currentLength = toatlWidth = w;
    iniAnima();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);

    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    int resultW = widthSize;
    int resultH = heightSize;

    int contentW = 0;
    int contentH = 0;
    if (widthMode == MeasureSpec.AT_MOST) {
      contentW = 2 * mRadiu + centerWidth;
      resultW = Math.min(resultW, contentW);
    }

    if (heightMode == MeasureSpec.AT_MOST) {
      contentH = 2 * mRadiu;
      resultH = Math.min(resultH, contentH);
    }
    setMeasuredDimension(resultW, resultH);
  }

  private String s = "点击加载";

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (CURRENT_STUTAS == NORMAL) {
      float start = (float) (toatlWidth * 1.0 / 2 - currentLength * 1.0 / 2);
      canvas.drawRoundRect(start, 0, (float) (toatlWidth * 1.0 / 2 + currentLength * 1.0 / 2),
          2 * mRadiu, 90, 90, paint);
      Rect textRectF = new Rect();
      textPaint.getTextBounds(s, 0, s.length(), textRectF);
      if (!isDrawText) {
        canvas.drawText(s, toatlWidth / 2 - textRectF.width() / 2, mRadiu + textRectF.height() / 3,
            textPaint);
      }
    } else if (CURRENT_STUTAS == LOADING) {
      canvas.drawCircle(toatlWidth / 2, mRadiu, mRadiu, paint);
      canvas.save();
      canvas.translate(toatlWidth / 2, mRadiu);

      mMeasure.setPath(rotePath, false);
      Path dst = new Path();
      mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue, mMeasure.getLength(), dst, true);
      canvas.drawPath(path, pathPaint);
      canvas.drawPath(dst, rotePathPaint);
      canvas.restore();
    } else if (CURRENT_STUTAS == SUCCESS) {
      canvas.drawCircle(toatlWidth / 2, mRadiu, mRadiu, paint);
      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_normal);
      canvas.drawBitmap(bitmap, toatlWidth / 2 - bitmap.getWidth() / 2,
          mRadiu - bitmap.getHeight() / 2, picPaint);
    } else if (CURRENT_STUTAS == ERROR) {
      canvas.drawCircle(toatlWidth / 2, mRadiu, mRadiu, paint);
      Bitmap bitmap =
          BitmapFactory.decodeResource(getResources(), R.drawable.ic_highlight_off_normal);
      canvas.drawBitmap(bitmap, toatlWidth / 2 - bitmap.getWidth() / 2,
          mRadiu - bitmap.getHeight() / 2, picPaint);
    } else if (CURRENT_STUTAS == PAUSE) {
      canvas.drawCircle(toatlWidth / 2, mRadiu, mRadiu, paint);
      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_arrow_normal);
      canvas.drawBitmap(bitmap, toatlWidth / 2 - bitmap.getWidth() / 2,
          mRadiu - bitmap.getHeight() / 2, picPaint);
    }
  }

  private class MyListener implements OnClickListener {

    @Override public void onClick(View view) {
      if (CURRENT_STUTAS == NORMAL) {
        startAnimation(collectAnimator);
      } else if (CURRENT_STUTAS == LOADING) {

        pauseLoading();
        Log.i("tag", "==========暂停");
      } else if (CURRENT_STUTAS == PAUSE) {

        restartLoding();
        Log.i("tag", "==========继续");
      }
    }
  }

  //继续
  public void restartLoding() {
    CURRENT_STUTAS = LOADING;
    mStartingAnimator.resume();
    invalidate();
  }

  //暂停
  public void pauseLoading() {
    CURRENT_STUTAS = PAUSE;
    mStartingAnimator.pause();
    invalidate();
  }

  //停止
  public void cancaleLoading() {
    mStartingAnimator.cancel();
  }

  public void loadingSuccess() {
    CURRENT_STUTAS = SUCCESS;
    invalidate();
  }

  public void loadingError() {
    CURRENT_STUTAS = ERROR;
    invalidate();
  }
}