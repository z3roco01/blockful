package z3roco01.blockful.render

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * the camera for the game, handles projection of the view, etc
 */
class Camera(var position: Vector3f, var rotation: Vector3f) {
    constructor() : this(Vector3f(0f, 0f, 0f), Vector3f(0f, 0f, 0f))

    /**
     * sets the position of the camera to the supplied coordinates
     * @param coords a [Vector3f] with the coordinates
     */
    fun setPos(coords: Vector3f) {
        this.position = coords
    }

    /**
     * moves the camera by the supplied vector
     * @param moveVec the [Vector3f] to move on
     */
    fun move(moveVec: Vector3f) {
        if(moveVec.z != 0f) {
            this.position.x += -Math.sin(Math.toRadians(this.rotation.y)) * moveVec.z
            this.position.z += Math.cos(Math.toRadians(this.rotation.y)) * moveVec.z
        }
        if(moveVec.x != 0f) {
            this.position.x += -Math.sin(Math.toRadians(this.rotation.y - 90f)) * moveVec.x
            this.position.z += Math.cos(Math.toRadians(this.rotation.y - 90f)) * moveVec.x
        }
        this.position.y += moveVec.y
    }


    /**
     * sets the rotation to the supplied rotation
     * @param rotation the [Vector3f] with the rotation
     */
    fun rotate(rotateVec: Vector3f) {
        this.rotate(rotateVec.x, rotateVec.y, rotateVec.z)
    }

    /**
     * sets the rotation to the supplied rotation
     * @param x the rotation on the x
     * @param y the rotation on the y
     * @param z the rotation on the z
     */
    fun rotate(x: Float, y: Float, z: Float) {
        rotation.x += x
        rotation.y += y
        rotation.z += z
    }

    /**
     * makes and returns the view matrix for this camera
     * @return the view matrix in a [Matrix4f]
     */
    fun getViewMatrix(): Matrix4f {
        val viewMatrix = Matrix4f()
        viewMatrix.identity()

        viewMatrix.rotate(Math.toRadians(this.rotation.x), Vector3f(1f, 0f, 0f))
            .rotate(Math.toRadians(this.rotation.y), Vector3f(0f, 1f, 0f))
            .rotate(Math.toRadians(this.rotation.z), Vector3f(0f, 0f, 1f))

        viewMatrix.translate(-this.position.x, -this.position.y, -this.position.z)

        return viewMatrix
    }
}