#version 330

layout (location =0) in vec3 pos;
layout (location =1) in vec3 inColour;

out vec3 vertColour;

uniform mat4 projMatrix;

void main() {
    gl_Position = projMatrix * vec4(pos, 1.0);
    vertColour = inColour;
}
