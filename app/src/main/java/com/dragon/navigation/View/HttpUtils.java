package com.dragon.navigation.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {
    /*
     * Function  :   ����Post���󵽷�����
     * Param     :   params���������ݣ�encode�����ʽ
     */
    public static String submitPostData(String strUrlPath,Map<String, String> params, String encode) {
        
        byte[] data = getRequestData(params, encode).toString().getBytes();//���������
        try {            
            
            //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php"; 
            URL url = new URL(strUrlPath);  
             
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //�������ӳ�ʱʱ��
            httpURLConnection.setDoInput(true);                  //�����������Ա�ӷ�������ȡ����
            httpURLConnection.setDoOutput(true);                 //����������Ա���������ύ����
            httpURLConnection.setRequestMethod("POST");     //������Post��ʽ�ύ����
            httpURLConnection.setUseCaches(false);               //ʹ��Post��ʽ����ʹ�û���
            //������������������ı�����
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //����������ĳ���
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //�����������������д������
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            
            int response = httpURLConnection.getResponseCode();            //��÷���������Ӧ��
            if(response == HttpURLConnection.HTTP_OK) {  //200
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //�������������Ӧ���
            }
        } catch (IOException e) {
            //e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }
    
    /*
     * Function  :   ��װ��������Ϣ
     * Param     :   params���������ݣ�encode�����ʽ
     */
   public static StringBuffer getRequestData(Map<String, String> params, String encode) {
      StringBuffer stringBuffer = new StringBuffer();        //�洢��װ�õ���������Ϣ
      try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                      .append("=")
                      .append(URLEncoder.encode(entry.getValue(), encode))
                      .append("&");
            }
           stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //ɾ������һ��"&"
        } catch (Exception e) {
           e.printStackTrace();
       }
       return stringBuffer;
    }
    
   /*
    * Function  :   �������������Ӧ�������������ת�����ַ�����
    * Param     :   inputStream����������Ӧ������
    */
   public static String dealResponseResult(InputStream inputStream) {
      String resultData = null;      //�洢������
       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] data = new byte[1024];
      int len = 0;
       try {
          while((len = inputStream.read(data)) != -1) {
             byteArrayOutputStream.write(data, 0, len);
          }
     } catch (IOException e) {
         e.printStackTrace();
        }
       resultData = new String(byteArrayOutputStream.toByteArray());    
       return resultData;
   }    
    
   
}