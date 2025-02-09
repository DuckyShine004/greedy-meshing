#version 330 core

layout(location = 0) in vec3 blockPositions;
layout(location = 1) in vec2 textureCoordinates;
layout(location = 2) in float textureIds;

out float outTextureIds;

out vec2 outTextureCoordinates;

out vec4 outVertexColour;

uniform mat4 projectionViewMatrix;

void main() {
    gl_Position = projectionViewMatrix * vec4(blockPositions, 1.0);

    outTextureIds = textureIds;

    outTextureCoordinates = textureCoordinates;

    outVertexColour = vec4(1.0, 1.0, 1.0, 1.0);
};
