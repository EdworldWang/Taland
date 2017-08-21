package com.edward.navigation.use;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * This file created by dragon on 2016/7/29 21:58,belong to com.vuforia.samples.SampleApplication.utils .
 */
//这个类是用于Model更换的，方式为读取txt
public class Model extends MeshObject {
    private  static final String TAG="modelTest";

    private Buffer mVertBuff;//顶点
    private Buffer mTexCoordBuff;//纹理坐标
    private Buffer mNormBuff;//normal
    private int verticesNumber = 0;
    private AssetManager assetManager;

    public Model(AssetManager inputassetManager){
        this.assetManager = inputassetManager;
        setVerts();
        setTexCoords();
        setNorms();
    }
    double[] model_VERTS;
    double[] model_TEX_COORDS;
    double[] model_NORMS;

    InputStream inputFile = null;

    private int loadVertsFromModel(String fileName) throws IOException {
        try{
            inputFile = assetManager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile));
            String line = reader.readLine();
            int floatsToRead = Integer.parseInt(line);
            model_VERTS = new double[3*floatsToRead];
            for(int i=0;i<floatsToRead;i++){
                String curline = reader.readLine();
                if(curline.indexOf('/')>=0){
                    i--;
                    continue;
                }
//                将一行分成3个数据
                String floatStrs[] = curline.split(",");
                model_VERTS[3 * i] = Float.parseFloat(floatStrs[0]);
                model_VERTS[3 * i + 1] = Float.parseFloat(floatStrs[1]);
                model_VERTS[3 * i + 2] = Float.parseFloat(floatStrs[2]);

            }
            return floatsToRead;
        } finally {
            if(inputFile !=null){
                inputFile.close();
            }
        }
    }
    private int loadTexCoordsFromModel(String fileName)
            throws IOException
    {
        try
        {
            inputFile = assetManager.open(fileName);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputFile));

            String line = reader.readLine();

            //ex.24168 3 floatsdata in 1 line
            int floatsToRead = Integer.parseInt(line);
            //int floatsToRead = 1000;

            model_TEX_COORDS = new double[2*floatsToRead];


            for (int i = 0; i < floatsToRead; i++)
            {

                String curline = reader.readLine();
                if( curline.indexOf('/') >= 0 ){
                    i--;
                    continue;
                }

                //split 1 line to 2 data
                String floatStrs[] = curline.split(",");

                model_TEX_COORDS[2*i] = Float.parseFloat(floatStrs[0]);
                model_TEX_COORDS[2*i+1] = Float.parseFloat(floatStrs[1]);
//                model_TEX_COORDS[3*i+2] = Float.parseFloat(floatStrs[2]);
            }

            return floatsToRead;


        } finally
        {
            if (inputFile != null)
                inputFile.close();
        }
    }

    private int loadNormsFromModel(String fileName)
            throws IOException
    {
        try
        {
            inputFile = assetManager.open(fileName);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputFile));

            String line = reader.readLine();

            //ex.24168 3 floatsdata in 1 line
            int floatsToRead = Integer.parseInt(line);
            //int floatsToRead = 1000;

            model_NORMS = new double[3*floatsToRead];


            for (int i = 0; i < floatsToRead; i++)
            {

                String curline = reader.readLine();
                if( curline.indexOf('/') >= 0 ){
                    i--;
                    continue;
                }

                //split 1 line to 3 data
                String floatStrs[] = curline.split(",");

                model_NORMS[3*i] = Float.parseFloat(floatStrs[0]);
                model_NORMS[3*i+1] = Float.parseFloat(floatStrs[1]);
                model_NORMS[3*i+2] = Float.parseFloat(floatStrs[2]);
            }

            return floatsToRead;


        } finally
        {
            if (inputFile != null)
                inputFile.close();
        }
    }

    private void setVerts(){
        int num = 0;
        try{
            num = loadVertsFromModel("ImageTargets/Model/verts.txt");

        } catch(IOException e){
            e.printStackTrace();
        }
        mVertBuff = fillBuffer(model_VERTS);
        verticesNumber = num;
    }
    private void setTexCoords()
    {
        int num = 0;
        try {
            num = loadTexCoordsFromModel("ImageTargets/Model/texcoords.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTexCoordBuff = fillBuffer(model_TEX_COORDS);

    }
    private void setNorms()
    {
        int num = 0;
        try {
            num = loadNormsFromModel("ImageTargets/Model/norms.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mNormBuff = fillBuffer(model_NORMS);
    }
    public int getNumObjectIndex()
    {
        return 0;
    }


    @Override
    public int getNumObjectVertex()
    {
        return verticesNumber;
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
            case BUFFER_TYPE_NORMALS:
                result = mNormBuff;
                break;
            default:
                break;

        }

        return result;
    }

}
