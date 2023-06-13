package fxmg;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.nio.channels.FileChannel;

public class FileUtil {

  static String encoding = "UTF-8";

  public static String getContent(String filePath){
    return getContent(new File(filePath));
  }

  public static String getContent(String filePath, String encoding){
    return getContent(new File(filePath), encoding);
  }

  public static String getContent(File file){
    return getContent(file, encoding);
  }

  public static String getContent(File file, String encoding){
    try{
        return new String(getBytes(file), encoding);
    }catch(Exception e){
      e.printStackTrace();
    }
    return "";
  }

  public static byte[] getBytes(String filePath){
    return getBytes(new File(filePath));
  }

  public static byte[] getBytes(File file){
    try(FileInputStream fis = new FileInputStream(file);ByteArrayOutputStream baos = new ByteArrayOutputStream();){
      byte[] buffer = new byte[1024];
      int length = 0;
      while((length = fis.read(buffer, 0, buffer.length)) != -1){
        baos.write(buffer, 0, length);
      }
      return baos.toByteArray();
    } catch(Exception e){
      e.printStackTrace();
    }
    return new byte[]{};
  }

  public static void writeContent(File file, String content){
    FileOutputStream fos = null;
    try{
        fos = new FileOutputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = content.getBytes("UTF-8");
        baos.write(bytes, 0, bytes.length);
        baos.writeTo(fos);
        baos.flush();
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      try{
        if(fos != null) fos.close();
      }catch(Exception e){
        e.printStackTrace();
      }
    }
  }

  public static void backup(File file) {
    FileInputStream fis = null;
    FileOutputStream fos = null;
    try{
        fis = new FileInputStream(file);
        fos = new FileOutputStream(new File(file.getName() + ".bak"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while((length = fis.read(buffer, 0, buffer.length)) != -1){
          baos.write(buffer, 0, length);
        }
        fos.write(baos.toByteArray());
        fos.flush();
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      try{
        if(fos != null) fos.close();
        if(fis != null) fis.close();
      }catch(Exception e){
        e.printStackTrace();
      }
    }
  }

}
