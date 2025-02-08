package com.duckyshine.app.model;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Buffer {
    private int vertexArrayId;

    private int indexBufferId;
    private int vertexBufferId;

    public Buffer() {
        this.vertexArrayId = 0;

        this.indexBufferId = 0;
        this.vertexBufferId = 0;
    }

    public void setup(float[] vertices, int[] indices) {
        this.vertexArrayId = glGenVertexArrays();

        this.vertexBufferId = glGenBuffers();
        this.indexBufferId = glGenBuffers();

        this.bindVertexArray();

        this.bindVertexBuffer(vertices);
        this.bindIndexBuffer(indices);

        this.setVertexAttributePointer(0, 3, GL_FLOAT, 3 * Float.BYTES, 0);
    }

    public void bindVertexArray() {
        glBindVertexArray(this.vertexArrayId);
    }

    public void detachVertexArray() {
        glBindVertexArray(0);
    }

    private void deleteVertexArray() {
        if (this.vertexArrayId != 0) {
            this.detachVertexArray();

            glDeleteVertexArrays(this.vertexArrayId);

            this.vertexArrayId = 0;
        }
    }

    private void bindVertexBuffer(float[] vertices) {
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);

        glBindBuffer(GL_ARRAY_BUFFER, this.vertexBufferId);

        vertexBuffer.put(vertices).flip();

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }

    private void detachVertexBuffer() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void deleteVertexBuffer() {
        if (this.vertexBufferId != 0) {
            this.detachVertexBuffer();

            glDeleteBuffers(this.vertexBufferId);

            this.vertexBufferId = 0;
        }
    }

    private void bindIndexBuffer(int[] indices) {
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexBufferId);

        indexBuffer.put(indices).flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
    }

    private void detachIndexBuffer() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void deleteIndexBuffer() {
        if (this.indexBufferId != 0) {
            this.detachIndexBuffer();

            glDeleteBuffers(this.indexBufferId);

            this.indexBufferId = 0;
        }
    }

    public void setVertexAttributePointer(int index, int size, int type, int stride, long pointer) {
        glVertexAttribPointer(index, size, type, false, stride, pointer);

        glEnableVertexAttribArray(index);
    }

    public int getVertexArrayId() {
        return this.vertexArrayId;
    }

    public void cleanup() {
        this.deleteVertexArray();

        this.deleteIndexBuffer();
        this.deleteVertexBuffer();
    }
}
