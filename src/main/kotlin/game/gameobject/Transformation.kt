package game.gameobject

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * holds the info abt the transformation of something and the projection of it
 * @param position the position of this transform
 * @param rotation the rotation of this transform
 * @param scale the scale of the transformation
 */
class Transformation(var position: Vector3f, var rotation: Vector3f, var scale: Float) {
    // constructor with empty params, creates a transform at the origin that isnt scaled from the original size
    constructor(): this(Vector3f(0f), Vector3f(0f), 1f)

    /*fun getProjectionMatrix(camera: Camera, aspectRatio: Float): Matrix4f = getProjectionMatrix(camera.fov, aspectRatio, camera.zNear, camera.zFar)

    /** returns the projection based off the camera and its fov etc
     * @param fov the field of view of the camera
     * @param aspectRatio the aspect ratio of the camera
     * @param zNear the near clipping plane of the camera
     * @param zFar the far clipping plane of the camera
     * @return the projection [Matrix4f] for the camera
     */
    fun getProjectionMatrix(fov: Float, aspectRatio: Float, zNear: Float, zFar: Float): Matrix4f =
        Matrix4f().perspective(fov, aspectRatio, zNear, zFar)*/

    /**
     * calculates the projection matrix for this transforms position, rotation and scale
     * @return the world [Matrix4f] for this transform
     */
    fun getWorldMatrix(): Matrix4f = Matrix4f().translate(position)
        .rotateXYZ(Math.toRadians(rotation.x), Math.toRadians(rotation.y), Math.toRadians(rotation.z))
        .scale(scale)
}