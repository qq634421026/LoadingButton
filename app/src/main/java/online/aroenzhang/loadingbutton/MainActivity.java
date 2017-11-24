package online.aroenzhang.loadingbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import online.aroenzhang.loadingbutton.ui.LoadButton;

public class MainActivity extends AppCompatActivity {

  private LoadButton loadingbutton;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //测试哦是是是
    loadingbutton = (LoadButton) findViewById(R.id.loadingbutton);
  }

  public void pause(View view) {
    loadingbutton.pauseLoading();
  }

  public void restar(View view) {
    loadingbutton.restartLoding();
  }

  public void success(View view) {
    loadingbutton.loadingSuccess();
  }

  public void error(View view) {
    loadingbutton.loadingError();
  }

  public void cancle(View view) {
    loadingbutton.cancaleLoading();
  }
}
