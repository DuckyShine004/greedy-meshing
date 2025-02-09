#version 330 core

out vec4 fragmentColour;

in vec2 outTextureCoordinates;

in vec4 outVertexColour;

void main() {
    fragmentColour = outVertexColour;
};
