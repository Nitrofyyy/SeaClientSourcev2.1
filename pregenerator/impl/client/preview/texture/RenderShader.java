// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.texture;

import org.lwjgl.util.vector.Vector3f;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.lwjgl.util.vector.Matrix4f;
import java.util.Iterator;
import org.lwjgl.opengl.GL20;
import org.lwjgl.BufferUtils;
import java.util.HashMap;
import java.nio.FloatBuffer;
import java.util.Map;

public class RenderShader
{
    public static final String VERTEX_FILE = "/pregenerator/base/impl/gui/shader/vertex.txt";
    public static final String FRAGMENT_FILE = "/pregenerator/base/impl/gui/shader/fragment.txt";
    public static RenderShader SHADER;
    int shaderID;
    Map<String, Integer> uniforms;
    FloatBuffer buffer;
    
    public RenderShader(final String vertex, final String fragment, final String... attributes) {
        this.uniforms = new HashMap<String, Integer>();
        this.buffer = BufferUtils.createFloatBuffer(16);
        final int vertexID = this.loadShader(vertex, 35633);
        final int fragmentID = this.loadShader(fragment, 35632);
        if (vertexID == -1 || fragmentID == -1) {
            this.shaderID = -1;
            System.out.println("Couldnt create Shader");
            return;
        }
        GL20.glAttachShader(this.shaderID = GL20.glCreateProgram(), vertexID);
        GL20.glAttachShader(this.shaderID, fragmentID);
        for (int i = 0; i < attributes.length; ++i) {
            GL20.glBindAttribLocation(this.shaderID, i, (CharSequence)attributes[i]);
        }
        GL20.glLinkProgram(this.shaderID);
        GL20.glDetachShader(this.shaderID, vertexID);
        GL20.glDetachShader(this.shaderID, fragmentID);
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);
    }
    
    public RenderShader addUniforms(final String... values) {
        for (final String s : values) {
            this.uniforms.put(s, GL20.glGetUniformLocation(this.shaderID, (CharSequence)s));
        }
        return this;
    }
    
    public RenderShader addArrayUniform(final String name, final int size) {
        for (int i = 0; i < size; ++i) {
            final String indexName = name + "[" + i + "]";
            this.uniforms.put(indexName, GL20.glGetUniformLocation(this.shaderID, (CharSequence)indexName));
        }
        return this;
    }
    
    public RenderShader finish() {
        GL20.glValidateProgram(this.shaderID);
        for (final Map.Entry<String, Integer> entry : this.uniforms.entrySet()) {
            if (entry.getValue() == -1) {
                System.out.println("Uniform[" + entry.getKey() + "] was not found");
            }
        }
        return this;
    }
    
    public boolean isShaderValid() {
        return this.shaderID != -1;
    }
    
    public void startShader() {
        GL20.glUseProgram(this.shaderID);
    }
    
    public void stopShader() {
        GL20.glUseProgram(0);
    }
    
    public void removeShader() {
        if (this.shaderID == -1) {
            return;
        }
        this.stopShader();
        GL20.glDeleteProgram(this.shaderID);
        this.shaderID = -1;
    }
    
    public void loadVec3Array(final String uniformName, final int index, final float x, final float y, final float z) {
        GL20.glUniform3f((int)this.uniforms.get(uniformName + "[" + index + "]"), x, y, z);
    }
    
    public void loadVec3(final String uniformName, final float x, final float y, final float z) {
        GL20.glUniform3f((int)this.uniforms.get(uniformName), x, y, z);
    }
    
    public void loadFloat(final String uniformName, final float value) {
        GL20.glUniform1f((int)this.uniforms.get(uniformName), value);
    }
    
    public void loadInt(final String uniformName, final int value) {
        GL20.glUniform1i((int)this.uniforms.get(uniformName), value);
    }
    
    public void loadMat(final String uniformName, final Matrix4f mat) {
        this.buffer.clear();
        mat.store(this.buffer);
        this.buffer.flip();
        GL20.glUniformMatrix4((int)this.uniforms.get(uniformName), false, this.buffer);
    }
    
    private int loadShader(final String file, final int type) {
        final StringBuilder shaderSource = new StringBuilder();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(RenderShader.class.getResourceAsStream(file)));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (!line.startsWith("//")) {
                    shaderSource.append(line).append("//\n");
                }
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        final int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, (CharSequence)shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, 35713) == 0) {
            System.out.println("Could not compile shader " + file);
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
            return -1;
        }
        return shaderID;
    }
    
    private static RenderShader createShader() {
        final RenderShader shader = new RenderShader("/pregenerator/base/impl/gui/shader/vertex.txt", "/pregenerator/base/impl/gui/shader/fragment.txt", new String[] { "in_position", "in_texture" });
        shader.addUniforms("proViewMatrix", "transform");
        shader.addUniforms("dataMap");
        shader.addArrayUniform("blockColors", 256);
        shader.finish();
        if (shader.isShaderValid()) {
            shader.startShader();
            final float[] data = DisplayTexture.defaultData;
            shader.loadMat("transform", createMatrix(0.0f, 0.0f, 2000.0f, 1.0f));
            shader.stopShader();
        }
        return shader;
    }
    
    public static Matrix4f createMatrix(final float x, final float y, final float z, final float scale) {
        final Matrix4f mat = new Matrix4f();
        mat.translate(new Vector3f(x, y, z));
        mat.scale(new Vector3f(scale, scale, 1.0f));
        return mat;
    }
    
    public static Matrix4f orthoLH(final float left, final float right, final float bottom, final float top, final float zNear, final float zFar, final boolean zZeroToOne) {
        final Matrix4f mat = new Matrix4f();
        final float rm00 = 2.0f / (right - left);
        final float rm2 = 2.0f / (top - bottom);
        final float rm3 = (zZeroToOne ? 1.0f : 2.0f) / (zFar - zNear);
        final float rm4 = (left + right) / (left - right);
        final float rm5 = (top + bottom) / (bottom - top);
        final float rm6 = (zZeroToOne ? zNear : (zFar + zNear)) / (zNear - zFar);
        mat.m30 += mat.m00 * rm4 + mat.m10 * rm5 + mat.m20 * rm6;
        mat.m31 += mat.m01 * rm4 + mat.m11 * rm5 + mat.m21 * rm6;
        mat.m32 += mat.m02 * rm4 + mat.m12 * rm5 + mat.m22 * rm6;
        mat.m33 += mat.m03 * rm4 + mat.m13 * rm5 + mat.m23 * rm6;
        mat.m00 *= rm00;
        mat.m01 *= rm00;
        mat.m02 *= rm00;
        mat.m03 *= rm00;
        mat.m10 *= rm2;
        mat.m11 *= rm2;
        mat.m12 *= rm2;
        mat.m13 *= rm2;
        mat.m20 *= rm3;
        mat.m21 *= rm3;
        mat.m22 *= rm3;
        mat.m23 *= rm3;
        return mat;
    }
    
    static {
        RenderShader.SHADER = createShader();
    }
}
