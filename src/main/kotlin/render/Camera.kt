package z3roco01.blockful.render

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f
import game.gameobject.GameObject
import z3roco01.blockful.math.Transformation

/**
 * the camera for the game, handles projection of the view, etc
 */
class Camera(transform: Transformation): GameObject(transform) {
    constructor(position: Vector3f, rotation: Vector3f): this(Transformation(position, rotation, 1f))
    constructor() : this(Transformation())

    // the cameras fov in radians
    val fov: Float = Math.toRadians(90.0f)
    // the near clipping plane
    val zNear = 0.01f
    // and the far clipping plane
    val zFar = 1000f

    /**
     * calculates the perspective matrix then multiplies it by the view matrix from [getViewMatrix]
     * @param aspectRatio the aspect ratio of the camera
     * @return the calculated projection matrix
     */
    fun getProjectionMatrix(aspectRatio: Float) = Matrix4f().perspective(fov, aspectRatio, zNear, zFar).mul(getViewMatrix())

    /**
     * moves the camera by the supplied vector
     * @param moveVec the [Vector3f] to move on
     */
    fun move(moveVec: Vector3f) {
        if(moveVec.z != 0f) {
            transformation.position.x += -Math.sin(Math.toRadians(transformation.rotation.y)) * moveVec.z
            transformation.position.z += Math.cos(Math.toRadians(transformation.rotation.y)) * moveVec.z
        }
        if(moveVec.x != 0f) {
            transformation.position.x += -Math.sin(Math.toRadians(transformation.rotation.y - 90f)) * moveVec.x
            transformation.position.z += Math.cos(Math.toRadians(transformation.rotation.y - 90f)) * moveVec.x
        }
        transformation.position.y += moveVec.y
    }

    /**
     * sets the rotation to the supplied rotation
     * @param x the rotation on the x
     * @param y the rotation on the y
     * @param z the rotation on the z
     */
    fun rotate(x: Float, y: Float, z: Float) {
        transformation.rotation.x += x
        transformation.rotation.y += y
        transformation.rotation.z += z
    }

    /**
     * makes and returns the view matrix for this camera
     * @return the view matrix in a [Matrix4f]
     */
    fun getViewMatrix() = Matrix4f()
        .rotate(Math.toRadians(transformation.rotation.x), Vector3f(1f, 0f, 0f))
        .rotate(Math.toRadians(transformation.rotation.y), Vector3f(0f, 1f, 0f))
        .rotate(Math.toRadians(transformation.rotation.z), Vector3f(0f, 0f, 1f))
        .translate(-transformation.position.x, -transformation.position.y, -transformation.position.z)
}