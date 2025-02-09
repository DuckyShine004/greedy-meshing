#version 330 core

in vec2 outTextureCoordinates;

in vec4 outVertexColour;

out float outTextureIndex;

out vec4 fragmentColour;

uniform sampler2DArray textureArray;

void main() {
    vec4 textureColour = texture(textureArray, vec3(outTextureCoordinates, outTextureIndex));
    fragmentColour = textureColour;
};
