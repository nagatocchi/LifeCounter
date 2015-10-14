package com.mio.lifecounter;

import java.util.Random;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class FullscreenActivity extends FragmentActivity {
	PowerManager.WakeLock wl;
	static TextView lifePTwo;
	static TextView lifePOne;
	//LinearLayout test2;
	//Button dummyButton;
	private GestureDetectorCompat gestureDetectorOne;
	private GestureDetectorCompat gestureDetectorTwo;
	static int lifeValueOne;
	static int lifeValueTwo;
	int coinPlayer;
	static int lifeProxy;
	boolean firstRun = true;
	static SharedPreferences preferences;
	static SharedPreferences.Editor editor;
	int initialPOneColorText = 0xFFFFFFFF;
	int initialPTwoColorText = 0xFFFFFFFF;
	int initialPOneColor = 0xFF0099CC;
	int initialPTwoColor = 0xFF9999CC;
	
	private String[] drawerListViewItems;
	private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) { 	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        
		//test2 = (LinearLayout) findViewById(R.id.fullscreen_content_controls);
        //dummyButton = (Button) findViewById(R.id.dummy_button); 
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Your Tag");
        wl.acquire();
        lifePOne = (TextView) findViewById(R.id.lifePOne);
        lifePTwo = (TextView) findViewById(R.id.lifePTwo);
        preferences = getPreferences(MODE_PRIVATE);
        firstRun = preferences.getBoolean("firstrun", true);
        if(firstRun) {  
        	editor = preferences.edit();
	        editor.putBoolean("firstrun", false); // value to store
	        editor.commit();
	        editor.putInt("life", 20); // value to store
	        editor.commit();
	        editor.putInt("color_t1", 0xFFFFFFFF); // value to store
	        editor.commit(); 
	        editor.putInt("color_t2", 0xFFFFFFFF); // value to store
	        editor.commit(); 
	        editor.putInt("color_p1", 0xFF0099CC); // value to store
	        editor.commit(); 
	        editor.putInt("color_p2", 0xFF9999CC); // value to store
	        editor.commit(); 
	        lifeValueOne = 20;
	        lifeValueTwo = 20;
	        lifeProxy = 20;
	        
	        DialogFragment firstRunFragment = new First();
	        firstRunFragment.show(getSupportFragmentManager(), "first");
        }
        else {
        	lifeProxy = preferences.getInt("life",20);
        	lifeValueTwo = lifeProxy;
        	lifeValueOne = lifeProxy;
        	initialPOneColorText = preferences.getInt("color_t1", 0xFFFFFFFF);
        	initialPTwoColorText = preferences.getInt("color_t2", 0xFFFFFFFF);
        	initialPOneColor = preferences.getInt("color_p1", 0xFF000000);
        	initialPTwoColor = preferences.getInt("color_p2", 0xFF000000);
        }
        
        lifePOne.setText(""+lifeValueOne);
        lifePTwo.setText(""+lifeValueTwo);
        lifePOne.setTextColor(initialPOneColorText);
        lifePTwo.setTextColor(initialPTwoColorText);
        lifePOne.setBackgroundColor(initialPOneColor);
        lifePTwo.setBackgroundColor(initialPTwoColor);
        
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //actionBarDrawerToggle = new ActionBarDrawerToggle(
           //     this,                  /* host Activity */
             //   drawerLayout,         /* DrawerLayout object */
             //   R.drawable.ic_launcher,  /* nav drawer icon to replace 'Up' caret */
             //   R.string.drawer_open,  /* "open drawer" description */
             //   R.string.drawer_close  /* "close drawer" description */
             //   ) {

            /** Called when a drawer has settled in a completely closed state. */
           // public void onDrawerClosed(View view) { 	
            	//test2.setVisibility(View.VISIBLE);
            	//dummyButton.setVisibility(View.VISIBLE);
           // }

            /** Called when a drawer has settled in a completely open state. */
            //public void onDrawerOpened(View drawerView) { 
            	//test2.setVisibility(View.GONE);
            	//dummyButton.setVisibility(View.GONE);
           // }
       // };

        // get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.items);
        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        
 
        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_listview_item, drawerListViewItems));
 
 
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
	
        
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        gestureDetectorOne = new GestureDetectorCompat(this, new MyGestureListener(lifePOne));
        gestureDetectorTwo = new GestureDetectorCompat(this, new MyGestureListener(lifePTwo));

        
        lifePOne.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) { 
				gestureDetectorOne.onTouchEvent(event);
				/*boolean proxy = gestureDetector.onTouchEvent(event);
				if(proxy){
					lifeValueOne += lifeProxy;
		    	lifePOne.setText(""+lifeValueOne); }*/
				return true;
				 /* if(event.getActionMasked() == MotionEvent.ACTION_UP)  
					  lifePOne.setText(""+(int)event.getX()+"//"+(int)event.getY());
        	    switch(event.getAction()) {
        	    case (MotionEvent.ACTION_DOWN) :
        	    	lifePOne.setText(""+(int)event.getX()+"//"+(int)event.getY());
        	    return true;
        	    case (MotionEvent.ACTION_MOVE) :
        	    	//clickTest((int)event.getAxisValue(0)/30);
        	    	
        	    //event.getFlags()
        	    return true;
        	    case (MotionEvent.ACTION_UP) :
        	    	//lifePOne.setText("UP");
        	    	//clickTest();
        	    return true;
        	    case (MotionEvent.ACTION_CANCEL) :
        	    	//lifePOne.setText("CANCEL");
        	    return true;
        	    case (MotionEvent.ACTION_OUTSIDE) :
        	    	//lifePOne.setText("outside bounds ");
        	    return true;      
        	    default : 
        	    	return true;
        	    }*/
			};		
        });
        lifePTwo.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event){ 
				gestureDetectorTwo.onTouchEvent(event);
				return true;
			};		
        });

    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //actionBarDrawerToggle.syncState();
    }
    
    @Override
    protected void onPause() {
        super.onPause(); 
        wl.release();
    }//End of onPause

    @Override
    protected void onRestart() {
    	super.onRestart();
    	wl.acquire();
    }//End of onResume
    
    @Override
    protected void onStop() {
        super.onStop(); 
        wl.release();
    }//End of onPause

    @Override
    protected void onResume() {
    	super.onResume();
    	wl.acquire();
    }//End of onResume
    
    /*@Override
    protected void onUserLeaveHint() {
      super.onUserLeaveHint();
      wl.release();
    }*/
    
    public void clickTest(int view, float x) {
    	if(view == 2){
    		if(lifeValueTwo >9000){
    			lifePTwo.setText("Over 9000!"); 
    			lifeValueTwo = 9001;
    		}
    		else if(lifeValueTwo < -9000){
    			lifePTwo.setText("Lost ?"); 
    			lifeValueTwo = -9001;
    		}
    		else{
    			lifeValueTwo += x;
    			lifePTwo.setText(""+lifeValueTwo); 
    		}
    	}
    	else if(view == 1){
    		if(lifeValueTwo >9000){
    			lifePOne.setText("Over 9000!"); 
    			lifeValueOne = 9001;
    		}
    		else if(lifeValueTwo < -9000){
    			lifePOne.setText("Lost ?"); 
    			lifeValueOne = -9001;
    		}
    		else{
    			lifeValueOne += x;
    			lifePOne.setText(""+lifeValueOne);
    		}
    	}
    }
    
    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }*/
    
    public static void resetLife(boolean now, int starting) {
    	lifeProxy = starting;
    	if(now){ 
	    	lifeValueOne = lifeProxy;
			lifeValueTwo = lifeProxy;
			lifePOne.setText(""+lifeValueOne);
	        lifePTwo.setText(""+lifeValueTwo);
    	}
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        /*Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        args.putInt("test", position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.lifePTwo, fragment)
                       .commit();

        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true);
        setTitle(drawerListViewItems[position]);
        drawerLayout.closeDrawer(drawerListView);*/
    	switch(position) {
	    	case 0: onClickColorPickerDialog(position);
				break;
    		case 1: onClickColorPickerDialog(position);
    			break;
    		case 2: onClickColorPickerDialog(position);
				break;
    		case 3: onClickColorPickerDialog(position);
				break;
    		case 4: DialogFragment newFragmentLife = new Life();
			    	newFragmentLife.show(getSupportFragmentManager(), "life");
    			break;
    		case 5: Random rnd = new Random();
    				coinPlayer = rnd.nextInt(2)+1;
    				DialogFragment newFragment = new CoinFlip(coinPlayer);
    			    newFragment.show(getSupportFragmentManager(), "flip");
    			break;
	    	case 6: resetLife(true, lifeProxy);
					Toast.makeText(FullscreenActivity.this, "Reset", Toast.LENGTH_LONG).show();
				break;
	    	default: Toast.makeText(FullscreenActivity.this, "Not Yet Implemented :(", Toast.LENGTH_LONG).show();
    	}
    	drawerListView.setItemChecked(position, false);
    	drawerLayout.closeDrawer(drawerListView);
    }
    public void onClickColorPickerDialog(int pos) {
		//The color picker menu item as been clicked. Show 
		//a dialog using the custom ColorPickerDialog class.
    	initialPOneColor = preferences.getInt("color_p1", 0xFF0099CC);
    	initialPTwoColor = preferences.getInt("color_p2", 0xFF9999CC);	
    	initialPOneColorText = preferences.getInt("color_t1", 0xFFFFFFFF);	
    	initialPTwoColorText = preferences.getInt("color_t2", 0xFFFFFFFF);
    	switch(pos){
    	case 0: final ColorPickerDialog colorDialog = new ColorPickerDialog(this, initialPOneColorText);
    			colorDialog.setAlphaSliderVisible(false);
    			colorDialog.setTitle("Pick a Color!");
    			colorDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(FullscreenActivity.this, "Selected Color: " + colorToHexString(colorDialog.getColor()), Toast.LENGTH_LONG).show();
						editor = preferences.edit();
						editor.putInt("color_t1", colorDialog.getColor());
						editor.commit();
						lifePOne.setTextColor(colorDialog.getColor());
					}
    			});		
    			colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Nothing to do here.
					}
    			});
    			colorDialog.show();
    		break;
    	case 1: final ColorPickerDialog colorDialogTwo = new ColorPickerDialog(this, initialPTwoColorText);
			    colorDialogTwo.setAlphaSliderVisible(false);
			    colorDialogTwo.setTitle("Pick a Color!");
			    colorDialogTwo.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(FullscreenActivity.this, "Selected Color: " + colorToHexString(colorDialogTwo.getColor()), Toast.LENGTH_LONG).show();
						editor = preferences.edit();
						editor.putInt("color_t2", colorDialogTwo.getColor());
						editor.commit();
						lifePTwo.setTextColor(colorDialogTwo.getColor());	
					}
				});		
			    colorDialogTwo.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Nothing to do here.
					}
				});
			    colorDialogTwo.show();
    		break;
    	case 2: final ColorPickerDialog colorDialogThree = new ColorPickerDialog(this, initialPOneColor);
    			colorDialogThree.setAlphaSliderVisible(false);
    			colorDialogThree.setTitle("Pick a Color!");
    			colorDialogThree.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(FullscreenActivity.this, "Selected Color: " + colorToHexString(colorDialogThree.getColor()), Toast.LENGTH_LONG).show();
						editor = preferences.edit();
						editor.putInt("color_p1", colorDialogThree.getColor());
						editor.commit();
						lifePOne.setBackgroundColor(colorDialogThree.getColor());
					}
				});		
    			colorDialogThree.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Nothing to do here.
					}
				});
    			colorDialogThree.show();
    		break;
    	case 3: final ColorPickerDialog colorDialogFour = new ColorPickerDialog(this, initialPTwoColor);
    			colorDialogFour.setAlphaSliderVisible(false);
    			colorDialogFour.setTitle("Pick a Color!");
    			colorDialogFour.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(FullscreenActivity.this, "Selected Color: " + colorToHexString(colorDialogFour.getColor()), Toast.LENGTH_LONG).show();
						editor = preferences.edit();
						editor.putInt("color_p2", colorDialogFour.getColor());
						editor.commit();
						lifePTwo.setBackgroundColor(colorDialogFour.getColor());
					}
    			});		
    			colorDialogFour.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Nothing to do here.
					}
    			});
    			colorDialogFour.show();
    		break;
    	}
	}
	
	private String colorToHexString(int color) {
		return String.format("#%06X", 0xFFFFFF & color);
	}
    
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    	private int view;
    	public MyGestureListener(TextView element){
    		super();
    		if(element.equals(lifePOne))
    			view = 1;
    		else if(element.equals(lifePTwo))
    			view = 2;
    	}
    	@Override
    	public boolean onDown(MotionEvent event){return true;}
    	
    	@Override
    	public boolean onDoubleTapEvent(MotionEvent event){return true;}
    	
    	@Override
    	public boolean onDoubleTap(MotionEvent event) {
    		clickTest(view, -1);
    		return true;
    	}
    	
    	@Override
    	public boolean onSingleTapConfirmed(MotionEvent event) {
    		clickTest(view,-1);
    		return true;
    	}
    	
    	@Override
    	public void onLongPress(MotionEvent event) {
    		clickTest(view,1);
    	}
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, 
                float velocityX, float velocityY) {  
        	//clickTest((int)velocityX);
        	//lifePOne.setText(""+(int)velocityX);
        	if(velocityX < -2000.00 && velocityX < 0.00)
        		clickTest(view,-5);       	
        	else if(velocityX > 2000.00 && velocityX > 0.00)
        		clickTest(view,5);
        	else
        		return false;

        	return true;
        }
        
        /*@Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY)
        {
        	if(distanceX < 0)clickPOne((int)1);
        	else if(distanceX > 0)clickPOne((int)-1);
        	else return false;
        	return true;
        }*/
    }
	
	class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {    
        	drawerListView.setItemChecked(position, true);
        	selectItem(position);
            //Toast.makeText(FullscreenActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();   	
        }
    }
	
	public static class CoinFlip extends DialogFragment { 
		int winner = 0;

	    public CoinFlip(int winner) {
	    	super();
	    	this.winner = winner;
	    }
	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("Player " + winner + " wins!").setTitle(R.string.dialog_title_flip)
	               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	public static class Life extends DialogFragment { 
	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	RelativeLayout linearLayout = new RelativeLayout(getActivity());
	        // Use the Builder class for convenient dialog construction
	    	final NumberPicker picker = new NumberPicker(getActivity());
	    	picker.setMinValue(0);
	    	picker.setMaxValue(999);
	    	
	    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
	        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	        
	        linearLayout.setLayoutParams(params);
	        linearLayout.addView(picker,numPicerParams);
	         
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setView(linearLayout);
	        builder.setMessage("Select starting Life").setTitle(R.string.dialog_title_life)
	               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       	resetLife(false, picker.getValue());
	                       	preferences = getActivity().getPreferences(MODE_PRIVATE);
	                       	editor = preferences.edit();
	                    	editor.putInt("life", picker.getValue()); // value to store
	                       	editor.commit();
	                   }
	               })
	               .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // cancel
	                   }
	               })
	               .setNeutralButton(R.string.okreset, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   	resetLife(true, picker.getValue());
	                	   	preferences = getActivity().getPreferences(MODE_PRIVATE);
	                       	editor = preferences.edit();
	                    	editor.putInt("life", picker.getValue()); // value to store
	                       	editor.commit();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    } 
	}
	
	public static class First extends DialogFragment { 	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(
	        		"Swype from left edge to open the navigation drawer." +
	        		"\nFling left or right to add or subtract life." + 
	        		"\nLongpress to add life." + 
	        		"\nTap to subtract life.").setTitle("First Run Instructions")
	               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) { }
	               });
	        return builder.create();
	    }
	}
	
	public static class Color extends DialogFragment { 	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(
	        		"Swype from left edge to open the navigation drawer." +
	        		"\nFling left or right to add or subtract life." + 
	        		"\nLongpress to add life." + 
	        		"\nTap to subtract life.").setTitle("First Run Instructions")
	               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) { }
	               });
	        return builder.create();
	    }
	}

	
}
