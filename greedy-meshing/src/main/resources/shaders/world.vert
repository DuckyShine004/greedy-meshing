#version 330 core

layout(location = 0) in vec3 aPos;

out vec4 vertexColour;

uniform mat4 projectionViewMatrix;

void main() {
    gl_Position = projectionViewMatrix * vec4(aPos, 1.0);
    vertexColour = vec4(1.0, 1.0, 1.0, 1.0);
};
