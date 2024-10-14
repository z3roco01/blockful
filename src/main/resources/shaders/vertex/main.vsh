#version 330

layout (location =0) in vec3 pos;
layout (location =1) in vec3 inColour;

out vec3 vertColour;
mat4 pp;
uniform mat4 projMatrix;
uniform mat4 worldMatrix;

void main() {
    gl_Position = projMatrix * worldMatrix * vec4(pos, 1.0);
    vertColour = inColour;
}
