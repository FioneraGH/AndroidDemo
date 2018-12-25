package com.fionera.demo.activity;

import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.fionera.base.activity.BaseActivity;
import com.fionera.base.util.GeneralUtil;
import com.fionera.base.util.ShowToast;
import com.fionera.demo.R;
import com.fionera.demo.util.ShaderHelper;
import com.fionera.demo.util.TextResourceReader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author fionera
 */
public class OpenGlActivity
        extends BaseActivity {
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(mContext);

        final ActivityManager activityManager = (ActivityManager) getSystemService(
                ACTIVITY_SERVICE);
        if (activityManager != null) {
            final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
            final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
            if (supportEs2 || !GeneralUtil.checkArchSupported()) {
                glSurfaceView.setEGLContextClientVersion(2);
                glSurfaceView.setRenderer(new Renderer());
            } else {
                ShowToast.show("Not support OpenGL 2.0");
                finish();
            }
        }

        setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glSurfaceView != null) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glSurfaceView != null) {
            glSurfaceView.onPause();
        }
    }

    private class Renderer
            implements GLSurfaceView.Renderer {
        private static final int BYTES_PER_FLOAT = 4;
        private static final int POSITION_COMPONENT_COUNT = 2;

        private float[] tableVertices = {-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0f, 0.5f, 0f, 0f, -0.25f, 0f, 0.25f, -0.6f,
                -0.6f, 0.6f, 0.6f, -0.6f, 0.6f, -0.6f, -0.6f, 0.6f, -0.6f, 0.6f, 0.6f,};

        private FloatBuffer vertexData;
        private int program;

        private static final String U_COLOR = "u_Color";
        private int uColorLocation;

        private static final String A_POSITION = "a_Position";
        private int aPositionLocation;

        Renderer() {
            vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT).order(
                    ByteOrder.nativeOrder()).asFloatBuffer();
            vertexData.put(tableVertices);
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            try {
                String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext,
                        R.raw.simple_vertex_shader);
                String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext,
                        R.raw.simple_fragment_shader);
                int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
                int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
                program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
                ShaderHelper.validateProgram(program);

                GLES20.glUseProgram(program);
                uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
                aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
                vertexData.position(0);
                GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                        GLES20.GL_FLOAT, false, 0, vertexData);
                GLES20.glEnableVertexAttribArray(aPositionLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i1) {
            
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 10, 6);

            GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

            GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

            GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

            GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
        }
    }
}
