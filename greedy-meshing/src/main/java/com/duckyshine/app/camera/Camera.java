package com.duckyshine.app.camera;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.lwjgl.glfw.GLFW;

import com.duckyshine.app.math.Vector3;

import com.duckyshine.app.debug.Debug;

public class Camera {
    private final float YAW = 90.0f;
    private final float PITCH = 0.0f;
    private final float PITCH_LIMIT = 89.0f;

    private final float SPEED = 2.5f;
    private final float SENSITIVITY = 0.1f;

    private float yaw;
    private float pitch;

    private float lastTime;

    private Vector3f position;
    private Vector3f mousePosition;

    private Vector3f up;
    private Vector3f front;

    private Vector3f velocity;

    private Matrix4f view;

    public Camera() {
        this.position = new Vector3f();

        this.initialise();
    }

    public Camera(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);

        this.initialise();
    }

    public void initialise() {
        this.yaw = this.YAW;
        this.pitch = -this.PITCH;

        this.lastTime = 0.0f;

        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);

        this.velocity = new Vector3f();

        this.view = new Matrix4f();
    }

    public void update(long window, float time) {
        move(window, time);
    }

    private void move(long window, float time) {
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            this.velocity.add(this.front);
        }

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            this.velocity.sub(this.front);
        }

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            this.velocity.add(Vector3.cross(this.front, this.up).normalize());
        }

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            this.velocity.sub(Vector3.cross(this.front, this.up).normalize());
        }

        this.updatePosition(time);
    }

    private void updatePosition(float time) {
        float deltaTime = time - this.lastTime;
        float speed = this.SPEED * deltaTime;

        this.lastTime = time;

        if (this.velocity.length() != 0.0f) {
            this.position.add(this.velocity.normalize().mul(speed));
            this.velocity.zero();
        }

        this.updateView();
    }

    private void updateView() {
        this.view.lookAt(this.position, Vector3.add(this.position, this.front), this.up);
    }

    public void rotate(float xOffset, float yOffset) {
        float theta;
        float omega;

        Vector3f direction;

        this.yaw += xOffset;
        this.pitch = Math.clamp(-this.PITCH_LIMIT, this.PITCH_LIMIT, this.pitch + yOffset);

        theta = Math.toRadians(this.yaw);
        omega = Math.toRadians(this.pitch);

        direction = new Vector3f();

        direction.x = Math.cos(theta) * Math.cos(omega);
        direction.y = Math.sin(omega);
        direction.z = Math.sin(theta) * Math.cos(omega);

        this.front = direction.normalize();
    }

    public String toString() {
        return this.position.toString();
    }
}
