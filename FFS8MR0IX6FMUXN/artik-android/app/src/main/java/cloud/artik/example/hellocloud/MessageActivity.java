package cloud.artik.example.hellocloud;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.artik.api.MessagesApi;
import cloud.artik.api.UsersApi;
import cloud.artik.client.ApiCallback;
import cloud.artik.client.ApiClient;
import cloud.artik.client.ApiException;
import cloud.artik.model.MessageAction;
import cloud.artik.model.MessageIDEnvelope;
import cloud.artik.model.NormalizedMessagesEnvelope;
import cloud.artik.model.UserEnvelope;

public class MessageActivity extends Activity {
    private static final String TAG = "MessageActivity";

    private static final String RDEVICE_ID = "12f4a53f0cb44ae4b83b3ffd75953429";
    public static final String RKEY_ACCESS_TOKEN = "daed7b8567d248c0a6d58614d67beb7c";  // r set


    private MessagesApi rMessagesApi = null; // r set

    private String rAccessToken;    // r set

    private TextView onionInfo;
    private TextView garlicInfo;
    private TextView chiliInfo;
    private TextView gingerInfo;
    private TextView potatoInfo;
    private TextView greenchilInfo;
    private TextView tempInfo;
    private TextView humidInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        rAccessToken = getIntent().getStringExtra(RKEY_ACCESS_TOKEN);   // r set

        Button getLatestMsgBtn = (Button)findViewById(R.id.getlatest_btn);

        onionInfo = (TextView)findViewById(R.id.textView9);
        garlicInfo = (TextView)findViewById(R.id.textView8);
        gingerInfo = (TextView)findViewById(R.id.textView11);
        chiliInfo = (TextView)findViewById(R.id.textView10);
        potatoInfo = (TextView)findViewById(R.id.textView13);
        greenchilInfo = (TextView)findViewById(R.id.textView12);
        tempInfo = (TextView)findViewById(R.id.textViewTemp);
        humidInfo = (TextView)findViewById(R.id.textViewHumid);
        
        setupArtikCloudApi();

        getLatestMsgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Now get the message
                getRainbowMsg();
            }
        });
    }

    private void setupArtikCloudApi() {
        ApiClient rApiClient = new ApiClient();
        rApiClient.setAccessToken(rAccessToken);
        rMessagesApi = new MessagesApi(rApiClient);

    }

    private void getRainbowMsg() {
        final String tag = TAG + " getLastNormalizedMessagesAsync";
        try {
            int messageCount = 2;
            rMessagesApi.getLastNormalizedMessagesAsync(messageCount, RDEVICE_ID, null,
                    new ApiCallback<NormalizedMessagesEnvelope>() {
                        @Override
                        public void onFailure(ApiException exc, int i, Map<String, List<String>> stringListMap) {
                            processFailure(tag, exc);
                        }

                        @Override
                        public void onSuccess(NormalizedMessagesEnvelope result, int i, Map<String, List<String>> stringListMap) {
                            Log.v(tag, " onSuccess latestMessage = " + result.getData().toString());
                            updateGetResponseOnUIThread(result.getData().get(0).getMid(), result.getData().get(0).getData().toString());
                        }

                        @Override
                        public void onUploadProgress(long bytes, long contentLen, boolean done) {
                        }

                        @Override
                        public void onDownloadProgress(long bytes, long contentLen, boolean done) {
                        }
                    });

        } catch (ApiException exc) {
            processFailure(tag, exc);
        }
    }


    static void showErrorOnUIThread(final String text, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(activity.getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }


    private void updateGetResponseOnUIThread(final String mid, final String msgData) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String delims = "[=}.,:]+";
                String[] tokens = msgData.split(delims);
                onionInfo.setText(tokens[19]+"g");
                garlicInfo.setText(tokens[1]+"g");//4
                gingerInfo.setText(tokens[16]+"g");//7
                chiliInfo.setText(tokens[10]+"g");
                potatoInfo.setText(tokens[4]+"g");//13
                greenchilInfo.setText(tokens[16]+"g");
                tempInfo.setText(tokens[7]+"C");//19
                humidInfo.setText(tokens[13]+"%");//19
            }
        });
    }

    private void processFailure(final String context, ApiException exc) {
        String errorDetail = " onFailure with exception" + exc;
        Log.w(context, errorDetail);
        exc.printStackTrace();
        showErrorOnUIThread(context+errorDetail, MessageActivity.this);
    }

} //MessageActivity

