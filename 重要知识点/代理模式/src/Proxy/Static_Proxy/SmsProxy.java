package Proxy.Static_Proxy;

public class SmsProxy implements SmsService{
    //被代理对象
    private final SmsService smsService;

    public SmsProxy(SmsService smsService){
        this.smsService = smsService;
    }

    @Override
    public String send(String message) {
        //调用方法前定义自己的操作
        System.out.println("before method send()");
        smsService.send(message);
        //调用方法后定义自己的操作
        System.out.println("after method send()");
        return null;
    }
}
