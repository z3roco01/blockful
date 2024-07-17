package z3roco01.blockful.render.mesh

class BlockMesh(colours: FloatArray): Mesh(
    floatArrayOf(
         0.5f, -0.5f,  0.5f,
        -0.5f, -0.5f,  0.5f,
        -0.5f,  0.5f,  0.5f,
         0.5f,  0.5f,  0.5f,
         0.5f, -0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        -0.5f,  0.5f, -0.5f,
         0.5f,  0.5f, -0.5f,
    ),
    intArrayOf(
        2, 1, 0,
        3, 2, 0,
        1, 2, 5,
        2, 6, 5,
        5, 6, 4,
        6, 7, 4,
        4, 7, 0,
        7, 3, 0,
        2, 3, 7,
        7, 6, 2,
        1, 5, 0,
        5, 4, 0
    ),
    colours
) {

}