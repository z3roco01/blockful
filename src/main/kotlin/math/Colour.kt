package z3roco01.blockful.math

/**
 * handles colour formats and conversions for rbga colours
 * @param r the red component of the colour
 * @param g the green component of the colour
 * @param b the blue component of the colour
 * @param a the alpha component of the colour
 */
class Colour(var r: UByte, var g: UByte, var b: UByte, var a: UByte) {
    /**
     * handles colour formats and conversions for rbga colours
     * @param r the red component of the colour
     * @param g the green component of the colour
     * @param b the blue component of the colour
     * @param a the alpha component of the colour
     */
    constructor(r: Int, g: Int, b: Int, a: Int): this(r.toUByte(), g.toUByte(), b.toUByte(), a.toUByte())

    /**
     * handles colour formats and conversions for rbga colours ( a will be set to 255 )
     * @param r the red component of the colour
     * @param g the green component of the colour
     * @param b the blue component of the colour
     */
    constructor(r: UByte, g: UByte, b: UByte): this(r, g, b, 255.toUByte())

    /**
     * handles colour formats and conversions for rbga colours ( a will be set to 255 )
     * @param r the red component of the colour
     * @param g the green component of the colour
     * @param b the blue component of the colour
     */
    constructor(r: Int, g: Int, b: Int): this(r.toUByte(), g.toUByte(), b.toUByte())

    /**
     * handles colour formats and conversions for rgba colours, takes floats inbetween 0.0 and 1.0
     * @param r the red component of the colour
     * @param g the green component of the colour
     * @param b the blue component of the colour
     * @param a the alpha component of the colour
     */
    constructor(r: Float, g: Float, b: Float, a: Float):
            this((r*255).toInt().toUByte(), (g*255).toInt().toUByte(), (b*255).toInt().toUByte(), (a*255).toInt().toUByte())

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
    fun rFloat(): Float = (r.toInt()/255f)

    /**
     * returns the green component as a 0 to 1 float
     * @return the float green component
     */
    fun gFloat(): Float = (g.toInt()/255f)

    /**
     * returns the blue component as a 0 to 1 float
     * @return the float blue component
     */
    fun bFloat(): Float = (b.toInt()/255f)

    /**
     * returns the alpha component as a 0 to 1 float
     * @return the float alpha component
     */
    fun aFloat(): Float = (a.toInt()/255f)
}