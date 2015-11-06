package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class RoomMapFragment extends Fragment implements GLSurfaceView.Renderer {

    private Room room;
    private GLSurfaceView surfaceView;

    /* these two object must be manipulated only in the background thread */
    private World world;
    private FrameBuffer frameBuffer;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        surfaceView = new GLSurfaceView(getActivity());

        // we must set the version og the GL engine we are using
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(this);
        return surfaceView;
    }

    @Override
    public void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    @Override
    public void onPause() {
        surfaceView.onPause();
        super.onPause();
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

        Object3D plane = Primitives.getPlane(1,3.0f);
        InputStream in = null;

        try {
            in = GlobalData.getAssetManager().open("polito_map.jpg");
        }
        catch (IOException e) {
            Log.println(Log.ASSERT,"ROOMMAP","error reading file");
            e.printStackTrace();
        }

        Texture mapTexture = null;
        if(in != null)  mapTexture = new Texture(in);
        else            mapTexture = new Texture(1024,1024, RGBColor.RED);

        TextureManager.getInstance().addTexture("mapTexture", mapTexture);
        plane.setTexture("mapTexture");
        plane.translate(5,0,10);
        plane.scale(10);

        world.setAmbientLight(255,255,255);
        world.addObject(plane);
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
}
