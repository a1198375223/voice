package git.starstudio.baiduvoice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import git.starstudio.baiduvoice.voice.BaiduVoiceManager;

public class MainActivity extends AppCompatActivity implements BaiduVoiceManager.ResultsAction{

    private TextView mBufferText;
    private TextView mResultText;
    private Button mButton;
    private BaiduVoiceManager mBaiduVoiceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBaiduVoiceManager = new BaiduVoiceManager(this, this);

        mBufferText = (TextView) findViewById(R.id.bufferText);
        mResultText = (TextView) findViewById(R.id.resultText);
        mButton = (Button) findViewById(R.id.button);

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mResultText.setText("");
                        mBufferText.setText("");
                        mButton.setText("说话中.....");
                        mBaiduVoiceManager.startRecognize();
                        break;
                    case MotionEvent.ACTION_UP:
                        mButton.setText("识别中....");
                        mBaiduVoiceManager.stopRecognize();
                        break;
                    default:
                }
                return true;
            }
        });
    }


    @Override
    public void results(String results , int type) {
        if (type == BaiduVoiceManager.ERROR) {
            mResultText.setText(results);
            mButton.setText("点我开始");
        } else if (type == BaiduVoiceManager.SUCCESS){
            //设置返回的结果
            mResultText.setText("Results: "+ results +"\n");
            mButton.setText("点我开始");
        } else if (type == BaiduVoiceManager.TEMPORARY) {
            mBufferText.setText("临时识别结果: " +results + "\n");
        }

    }
}
