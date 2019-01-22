package com.wyx.product;


import com.wyx.product.api.ProductServiceApi;
import com.wyx.product.bean.Product;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: yuxiang
 * @Description:
 * @Date: Create in 2019/1/22
 */
public class Main {


    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true){
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                //读取网络协议
                String apiClassName = objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                Class[] parameterTypes = (Class[])objectInputStream.readObject();
                Object[] argsMethod = (Object[])objectInputStream.readObject();

                Class clazz = null;

                //从API到具体实现的映射关系
                if(apiClassName.equals(ProductServiceApi.class.getName())){
                    clazz = ProductServiceApi.class;
                }

                Method method = clazz.getMethod(methodName, parameterTypes);
                Object invoke = method.invoke(clazz.newInstance(), argsMethod);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(invoke);
                objectOutputStream.flush();

                objectInputStream.close();
                objectOutputStream.close();
                socket.close();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
