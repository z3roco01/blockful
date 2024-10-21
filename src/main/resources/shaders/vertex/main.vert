#version 330

layout (location =0) in vec3 pos;
layout (location =1) in vec3 inColour;
layout (location =2) in int direction;

out vec3 vertColour;
uniform mat4 projMatrix;
uniform mat4 worldMatrix;

void main() {
    gl_Position = projMatrix * worldMatrix * vec4(pos, 1.0);
    if(direction == 1) {
        vertColour = vec3(0.0f, 0.0f, 0.0f);
    }else {
        vertColour = vec3(0.46484375f, 0.86328125f, 0.46484375f);
    }
}
