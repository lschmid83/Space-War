package com.citex.spacewar;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * An OpenGL ES renderer based on the GLSurfaceView rendering framework.  This
 * class is responsible for drawing a list of renderables to the screen every
 * frame.  It also manages loading of textures and (when VBOs are used) the
 * allocation of vertex buffer objects.
 * 
 * @author SpriteMethodTest<BR><BR>
 * http://code.google.com/p/apps-for-android/source/browse/trunk/SpriteMethodTest
 */

public class GLSurfaceViewRenderer implements GLSurfaceView.Renderer {

    /** Specifies the format our textures should be converted to upon load. */
    private static BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    /** The screen dimensions */
    public static double mWidth, mHeight;
    /** The game drawing panel */
    public GamePanel mGamePanel;
	/** Store the current time values. */
	private long time1;
	private long time2;
    /** Provides access to an application's raw asset files */
    public static AssetManager mAssetManager;
    /** The screen resolution */
    private float mResoultionWidth, mResolutionHeight;

    /**
     * Constructs the GLSurfaceViewRenderer
     */
    public GLSurfaceViewRenderer(AssetManager assetManager) {
        // Set our bitmaps to 16-bit, 565 format.
        sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    	// Store the current time values.
    	time1 = System.currentTimeMillis();
        mAssetManager = assetManager;
        mResoultionWidth = 400f;
        mResolutionHeight = 240f;
    }

    /**
     * Called whenever the surface is created.  This happens at startup, and
     * may be called again at runtime if the device context is lost (the screen
     * goes to sleep, etc).  This function must fill the contents of vram with
     * texture data and (when using VBOs) hardware vertex arrays.
     * @param gl The GL context
     */
    public void surfaceCreated(GL10 gl) {
        /*
         * Some one-time OpenGL initialization can be made here probably based
         * on features of this particular context
         */
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glDepthMask(false);
        gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_LIGHTING);

        mGamePanel = new GamePanel(gl, mAssetManager);
    }

    /**
     * Called when the size of the window changes.
     * @param gl The GL context
     * @param width The width of the window
     * @param height The height of the window
     */
    public void sizeChanged(GL10 gl, int width, int height) {

        mWidth = width;
        mHeight = height;   	
    	
        gl.glViewport(0, 0, width, height);
        
        /*
         * Set our projection matrix. This doesn't have to be done each time we
         * draw, but usually a new projection needs to be set when the viewport
         * is resized. (http://stackoverflow.com/questions/4914921/help-me-configure-opengl-for-2d)
         */
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(mResolutionHeight, 0.0f, mResoultionWidth, 0.0f, 0.0f, 1.0f);

        //scale
        gl.glScalef( mResolutionHeight / mResoultionWidth, mResoultionWidth/ mResolutionHeight, 1);
        gl.glTranslatef(mResoultionWidth, 0.0f, 0.0f);
        gl.glRotatef(-270.0f, 0.0f, 0.0f, 1.0f);

        gl.glShadeModel(GL10.GL_FLAT);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
        gl.glEnable(GL10.GL_TEXTURE_2D);
    }

    /**
     * Draws the sprites
     * @param gl The GL context
     */
    public void drawFrame(GL10 gl) {

    	time2 = System.currentTimeMillis(); // Get current time
		int delta = (int) (time2 - time1) - 3; // Calculate how long it's been since last updated

        Grid.beginDrawing(gl, true, false);
       	
        // Draw game panel
        mGamePanel.paintComponent(gl, delta);

        Grid.endDrawing(gl);

        time1 = time2; // Update our time variables.             
    }

    /**
     * Gets the returns a standard configuration
     */
    public int[] getConfigSpec() {
        // We don't need a depth buffer, and don't care about our
        // color depth.
        int[] configSpec = {EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE};
        return configSpec;
    }

    /**
     * Called when the rendering thread shuts down.  This is a good place to
     * release OpenGL ES resources.
     * @param gl
     */
    public void shutdown(GL10 gl) {
    	mGamePanel.destroy(gl);
    	
    }
    
    /**
     * Pass the key down event to the GamePanel
     * @param event Description of the key event
     */
    public void onKeyDown(KeyEvent event) {
        mGamePanel.onKeyDown(event);
    }

    
    /**
     * Called when a touch screen motion event occurs
     * @param event The motion event that occurred 
     */
    public void onTouchEvent(final MotionEvent event) {
    	mGamePanel.onTouchEvent(event); 	
    }
    
    /**
     * Pass the key up event to the GamePanel
     * @param event Description of the key event
     */
    public void onKeyUp(KeyEvent event) {
        mGamePanel.onKeyUp(event);
    }
}
