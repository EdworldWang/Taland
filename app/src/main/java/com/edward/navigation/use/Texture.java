/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.edward.navigation.use;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;


// Support class for the Vuforia samples applications.
// Exposes functionality for loading a texture from the APK.
public class Texture
{
    private static final String LOGTAG = "Texture";
    
    public int mWidth;          // The width of the texture.
    public int mHeight;         // The height of the texture.
    public int mChannels;       // The number of channels.
    public ByteBuffer mData;    // The pixel data.
    public int[] mTextureID = new int[1];
    public boolean mSuccess = false;
    public static int Texturenum=0;
    public static HashMap TextureMap=new HashMap();
    /* Factory function to load a texture from the APK. */
    public static Texture loadTextureFromApk(String fileName,
                                             AssetManager assets, String TextureName)
    {
        InputStream inputStream = null;
        try
        {
            inputStream = assets.open(fileName, AssetManager.ACCESS_BUFFER);

            BufferedInputStream bufferedStream = new BufferedInputStream(
                inputStream);
            Bitmap bitMap = BitmapFactory.decodeStream(bufferedStream);
            
            int[] data = new int[bitMap.getWidth() * bitMap.getHeight()];

            //DATA的長度爲像素點衚的個數
                       bitMap.getPixels(data, 0, bitMap.getWidth(), 0, 0,
                bitMap.getWidth(), bitMap.getHeight());
          //  public void getPixels(int[] pixels, int offset, int stride,int x, int y, int width, int height)
/*
            获取原Bitmap的像素值存储到pixels数组中。
            参数：
            pixels     接收位图颜色值的数组
            offset     写入到pixels[]中的第一个像素索引值
            stride     pixels[]中的行间距个数值(必须大于等于位图宽度)。不能为负数
            x          从位图中读取的第一个像素的x坐标值。
            y          从位图中读取的第一个像素的y坐标值
            width      从每一行中读取的像素宽度
            height 读取的行数*/


            bitMap.getPixels(data, 0, bitMap.getWidth(), 0, 0,
                bitMap.getWidth(), bitMap.getHeight());
            

            return loadTextureFromIntBuffer(data, bitMap.getWidth(),
                bitMap.getHeight(),TextureName);
        } catch (IOException e)
        {
            Log.e(LOGTAG, "Failed to log texture '" + fileName + "' from APK");
            Log.i(LOGTAG, e.getMessage());
            return null;
        }
    }

    public static Texture loadTextureFromView(View v, String TextureName){
        Bitmap bitMap=createViewBitmap(v);
        int[] data = new int[bitMap.getWidth() * bitMap.getHeight()];

        //DATA的長度爲像素點衚的個數
        bitMap.getPixels(data, 0, bitMap.getWidth(), 0, 0,
                bitMap.getWidth(), bitMap.getHeight());
        //  public void getPixels(int[] pixels, int offset, int stride,int x, int y, int width, int height)
/*
            获取原Bitmap的像素值存储到pixels数组中。
            参数：
            pixels     接收位图颜色值的数组
            offset     写入到pixels[]中的第一个像素索引值
            stride     pixels[]中的行间距个数值(必须大于等于位图宽度)。不能为负数
            x          从位图中读取的第一个像素的x坐标值。
            y          从位图中读取的第一个像素的y坐标值
            width      从每一行中读取的像素宽度
            height 读取的行数*/


        bitMap.getPixels(data, 0, bitMap.getWidth(), 0, 0,
                bitMap.getWidth(), bitMap.getHeight());


        return loadTextureFromIntBuffer(data, bitMap.getWidth(),
                bitMap.getHeight(),TextureName);
    }

    public static Bitmap createViewBitmap(View v) {
        v.setDrawingCacheEnabled(true);
       v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.buildDrawingCache();
        v.layout(0, 0, v.getMeasuredWidth(),
                v.getMeasuredHeight());
       // v.setVisibility(View.INVISIBLE);
        Bitmap bitmap = v.getDrawingCache();
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap);
        saveBmp(bitmap2,"opengl"+".png");
        return bitmap2;
    }

    private static void saveBmp(Bitmap bmp, String filename) {
        try {
            String contentpath = Environment.getExternalStorageDirectory() + "/" +"ARGO";
            Log.i(LOGTAG,"path="+contentpath);
            File contentfile = new File(contentpath);
            if (!contentfile.exists()||!contentfile.isDirectory()){
                try{
                    contentfile.mkdir();
                    Log.i(LOGTAG, "make dir");
                }catch (Exception e) {
                    Log.e(LOGTAG, "MakeDirException"+e.toString());
                }
            }else{
                Log.i(LOGTAG,"目录已经存在");
            }
            String picturepath=contentpath+"/"+filename;
            Log.i(LOGTAG,"picturepath="+picturepath);
            File picturefile = new File(picturepath);
            picturefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(picturefile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.i(LOGTAG,e.getStackTrace().toString());
            Log.i(LOGTAG,"Exception"+e.toString());
        }
    }
    
    
    public static Texture loadTextureFromIntBuffer(int[] data, int width,
        int height,String TextureName)
    {
        // Convert:
        int numPixels = width * height;
        byte[] dataBytes = new byte[numPixels * 4];

        for (int p = 0; p < numPixels; ++p)
        {
            int colour = data[p];
            dataBytes[p * 4] = (byte) (colour >>> 16); // R
            dataBytes[p * 4 + 1] = (byte) (colour >>> 8); // G
            dataBytes[p * 4 + 2] = (byte) colour; // B
            dataBytes[p * 4 + 3] = (byte) (colour >>> 24); // A
        }
        
        Texture texture = new Texture();
        texture.mWidth = width;
        texture.mHeight = height;
        texture.mChannels = 4;
        
        texture.mData = ByteBuffer.allocateDirect(dataBytes.length).order(
            ByteOrder.nativeOrder());
        int rowSize = texture.mWidth * texture.mChannels;
        for (int r = 0; r < texture.mHeight; r++)


        texture.mData.put(dataBytes, rowSize * (texture.mHeight - 1 - r),
                rowSize);


        texture.mData.rewind();
        
        // Cleans variables
        dataBytes = null;
        data = null;
        
        texture.mSuccess = true;
        if (TextureMap.get(TextureName)==null) {
            TextureMap.put(TextureName, Texturenum);
            Texturenum++;
        }
        return texture;
    }
}
