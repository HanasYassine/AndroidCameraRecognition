package licane.androidcamerarecognition;

import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.Manifest;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    // new prog
    //    File file=new File("C:/Users/MSI/Desktop/android/AndroidCameraRecognition/app/src/main/res/others/Licane.txt/");
    SurfaceView cameraView;
    TextView textView;
    TextView text1;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch(requestCode){
           case RequestCameraPermissionID:
           {
               if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                return;

                   }
                   try {
                       cameraSource.start(cameraView.getHolder());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
    }}}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (SurfaceView)findViewById(R.id.surface_view);
        //textView = (TextView)findViewById(R.id.text_view);
        textView = (TextView)findViewById(R.id.textView);

        //*********************************about File**************************/
        //****lire un fichier ****//

        text1 = (TextView)findViewById(R.id.text1);

        String data="";
        text1.setVisibility(View.INVISIBLE);
        StringBuffer sbuffer = new StringBuffer();
        InputStream is = this.getResources().openRawResource(R.raw.testfile);
        BufferedReader reader= new BufferedReader(new InputStreamReader(is));
        if(is !=null){
            try{
                while((data=reader.readLine()) != null){
                    sbuffer.append(data +"n");
                }
                text1.setText(sbuffer);
                is.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        //*****end lire fichier****//
        //******comparer le contenu du fich avec ce qu'on voie***///
           /* public boolean comparee(){

        if(text1.getText().toString().equalsIgnoreCase(textView.getText().toString()))
                }
*/

        //*****ned compare****////
        //********************************end ********************************/

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependcies are not yet available");
        } else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer).setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {


                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                              ActivityCompat.requestPermissions(MainActivity.this,
                                      new String[]{Manifest.permission.CAMERA},
                                     //RequestCameraPermissionID
                                      RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                      }catch(IOException e){
                          e.printStackTrace();
                      }

                  }

                  @Override
                  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                  }

                  @Override
                  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                      cameraSource.stop();

                  }
              });

              textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                  @Override
                  public void release() {

                  }

                  @Override
                  public void receiveDetections(Detector.Detections<TextBlock> detections) {

                      final SparseArray<TextBlock> items = detections.getDetectedItems();
                      if(text1.getText().toString().equalsIgnoreCase(textView.getText().toString())){
                      if(items.size() !=0)
                      {
                          textView.post(new Runnable() {
                              @Override
                              public void run() {
                                   StringBuilder stringBuilder = new StringBuilder();
                                  for(int i=0;i<items.size();++i){
                                      TextBlock item=items.valueAt(i);
                                      stringBuilder.append(item.getValue());
                                      stringBuilder.append("\n");
                                  }
                                  textView.setText(stringBuilder.toString());
                              }
                          });
                      }

                  }}
              });
          }
    }
}
