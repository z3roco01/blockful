package z3roco01.blockful.math

/**
 * handles colour formats and conversions for rbga colours
 * @param r the red component of the colour
 * @param g the green component of the colour
 * @param b the blue component of the colour
 * @param a the alpha component of the colour
 */
class Colour(var r: Byte, var g: Byte, var b: Byte, var a: Byte) {
    /**
     * handles colour formats and conversions for rbga colours ( a will be set to 255 )
     * @param r the red component of the colour
     * @param g the green component of the colour
     * @param b the blue component of the colour
     */
    constructor(r: Byte, g: Byte, b: Byte): this(r, g, b, 255.toByte())

    /**
     * handles colour formats and conversions for rgba colours, takes floats inbetween 0.0 and 1.0
     * @param r the red component of the colour
     * @param g the green component of the colour
     * @param b the blue component of the colour
     * @param a the alpha component of the colour
     */
    constructor(r: Float, g: Float, b: Float, a: Float):
            this((r*255).toInt().toByte(), (g*255).toInt().toByte(), (b*255).toInt().toByte(), (a*255).toInt().toByte())

    /**
     * handles colour formats and conversions for rgba colours, takes floats inbetween 0.0 and 1.0
     * @param r the red component of the colour
     * @param g the green component of the colour
     * @param b the blue component of the colour
     */
    constructor(r: Float, g: Float, b: Float): this(r, g, b, 1f)

    /**
     * returns the red component as a 0 to 1 float
     * @return the float red component
     */
    fun getRFloat(): Float = (r/255f)

    /**
     * returns the green component as a 0 to 1 float
     * @return the float green component
     */
    fun getGFloat(): Float = (g/255f)

    /**
     * returns the blue component as a 0 to 1 float
     * @return the float blue component
     */
    fun getBFloat(): Float = (b/255f)

    /**
     * returns the alpha component as a 0 to 1 float
     * @return the float alpha component
     */
    fun getAFloat(): Float = (a/255f)
}