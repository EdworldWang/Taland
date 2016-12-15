/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

package com.dragon.navigation.use;

import java.nio.Buffer;


public class MyArrow extends MeshObject
{
    //please notice the point between the trangle
    // Data for drawing the 3D plane as overlay
    private static final double cubeVertices[]  = {
            0, 2, 0.1,
            -2, 0, 0.1,
            2, 0, 0.1,  //front

            0, 2, -0.1,
            -2, 0, -0.1,
            -2, 0, 0.1, //left top one

            0, 2, -0.1,
            -2, 0, 0.1,
            0, 2, 0.1, //left top two

            0, 2, -0.1,
            2, 0, -0.1,
            -2, 0, -0.1, //back

            0, 2, 0.1,
            2, 0, 0.1,
            2, 0, -0.1,  //right top one

            0, 2, 0.1,
            2, 0,-0.1,
            0, 2, -0.1, //right top two

            -2, 0, 0.1,
            -2, 0, -0.1,
            2,  0, -0.1,  //trangle bottom one

            -2, 0, 0.1,
            2,  0, -0.1,
            2, 0, 0.1,   //trangle bottom two

           -0.764, 0, 0.1,
            -0.764,  -3.2356, 0.1,
            0.764,-3.2356, 0.1,
             //square front one

            -0.764, 0, 0.1,
            0.764,  -3.2356, 0.1,
            0.764,  0, 0.1,   //square front two


            0.764, 0, -0.1,
            0.764, -3.2356, -0.1,
            -0.764, -3.2356, -0.1, //back square one

            0.764, 0, -0.1,
            -0.764,-3.2356, -0.1,
            -0.764, 0, -0.1,        //back square two

            -0.764, 0, -0.1,         //bottom left one
            -0.764, -3.2356, -0.1,
            -0.764, -3.2356, 0.1,

            -0.764, 0, -0.1,        //bottom left two
            -0.764, -3.2356, 0.1,
            -0.764, 0, 0.1,

            0.764, 0, 0.1,           //bottom right one
            0.764, -3.2356, 0.1,
            0.764, -3.2356, -0.1,

           0.764, 0, 0.1,           //bottom right two
            0.764, -3.2356, -0.1,
            0.764, 0, -0.1,

            -0.764, -3.2356, 0.1,    //bottom one
            -0.764, -3.2356, -0.1,
            0.764, -3.2356, -0.1,

            -0.764, -3.2356, 0.1,   //bottom two
            0.764, -3.2356, -0.1,
            0.764, -3.2356, 0.1



    };

    private static final double cubeTexcoords[] = {
            0, 0, 1, 0, 1, 1,
            0, 1, 1, 0, 0, 0,
            0, 1, 1, 1, 0, 0,
            1, 0, 1, 1, 0, 1,
            1, 0, 0, 0, 0, 1,
            1, 1, 0, 0, 1, 0,
            1, 1, 0, 1, 1, 0,
            0, 0, 0, 1, 1, 1,
            0, 0, 1, 0, 1, 1,
            0, 1, 1, 0, 0, 0,
            0, 1, 1, 1, 0, 0,
            1, 0, 1, 1, 0, 1,
            1, 0, 0, 0, 0, 1,
            1, 1, 0, 0, 1, 0,
            1, 1, 0, 1, 1, 0,
            0, 0, 0, 1, 1, 1,
            0, 0, 1, 0, 1, 1,
            0, 1, 1, 0, 0, 0,
            0, 1, 1, 1, 0, 0,
            1, 0, 1, 1, 0, 1,
            1, 0, 0, 0, 0, 1,
            1, 1, 0, 0, 1, 0,
            1, 1, 0, 1, 1, 0,
            0, 0, 0, 1, 1, 1,
            0, 1, 1, 0, 0, 0,
            0, 1, 1, 1, 0, 0,
            1, 0, 1, 1, 0, 1,
            1, 0, 0, 0, 0, 1,
            1, 1, 0, 0, 1, 0,
            1, 1, 0, 1, 1, 0,
            0, 0, 0, 1, 1, 1
    };


    private static final double cubeNormals[]   = {
            0, 0, 1,  0, 0, 1,  0, 0, 1, //front
            -1, 1, 0, -1, 1, 0, -1, 1, 0, //left one
            -1, 1, 0, -1, 1, 0, -1, 1, 0, //left two
             0, 0, -1,   0, 0, 1,  0, 0, -1, //back
            1,1,0,  1, 1, 0, 1,1,0,  //right one
            1,1,0,  1, 1, 0, 1,1,0,  //right two
            0, -1,0 ,0,-1,0, 0,-1,0,  //bottom one
            0, -1,0 ,0,-1,0, 0,-1,0,  //bottom two
            0,0,1,  0,0,1,  0,0,1,     //square front  one
            0,0,1,  0,0,1,  0,0,1,    //square front two

            0,0,-1,  0,0,-1,  0,0,-1,     //back square  one
            0,0,-1,  0,0,-1,  0,0,-1,       //back square two

            -1,0,0,   -1,0,0, -1,0,0,//bottom left one
            -1,0,0,   -1,0,0, -1,0,0,//bottom left two

            1,0,0,      1,0,0,      1,0,0,//bottom right one
            1,0,0,      1,0,0,      1,0,0,//bottom right two

            0,-1,0,     0,-1,0,      0,-1,0,
            0,-1,0,     0,-1,0,     0,-1,0
    };

    private static final short  cubeIndices[]   = {
            0, 1, 2, 0, 2, 3, // front
            4, 6, 5, 4, 7, 6, // back
            8, 9, 10, 8, 10, 11, // left
            12, 14, 13, 12, 15, 14, // right
            16, 17, 18, 16, 18, 19, // top
            20, 22, 21, 20, 23, 22  // bottom
    };

    private Buffer mVertBuff;
    private Buffer mTexCoordBuff;
    private Buffer mNormBuff;
    private Buffer mIndBuff;


    public MyArrow()
    {
        mVertBuff = fillBuffer(cubeVertices);
        mTexCoordBuff = fillBuffer(cubeTexcoords);
        mNormBuff = fillBuffer(cubeNormals);
        mIndBuff = fillBuffer(cubeIndices);
    }


    @Override
    public Buffer getBuffer(BUFFER_TYPE bufferType)
    {
        Buffer result = null;
        switch (bufferType)
        {
            case BUFFER_TYPE_VERTEX:
                result = mVertBuff;
                break;
            case BUFFER_TYPE_TEXTURE_COORD:
                result = mTexCoordBuff;
                break;
            case BUFFER_TYPE_INDICES:
                result = mIndBuff;
                break;
            case BUFFER_TYPE_NORMALS:
                result = mNormBuff;
            default:
                break;
        }
        return result;
    }


    @Override
    public int getNumObjectVertex()
    {
        return cubeVertices.length / 3;
    }


    @Override
    public int getNumObjectIndex()
    {
        return cubeIndices.length;
    }
}
