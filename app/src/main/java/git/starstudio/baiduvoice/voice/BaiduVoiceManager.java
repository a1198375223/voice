package git.starstudio.baiduvoice.voice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;

/**
 * 创建一个语音识别的Manager类 并实现RecognitionListener接口
 */

public class BaiduVoiceManager  implements RecognitionListener{

    public static final int ERROR = 10000;
    public static final int SUCCESS = 20000;
    public static final int TEMPORARY = 30000;

    private Context mContext;

    private SpeechRecognizer mSpeechRecognizer;

    private ResultsAction mResultsAction;


    //创建一个接口供返回调用
    public interface ResultsAction{
        void results(String results, int type);
    }

    public BaiduVoiceManager(Context context , ResultsAction resultsAction) {
        this.mContext = context;
        this.mResultsAction = resultsAction;
        //创建语音识别实例
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context,
                new ComponentName(mContext, VoiceRecognitionService.class));
        //设置监听器
        mSpeechRecognizer.setRecognitionListener(this);
    }

    //开始启动语音
    public void startRecognize(){
        //此时这个intent 可以绑定参数 详情参见官网
        Intent intent = new Intent();

        mSpeechRecognizer.startListening(intent);
    }

    //停止语音
    public void stopRecognize(){
        mSpeechRecognizer.stopListening();
    }

    //销毁识别语音实例
    public void destory(){
        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.destroy();
        mSpeechRecognizer = null;
    }


    //只有当此方法回调之后才能开始说话，否则会影响识别结果。
    @Override
    public void onReadyForSpeech(Bundle params) {
        Toast.makeText(mContext, "请开始说话", Toast.LENGTH_SHORT).show();
    }

    //当用户开始说话，会回调此方法。
    @Override
    public void onBeginningOfSpeech() {

    }

    //引擎将对每一帧语音回调一次该方法返回音量值。
    //简单来说就是来处理音量变化
    @Override
    public void onRmsChanged(float rmsdB) {

    }

    //此方法会被回调多次，buffer是当前帧对应的PCM语音数据，拼接后可得到完整的录音数据。
    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    //当用户停止说话后，将会回调此方法。
    @Override
    public void onEndOfSpeech() {

    }

    //识别出错，将会回调此方法，调用该方法之后将不再调用onResults方法。
    @Override
    public void onError(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                Toast.makeText(mContext, "音频出错啦！！", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                Toast.makeText(mContext, "其他客户端错误！！！", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                Toast.makeText(mContext, "权限不足，请查询后重试!!!!", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                Toast.makeText(mContext, "网络错误!!!", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                Toast.makeText(mContext, "网络超时!!!", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                Toast.makeText(mContext, "匹配错误!!!", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Toast.makeText(mContext, "识别繁忙!!!", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_SERVER:
                Toast.makeText(mContext, "服务端错误!!!!", Toast.LENGTH_SHORT).show();
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Toast.makeText(mContext, "没有语音输入!!!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(mContext, "其他错误!!!", Toast.LENGTH_SHORT).show();
        }

        mResultsAction.results("出错误啦!!!" ,ERROR);

    }

    //返回最终识别结果，将会回调此方法。
    @Override
    public void onResults(Bundle results) {
        String result = results.get(SpeechRecognizer.RESULTS_RECOGNITION).toString();
        mResultsAction.results(result , SUCCESS);
    }

    //返回临时识别结果，将会回调此方法。
    @Override
    public void onPartialResults(Bundle partialResults) {
        String temporary = partialResults.get(SpeechRecognizer.RESULTS_RECOGNITION).toString();
        mResultsAction.results(temporary,TEMPORARY);
    }

    //返回识别事件，将会回调此方法。
    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
