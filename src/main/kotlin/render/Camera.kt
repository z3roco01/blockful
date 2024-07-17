package z3roco01.blockful.render

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(var position: Vector3f, var rotation: Vector3f) {
    constructor() : this(Vector3f(0f, 0f, 0f), Vector3f(0f, 0f, 0f))

    fun setPosition(x: Float, y: Float, z: Float) {
        this.position.x = x
        this.position.y = y
        this.position.z = z
    }

    fun move(moveVec: Vector3f) {
        this.move(moveVec.x, moveVec.y, moveVec.z)
    }

    fun move(distanceX: Float, distanceY: Float, distanceZ: Float) {
        if(distanceZ != 0f) {
            this.position.x += -Math.sin(Math.toRadians(this.rotation.y)) * distanceZ
            this.position.z += Math.cos(Math.toRadians(this.rotation.y)) * distanceZ
        }
        if(distanceX != 0f) {
            this.position.x += -Math.sin(Math.toRadians(this.rotation.y - 90f)) * distanceX
            this.position.z += Math.cos(Math.toRadians(this.rotation.y - 90f)) * distanceX
        }
        this.position.y += distanceY
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        this.rotation.x = x
        this.rotation.y = y
        this.rotation.z = z
    }

    fun rotate(rotateVec: Vector3f) {
        this.rotate(rotateVec.x, rotateVec.y, rotateVec.z)
    }

    fun rotate(x: Float, y: Float, z: Float) {
        rotation.x += x
        rotation.y += y
        rotation.z += z
    }

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