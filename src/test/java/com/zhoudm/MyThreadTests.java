package com.zhoudm;


/**
 * Created by Administrator on 2017/8/11.
 */
class MyThreads extends Thread {
    private int tid;

    public MyThreads(int tid){
        this.tid = tid;
    }

    @Override
    public void run(){
        try{
            for(int i=0;i<10;++i){
                Thread.sleep(1000);
                System.out.println(String.format("%d : %d" ,tid, i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Service{
    private String ticketName;
    private int totalCount;
    private int remaining;

    public Service(String ticketName , int totalCount){
        this.ticketName=ticketName;
        this.totalCount=totalCount;
        this.remaining=totalCount;
    }

    public synchronized int saleTicket(int ticketNum){
        if(remaining>0){
            remaining-=ticketNum;
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            if(remaining>=0){
                return remaining;
            }else{
                remaining+=ticketNum;
                return -1;
            }

        }
        return -1;
    }

    public synchronized int getRemaining(){
        return remaining;
    }

    public String getTicketName(){
        return  this.ticketName;
    }
}

class TicketSaler implements Runnable{
    private String name;
    private Service service;

    public TicketSaler(String windowName , Service service){
        this.name=windowName;
        this.service=service;
    }

    @Override
    public void run(){
        while(service.getRemaining()>0){
            synchronized (this){
                System.out.println(Thread.currentThread().getName()+"出售第"+service.getRemaining()+"张票，");
                int remaining =service.saleTicket(1);
                if(remaining>=0){
                    System.out.println("出票成功！剩余"+remaining+"张票。");
                }else {
                    System.out.println("出票失败！ 该票已售完 。");
                }
            }
        }
    }
}

public class MyThreadTests{
    public static void main(String [] args){
        //testThread();
        Service service=new Service("广州 --> 长沙",500);
        TicketSaler ticketSaler=new TicketSaler("售票系统" , service);
        Thread threads []=new Thread[8];
        for(int i=0;i<threads.length;i++){
            threads[i]=new Thread(ticketSaler,"窗口"+(i+1));
            System.out.println("窗口"+(i+1)+"开始出售"+service.getTicketName()+"的票...");
            threads[i].start();
        }
    }
}
