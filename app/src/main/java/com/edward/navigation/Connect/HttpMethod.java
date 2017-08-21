package com.edward.navigation.Connect;

import android.os.Build;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class HttpMethod {

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("Get" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            Log.i("1", "1");

            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            Log.i("1", "2");
            if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
                System.setProperty("http.keepAlive", "false");
                Log.i("=","handsome");
            }
                // ����ͨ�õ���������
            conn.setRequestProperty("connection", "close");
            conn.setRequestProperty("Content-Type", "application/json");
            Log.i("1", "3");
            conn.setRequestProperty("Accept-Charset", "utf-8");  
            Log.i("1", "4");

            conn.setRequestProperty("mid", "00105100000001");
            conn.setRequestProperty("Method","POST");
            Log.i("1", "5");
            conn.setDoOutput(true);
            Log.i("1", "6");
            conn.setDoInput(true);
            Log.i("1", "7");
            out = new PrintWriter(conn.getOutputStream());
            Log.i("1", "8");
            out.print(param);
            Log.i("1", "9");
            out.flush();
            Log.i("1", "10");
            //BufferedReader
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            Log.i("1", "11");
            String line;
            Log.i("1", "12");
            while ((line = in.readLine()) != null) {
                result += line;
            }
            Log.i("1", "13");
        } catch (Exception e) {
            System.out.println("post"+e);
            e.printStackTrace();
        }
        //finally
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
    public static void  httpUrlConnPost(String name,String password){
        HttpURLConnection urlConnection = null;
        URL url = null;
        Log.i("fdf","fddgfgfdgfs");
        try {
            url = new URL("http://172.31.102.73/AR/server.php?name=梁宇");
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            JSONObject json = new JSONObject();//创建json对象
            json.put("name", URLEncoder.encode(name, "UTF-8"));//使用URLEncoder.encode对特殊和不可见字符进行编码
            json.put("password", URLEncoder.encode(password, "UTF-8"));//把数据put进json对象中
            String jsonstr = json.toString();//把JSON对象按JSON的编码格式转换为字符串
            //-------------使用字节流发送数据--------------
            //OutputStream out = urlConnection.getOutputStream();
            //BufferedOutputStream bos = new BufferedOutputStream(out);//缓冲字节流包装字节流
            //byte[] bytes = jsonstr.getBytes("UTF-8");//把字符串转化为字节数组
            //bos.write(bytes);//把这个字节数组的数据写入缓冲区中
            //bos.flush();//刷新缓冲区，发送数据
            //out.close();
            //bos.close();
            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){//得到服务端的返回码是否连接成功
                //------------字节流读取服务端返回的数据------------
                //InputStream in = urlConnection.getInputStream();//用输入流接收服务端返回的回应数据
                //BufferedInputStream bis = new BufferedInputStream(in);//高效缓冲流包装它，这里用的是字节流来读取数据的，当然也可以用字符流
                //byte[] b = new byte[1024];
                //int len = -1;
                //StringBuffer buffer = new StringBuffer();//用来接收数据的StringBuffer对象
                //while((len=bis.read(b))!=-1){
                //buffer.append(new String(b, 0, len));//把读取到的字节数组转化为字符串
                //}
                //in.close();
                //bis.close();
                //Log.d("zxy", buffer.toString());//{"json":true}
                //JSONObject rjson = new JSONObject(buffer.toString());//把返回来的json编码格式的字符串数据转化成json对象
                //------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                Log.i("fdf","==s");
                while((str = br.readLine())!=null){//BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = JSONObject.parseObject(buffer.toString());

                Log.i("zxy", "rjson="+rjson);//rjson={"json":true}
                boolean result = rjson.getBoolean("json");//从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数据
               /* if(result){//判断结果是否正确
                    mHandler.sendEmptyMessage(USERLOGIN_SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(USERLOGIN_FAILED);
                }*/
            }else{
                Log.i("fdf","fdfds");
               // mHandler.sendEmptyMessage(USERLOGIN_FAILED);
            }
        } catch (Exception e) {
           Log.e("", e.getStackTrace().toString());
          //  mHandler.sendEmptyMessage(USERLOGIN_FAILED);
        }finally{
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
    }
}
