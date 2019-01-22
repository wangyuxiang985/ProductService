package com.wyx.order.service;

import com.wyx.product.api.ProductServiceApi;
import com.wyx.product.bean.Product;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @Author: yuxiang
 * @Description:
 * @Date: Create in 2019/1/22
 * 订单系统-调用商品系统API的方法
 */
public class Main {
    public static void main(String[] args) {

        ProductServiceApi serviceApi = null;

        Object rpc = rpc(ProductServiceApi.class);
        if(rpc instanceof ProductServiceApi){
            serviceApi = (ProductServiceApi)rpc;
        }
        if (serviceApi != null){
            Product product = serviceApi.queryProductById(123L);
            System.out.println(product);
        }

    }


    public static Object rpc(final Class clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket("127.0.0.1",8888);
                //我们想远程调用哪个类的哪个方法，并传递给这个方法什么参数
                //注意：我们只知道Product API ,并不知道Product API在Product的实现
                String apiClazzName = clazz.getName();
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeUTF(apiClazzName);
                objectOutputStream.writeUTF(methodName);
                objectOutputStream.writeObject(parameterTypes);
                objectOutputStream.writeObject(args);
                objectOutputStream.flush();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object readObject = objectInputStream.readObject();
                //关闭流
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();

                return readObject;
            }
        });
    }
}
