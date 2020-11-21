package com.example.photo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtils {
    public static String SAVE_DIR = System.getProperty("user.dir") + "/photo/";
    public static String NO_SUFFIX = ".";
    //保存文件
    public static File save(String dirs,String name, InputStream stream) {
        File file = new File(SAVE_DIR + dirs);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        file = new File(file.getAbsolutePath() + File.separator + name);
        file.deleteOnExit();
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[2048];
            int length = 0;
            while ((length = stream.read(bytes))>= 0) {
                outputStream.write(bytes,0,length);
            }
            outputStream.close();
            stream.close();
            return file;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //删除文件或目录
    public static boolean deleteAnyone(String FileName){

        File file = new File(FileName);//根据指定的文件名创建File对象


        if ( !file.exists() ){  //要删除的文件不存在
            System.out.println("文件"+FileName+"不存在，删除失败！" );
            return false;
        }else{ //要删除的文件存在

            if ( file.isFile() ){ //如果目标文件是文件

                return deleteFile(FileName);

            }else{  //如果目标文件是目录
                return deleteDir(FileName);
            }
        }
    }

    //删除文件
    public static boolean deleteFile(String fileName){


        File file = new File(fileName);//根据指定的文件名创建File对象

        if (  file.exists() && file.isFile() ){ //要删除的文件存在且是文件

            if ( file.delete()){
                System.out.println("文件"+fileName+"删除成功！");
                return true;
            }else{
                System.out.println("文件"+fileName+"删除失败！");
                return false;
            }
        }else{

            System.out.println("文件"+fileName+"不存在，删除失败！" );
            return false;
        }


    }


    //删除目录
    public static boolean deleteDir(String dirName){

        if ( dirName.endsWith(File.separator) )//dirName不以分隔符结尾则自动添加分隔符
            dirName = dirName + File.separator;

        File file = new File(dirName);//根据指定的文件名创建File对象

        if ( !file.exists() || ( !file.isDirectory() ) ){ //目录不存在或者
            System.out.println("目录删除失败"+dirName+"目录不存在！" );
            return false;
        }

        File[] fileArrays = file.listFiles();//列出源文件下所有文件，包括子目录


        for ( int i = 0 ; i < fileArrays.length ; i++ ){//将源文件下的所有文件逐个删除
            deleteAnyone(fileArrays[i].getAbsolutePath());
        }


        if ( file.delete() )//删除当前目录
            System.out.println("目录"+dirName+"删除成功！" );


        return true;

    }

    //获取文件路径
    public static String[] getFilesPath(String dirName) {
        File file = new File(SAVE_DIR + dirName);
        if ( file == null || !file.isDirectory()) {
            return new String[0];
        }
        try {
            String[] fileList = file.list();
            if (fileList != null) {
                for (int index = 0; index < fileList.length ; index ++) {
                    fileList[index] = fileList[index].replace(file.getAbsolutePath(),"");
                }
                return fileList;
            }else {
                return new String[0];
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    //获取文件
    public static File getFile(String path) {
        return new File(FileUtils.SAVE_DIR + "/" + path);
    }

    public static boolean WriteTo(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] bytes = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes,0,length);
            }
            inputStream.close();
            outputStream.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //获取目录
    public static String getSuffix(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
