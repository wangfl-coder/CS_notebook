package Proxy.Cglib_Dynamic_Proxy;

public class AliSmsService {

    public String send(String message){
        System.out.println("send message: " +message);
        return message;
    }
}
