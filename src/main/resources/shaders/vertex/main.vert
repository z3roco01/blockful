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
    }else if(direction == 2){
        vertColour = vec3(1.0f, 1.0f, 0.0f);
    }else if(direction == 3){
        vertColour = vec3(0.0f, 1.0f, 1.0f);
    }else if(direction == 4){
        vertColour = vec3(1.0f, 0.0f, 1.0f);
    }else if(direction == 5){
        vertColour = vec3(0.0f, 0.0f, 1.0f);
    }else if(direction == 0) {
        vertColour = vec3(1.0f, 0.0f, 0.0f);
    }
}
