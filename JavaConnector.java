package fxmg;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;
import java.security.*;

public class JavaConnector {

    WebEngine webEngine;

    public JavaConnector(WebEngine webEngine){
        this.webEngine = webEngine;
    }

    String basePath = "D:/test_env/apache-tomcat-7.0.91_ssl/webapps/ROOT/uploadFile/";

    String success = "success";

    String fail = "fail";

    public void reload(){
        String basePath = "F:/workspace/fxmg/resource/";
        String index = "index.html";
        String content = FileUtil.getContent(basePath + index);
        webEngine.loadContent(content);
    }

    public void rename(String name){
        JSObject jsConnector = (JSObject)webEngine.executeScript("getJsConnector()");
        String format = "<span class=\"%s\">%s</span>";
        String target = "1";
        File file = new File(basePath, target);
        File reName = new File(basePath, name);
        if(file.isDirectory()){
            boolean result = file.renameTo(reName);
            String message = "";
            if (result) {
                message = String.format(format, success, name);
            } else {
                message = String.format(format, fail, name);
            }
            jsConnector.call("renameResult", message);
        } else {
            jsConnector.call("renameResult", String.format(format, fail, "File Not Exists."));
        }
    }

    public void setSysClipboardText(String text){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(text);
        clipboard.setContents(tText, null);
    }

    public void fxgenerate(String aid, String max, boolean split){
        this.aid = aid;
        this.max = max;
        /*
        int mx = Integer.parseInt(max);
        StringBuffer strb = new StringBuffer();
        strb.append("[");
        String serId;
        for(int i = 1; i <= mx; i++){
            serId = String.valueOf(100000 + i).substring(1);
            strb.append("{0:" + stubJsonString(serId));
            strb.append(",1:" + stubJsonString(serId + ext));
            strb.append(",2:" + stubJsonString(numGen(aid, serId)));
            if(i == mx) strb.append("}");
            else strb.append("},");
        }
        strb.append("]");

        StringBuffer strb2 = new StringBuffer();
        strb2.append("var index = 0;\n");
        strb2.append("var aid = \"" + aid + "\";\n");
        strb2.append("var arrys = " + strb.toString() + "\n");
        strb2.append("loadImage();");
        */
        StringBuffer strb2 = new StringBuffer();
        run(strb2, split);

        JSObject jsConnector = (JSObject)webEngine.executeScript("getJsConnector()");
        jsConnector.call("showResult", strb2.toString());
    }

    public static void run(StringBuffer strb, boolean split){
        if(split) {
            strb.append("//" + Integer.parseInt(max) / 2);
            strb.append("\n");
            strb.append("var index = 0;");
            strb.append("\n");
            strb.append("var aid = \"" + aid + "\";");
            strb.append("\n");
            strb.append("var arrys = " + run2(1));
            strb.append("\n");
            strb.append("loadImage();");
            strb.append("\n");

            strb.append("@@@");

            strb.append("//" + max);
            strb.append("\n");
            strb.append("var index = 0;");
            strb.append("\n");
            strb.append("var aid = \"" + aid + "\";");
            strb.append("\n");
            strb.append("var arrys = " + run2(2));
            strb.append("\n");
            strb.append("loadImage();");
        } else {
            strb.append("//" + max);
            strb.append("\n");
            strb.append("var index = 0;");
            strb.append("\n");
            strb.append("var aid = \"" + aid + "\";");
            strb.append("\n");
            strb.append("var arrys = " + run2(0));
            strb.append("\n");
            strb.append("loadImage();");
            strb.append("\n");
        }
    }

    public static String run2(int split){
        int mx = Integer.parseInt(max);
        int i = 1;
        if(split == 1){
            mx = mx / 2;
        }
        if(split == 2){
            i = mx / 2;
            i += 1;
        }
        StringBuffer strb = new StringBuffer();
        strb.append("[");
        String serId;
        for(; i <= mx; i++){
            serId = String.valueOf(100000 + i).substring(1);
            strb.append("{0:" + stubJsonString(serId));
            strb.append(",1:" + stubJsonString(serId + ext));
            strb.append(",2:" + stubJsonString(numGen(aid, serId)));
            if(i == mx) strb.append("}");
            else strb.append("},");
        }
        strb.append("]");
        return strb.toString();
    }

    public static int baseline = 100000;

    public static String aid;

    public static String max;

    public static String ext = ".webp";

    public static void init() throws Exception {
        String datas = new String(getBytes(), "UTF-8");
        String[] lines = datas.split("\r\n");
        if(null == lines)
            throw new RuntimeException("echo aid max > clink.txt");
        aid = lines[0].split(" ")[0];
        max = lines[0].split(" ")[1];
    }

    public static String stubJsonString(String input){
        return String.format("'%s'", input);
    }

    public static String numGen(String aid, String serId){
        return numGen_V2(aid, serId);
    }

    public static String numGen_V2(String aid, String serId) {
        if(Integer.parseInt(aid) < 268850)
            return "10";
        int iAid = Integer.parseInt(aid);
        int max = 268850;
        int min = 421925;
        int other = 421926;
        String md5sum = md5sum(aid + serId);
        int source = Character.codePointAt(md5sum, md5sum.length() - 1);
        int num = (iAid >= max && iAid <= min)
                ? source %= 10
                : iAid > other ? source %= 8 : source;
        switch (num) {
            case 0:
                num = 2;
                break;
            case 1:
                num = 4;
                break;
            case 2:
                num = 6;
                break;
            case 3:
                num = 8;
                break;
            case 4:
                num = 10;
                break;
            case 5:
                num = 12;
                break;
            case 6:
                num = 14;
                break;
            case 7:
                num = 16;
                break;
            case 8:
                num = 18;
                break;
            case 9:
                num = 20;
                break;
        }
        return num+"";
    }

    public static String numGen_V1(String aid, String serId) {
        if(Integer.parseInt(aid) < 268850)
            return "10";
        String md5sum = md5sum(aid + serId);
        int source = Character.codePointAt(md5sum, md5sum.length() - 1);
        int num = source %= 10;
        switch (num) {
            case 0:
                num = 2;
                break;
            case 1:
                num = 4;
                break;
            case 2:
                num = 6;
                break;
            case 3:
                num = 8;
                break;
            case 4:
                num = 10;
                break;
            case 5:
                num = 12;
                break;
            case 6:
                num = 14;
                break;
            case 7:
                num = 16;
                break;
            case 8:
                num = 18;
                break;
            case 9:
                num = 20;
                break;
        }
        return num+"";
    }

    /**
     * @param param MD5 加密的字符串
     * @return String MD5Hash 值
     * @throws
     * @Description: 加密指定的字符串并返回加密后的 MD5Hash 值（全小写）
     * @author
     * @date 2018/4/26 15:08
     */
    public static String md5sum(String param) {
        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(param.getBytes());
            byte[] digest = alg.digest();
            StringBuffer strb = new StringBuffer();
            String str = "";
            for (int i = 0; i < digest.length; ++i) {
                str = Integer.toHexString(digest[i] & 0xFF);
                if (str.length() == 1)
                    strb.append("0");
                strb.append(str);
            }
            return strb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getBytes(){
        return getBytes("clink.txt");
    }

    public static byte[] getBytes(String path){
        if(Objects.isNull(path)) return getBytes();
        try {
            File file = new File(path);
            if(!file.exists()) file.createNewFile();
            try(FileInputStream fis = new FileInputStream(file)){
                int len = -1;
                byte[] buf = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while( (len = fis.read(buf, 0, buf.length)) != -1){
                    baos.write(buf, 0, len);
                }
                return baos.toByteArray();
            } catch (IOException e){
                throw e;
            }
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}
