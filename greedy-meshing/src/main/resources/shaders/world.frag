#version 330 core

out vec4 fragmentColour;

in vec4 vertexColour;

void main() {
    fragmentColour = vertexColour;
}
