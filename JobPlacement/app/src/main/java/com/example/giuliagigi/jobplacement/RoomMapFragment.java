package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class RoomMapFragment extends Fragment implements GLSurfaceView.Renderer, View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener {

    private static final String BUNDLE_IDENTIFIER = "ROOMMAP";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE__KEY_ROOM = "bundle_room";

    private static final float MOVE_SCALE_FACTOR = 0.025f;
    private static final float SCALE_SCALE_FACTOR = 0.01f;
    private static final int MAX_SCALE = 40;
    private static final int MIN_SCALE = 10;
    private static final int MAP_START_SCALE = 30;
    private static final float COEFFICIENT_X = 0.45f;
    private static final float OFFSET_X = 4.5f;
    private static final float COEFFICIENT_Y = 0.5f;
    private static final float OFFSET_Y = 5f;
    private static final float MARKER_SCALE = 0.005f;

    private GlobalData globalData;
    private Room room;
    private GLSurfaceView surfaceView;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    /* these two object must be manipulated only in the background thread */
    private Activity activity;
    private World world;
    private FrameBuffer frameBuffer;
    private Object3D mapPlane, roomMarker;
    float lastX,lastY, lastSpan;
    boolean isScaling = false;

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- CONSTRUCTOR -----------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    public static RoomMapFragment newInstance(Room room) {
        RoomMapFragment fragment = new RoomMapFragment();
        fragment.room = room;
        return fragment;
    }

    public RoomMapFragment() {
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.globalData = (GlobalData)activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null)
                room = (Room)bundle.get(BUNDLE__KEY_ROOM);
        }

        surfaceView = new GLSurfaceView(getActivity());

        // we must set the version og the GL engine we are using
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8,8,8,8,16,0);
        surfaceView.setRenderer(this);
        surfaceView.setOnTouchListener(this);
        scaleGestureDetector = new ScaleGestureDetector(getActivity().getApplicationContext(),this);
        gestureDetector = new GestureDetector(getActivity().getApplicationContext(), this);
        return surfaceView;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        surfaceView.onResume();
    }

    @Override
    public void onPause() {

        surfaceView.onPause();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        String tail = room.toString();
        outState.putString(BUNDLE_KEY_TAIL,tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
        bundle.put(BUNDLE__KEY_ROOM,room);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- RENDERER METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    /* IMPORTANT: all these methods are performed on a secondary thread, so they shall
    * never access some UI interface element, but only objects of the scene*/

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        world = new World();

        InputStream in = null;
        try {
            in = GlobalData.getAssetManager().open("polito_map.jpg");
        }
        catch (IOException e) {
            Log.println(Log.ASSERT,"ROOMMAP","error reading file");
            e.printStackTrace();
        }

        /* -------- creating textures ------------------------- */
        Texture mapTexture = null;
        if(in != null)  mapTexture = new Texture(in);
        else            mapTexture = new Texture(1024,1024, RGBColor.RED);

        if(!TextureManager.getInstance().containsTexture("mapTexture"))
            TextureManager.getInstance().addTexture("mapTexture", mapTexture);

        Texture markerTexture = new Texture(64,64,RGBColor.RED);
        if(!TextureManager.getInstance().containsTexture("markerTexture"))
            TextureManager.getInstance().addTexture("markerTexture",markerTexture);

        /* -------- creating objects ------------------------- */
        mapPlane = Primitives.getPlane(1, 1);
        mapPlane.setTexture("mapTexture");
        roomMarker = Primitives.getSphere(2);
        roomMarker.setTexture("markerTexture");

        roomMarker.setCenter(new SimpleVector(mapPlane.getCenter().x,mapPlane.getCenter().y,mapPlane.getCenter().z));
        float dx = (room.getXCoordinate() - 0.5f);
        float dy = (room.getYCoordinate() - 0.5f);
        roomMarker.translate(dx,dy,0);
        mapPlane.addChild(roomMarker);

        mapPlane.translate(-dx*MAP_START_SCALE, -dy*MAP_START_SCALE, 10);
        Log.println(Log.ASSERT,"ROOMMAP", " x = " + mapPlane.getTranslation().x + "; y = " + mapPlane.getTranslation().y + "; scale = " + mapPlane.getScale());
        mapPlane.setScale(MAP_START_SCALE);
        roomMarker.scale(MARKER_SCALE);

        world.setAmbientLight(255,255,255);
        world.addObject(mapPlane);
        world.addObject(roomMarker);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        frameBuffer = new FrameBuffer(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        frameBuffer.clear();            // erases the current content of the frame buffer
        world.renderScene(frameBuffer); // applies geometric transforms responsible to create the 2D representation of the world
        world.draw(frameBuffer);        // draws a 2D image of the world on the frame buffer

        frameBuffer.display();          // displays the current content of the frame buffer
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- USER INTERACTION METHODS ----------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        return true;
    }


    /* ------------- scaling --------------------------------------- */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        final float ds = detector.getCurrentSpan() - lastSpan;

        lastSpan = detector.getCurrentSpan();

        surfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {

                float newScale = (mapPlane.getScale())*(1 + ds * SCALE_SCALE_FACTOR);

                newScale = Math.min(MAX_SCALE, newScale);
                newScale = Math.max(MIN_SCALE, newScale);
                mapPlane.setScale(newScale);

                float maxX = mapPlane.getScale()*COEFFICIENT_X - OFFSET_X;
                float minX = -maxX;
                float maxY = mapPlane.getScale()*COEFFICIENT_Y - OFFSET_Y;
                float minY = -maxY;
                float xTranslate = 0, yTranslate = 0;

                if( (mapPlane.getTranslation().x) > maxX)
                    xTranslate = maxX - mapPlane.getTranslation().x;
                else if( (mapPlane.getTranslation().x) < minX)
                    xTranslate = minX - mapPlane.getTranslation().x;

                if( (mapPlane.getTranslation().y) > maxY)
                    yTranslate = maxY - mapPlane.getTranslation().y;
                else if( (mapPlane.getTranslation().y) < minY)
                    yTranslate = minY - mapPlane.getTranslation().y;

                mapPlane.translate(xTranslate, yTranslate, 0);
            }
        });

        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {

        lastSpan = detector.getCurrentSpan();
        isScaling = true;

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

        isScaling = false;
    }


    /* ------------- moving --------------------------------------- */
    @Override
    public boolean onDown(MotionEvent e) {

        if (isScaling) return true;

        lastX = e.getX();
        lastY = e.getY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if (isScaling) return false;

        final float dx = e2.getX() - lastX;
        final float dy = e2.getY() - lastY;

        lastX = e2.getX();
        lastY = e2.getY();

        surfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {

                float maxX = mapPlane.getScale()*COEFFICIENT_X - OFFSET_X;
                float minX = -maxX;
                float maxY = mapPlane.getScale()*COEFFICIENT_Y - OFFSET_Y;
                float minY = -maxY;
                float xTranslate = dx * MOVE_SCALE_FACTOR;
                float yTranslate = dy * MOVE_SCALE_FACTOR;

                if( (mapPlane.getTranslation().x + xTranslate) > maxX)
                    xTranslate = maxX - mapPlane.getTranslation().x;
                else if( (mapPlane.getTranslation().x + xTranslate) < minX)
                    xTranslate = minX - mapPlane.getTranslation().x;

                if( (mapPlane.getTranslation().y + yTranslate) > maxY)
                    yTranslate = maxY - mapPlane.getTranslation().y;
                else if( (mapPlane.getTranslation().y + yTranslate) < minY)
                    yTranslate = minY - mapPlane.getTranslation().y;

                mapPlane.translate(xTranslate, yTranslate, 0);
            }
        });

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
