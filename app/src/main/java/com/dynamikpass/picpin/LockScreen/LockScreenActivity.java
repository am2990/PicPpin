package com.dynamikpass.picpin.LockScreen;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dynamikpass.picpin.R;

/**
 * This activity renders the UI for the Lock Screen
 * Created by Prakhar on 3/1/15.
 */

public class LockScreenActivity extends Activity {
    KeyguardManager.KeyguardLock k1;
    int WindowWidth;
    int WindowHeight;
    ImageView droid,phone,home;

    int home_x,home_y;
    int[] droidPosition;

    private RelativeLayout.LayoutParams layoutParams;
    Toast toast = null;



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.main);
        droid =(ImageView)findViewById(R.id.droid);
        hideSystemUI();


        System.out.println("measured width"+droid.getMeasuredWidth());
        System.out.println(" width"+droid.getWidth());


        if(getIntent()!=null&&getIntent().hasExtra("kill")&&getIntent().getExtras().getInt("kill")==1){
            // Toast.makeText(this, "" + "kill activityy", Toast.LENGTH_SHORT).show();
            finish();
        }

        try{
            // initialize receiver


            startService(new Intent(this,BackgroundListenerService.class));




  /*      KeyguardManager km =(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        k1 = km.newKeyguardLock("IN");
        k1.disableKeyguard();*/
            StateListener phoneStateListener = new StateListener();
            TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

            WindowWidth =getWindowManager().getDefaultDisplay().getWidth();
            System.out.println("WindowWidth"+ WindowWidth);
            WindowHeight =getWindowManager().getDefaultDisplay().getHeight();
            System.out.println("WindowHeight"+ WindowHeight);

            ViewGroup.MarginLayoutParams marginParams2 = new ViewGroup.MarginLayoutParams(droid.getLayoutParams());

            marginParams2.setMargins((WindowWidth /24)*10,((WindowHeight /32)*8),0,0);

            //marginParams2.setMargins(((WindowWidth-droid.getWidth())/2),((WindowHeight/32)*8),0,0);
            RelativeLayout.LayoutParams layoutdroid = new RelativeLayout.LayoutParams(marginParams2);

            droid.setLayoutParams(layoutdroid);

        /* phone =(ImageView)findViewById(R.id.phone);
        MarginLayoutParams marginParams = new MarginLayoutParams(phone.getLayoutParams());
         marginParams.setMargins(0,WindowHeight/32,WindowWidth/24,0);
         LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(marginParams);
         phone.setLayoutParams(layoutParams1);
*/

            LinearLayout homelinear = (LinearLayout)findViewById(R.id.homelinearlayout);
            homelinear.setPadding(0,0,0,(WindowHeight /32)*3);
            home =(ImageView)findViewById(R.id.home);

            ViewGroup.MarginLayoutParams marginParams1 = new ViewGroup.MarginLayoutParams(home.getLayoutParams());

            marginParams1.setMargins((WindowWidth /24)*10,0,(WindowHeight /32)*8,0);
            // marginParams1.setMargins(((WindowWidth-home.getWidth())/2),0,(WindowHeight/32)*10,0);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(marginParams1);

            home.setLayoutParams(layout);

            droid.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            // Note that system bars will only be "visible" if none of the
                            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                            hideSystemUI();
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                // TODO: The system bars are visible. Make any desired
                                // adjustments to your UI, such as showing the action bar or
                                // other navigational controls.
                                hideSystemUI();
                            } else {
                                // TODO: The system bars are NOT visible. Make any desired
                                // adjustments to your UI, such as hiding the action bar or
                                // other navigational controls.
                                hideSystemUI();
                            }
                        }
                    });

            droid.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    hideSystemUI();
                    switch(event.getAction())
                    {

                        case MotionEvent.ACTION_DOWN:
                            int[] hompos=new int[2];
                            // int[] phonepos=new int[2];
                            droidPosition =new int[2];
                            //phone.getLocationOnScreen(phonepos);
                            home.getLocationOnScreen(hompos);
                            home_x=hompos[0];
                            home_y=hompos[1];
                            //  phone_x=phonepos[0];
                            // phone_y=phonepos[1];


                            break;
                        case MotionEvent.ACTION_MOVE:
                            int x_cord = (int)event.getRawX();
                            int y_cord = (int)event.getRawY();

                            if(x_cord> WindowWidth -(WindowWidth /24)){x_cord= WindowWidth -(WindowWidth /24)*2;}
                            if(y_cord> WindowHeight -(WindowHeight /32)){y_cord= WindowHeight -(WindowHeight /32)*2;}

                            layoutParams.leftMargin = x_cord ;
                            layoutParams.topMargin = y_cord;

                            droid.getLocationOnScreen(droidPosition);
                            v.setLayoutParams(layoutParams);

                            if(((x_cord-home_x)<=(WindowWidth /24)*5 && (home_x-x_cord)<=(WindowWidth /24)*4)&&((home_y-y_cord)<=(WindowHeight /32)*5))
                            {
                                System.out.println("home overlapps");
                                System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+ droidPosition[0]);

                                System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+ droidPosition[1]);

                                v.setVisibility(View.GONE);

                                // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
                                finish();
                            }
                            else{
                                System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+ droidPosition[0]);

                                System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+ droidPosition[1]);


                                System.out.println("home notttt overlapps");
                            }
		             /* if(((x_cord-phone_x)>=128 && (x_cord-phone_x)<=171 )&&((phone_y-y_cord)<=10))
		              {
		            	  System.out.println("phone overlapps");
                       finish();
		              }
		              else{
		            	     System.out.println(phone_x+"  "+(int)event.getRawX()+"  "+x_cord+" "+droidPosition[0]);

			            	 System.out.println(phone_y+"  "+(int)event.getRawY()+"  "+y_cord+" "+droidPosition[1]);


			            	 System.out.println("phone not overlapps" +
			            	 		" overlapps");
		              }*/
                            // v.invalidate();




                            break;
                        case MotionEvent.ACTION_UP:


                            int x_cord1 = (int)event.getRawX();
                            int y_cord2 = (int)event.getRawY();

                            if(((x_cord1-home_x)<=(WindowWidth /24)*5 && (home_x-x_cord1)<=(WindowWidth /24)*4)&&((home_y-y_cord2)<=(WindowHeight /32)*5))
                            {
                                System.out.println("home overlapps");
                                System.out.println("homeee"+home_x+"  "+(int)event.getRawX()+"  "+x_cord1+" "+ droidPosition[0]);

                                System.out.println("homeee"+home_y+"  "+(int)event.getRawY()+"  "+y_cord2+" "+ droidPosition[1]);

                                // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/")));
                                //finish();
                            }
                            else{

                                layoutParams.leftMargin = (WindowWidth /24)*10;
                                layoutParams.topMargin = (WindowHeight /32)*8;
                                v.setLayoutParams(layoutParams);


                            }




                    }

                    return true;
                }
            });

        }catch (Exception e) {
            // TODO: handle exception
        }

    }


    class StateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("call Activity off hook");
                    finish();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };
    public void onSlideTouch( View view, MotionEvent event )
    {
        hideSystemUI();

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int x_cord = (int)event.getRawX();
                int y_cord = (int)event.getRawY();

                if(x_cord> WindowWidth){x_cord= WindowWidth;}
                if(y_cord> WindowHeight){y_cord= WindowHeight;}

                layoutParams.leftMargin = x_cord -25;
                layoutParams.topMargin = y_cord - 75;

                view.setLayoutParams(layoutParams);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss.
    }

    //only used in lockdown mode
    @Override
    protected void onPause() {
        super.onPause();

        // Don't hang around.
        // finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Don't hang around.
        // finish();
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)||(keyCode == KeyEvent.KEYCODE_POWER)||(keyCode == KeyEvent.KEYCODE_VOLUME_UP)||(keyCode == KeyEvent.KEYCODE_CAMERA)) {
            //this is where I can do my stuff
            return true; //because I handled the event
        }
        if((keyCode == KeyEvent.KEYCODE_HOME)){

            return true;
        }

        return false;

    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER ||(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)||(event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
            //Intent i = new Intent(this, NewActivity.class);
            //startActivity(i);
            return false;
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_HOME)){

            System.out.println("alokkkkkkkkkkkkkkkkk");
            return true;
        }
        return false;
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        droid =(ImageView)findViewById(R.id.droid);

        droid.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public void onDestroy(){
        // k1.reenableKeyguard();

        super.onDestroy();
    }

}
