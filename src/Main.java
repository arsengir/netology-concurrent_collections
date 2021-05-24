import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int MAX_CALL = 60;
        final int CALL_DELAY = 1000;
        final int DELAY_OPERATOR = 5000;
        final int TALK = 4000;


        ConcurrentLinkedQueue<String> queueCall = new ConcurrentLinkedQueue<>();

        Runnable call = () -> {
            for (int i = 0; i < MAX_CALL; i++) {
                System.out.println("Поступил звонок " + i);
                queueCall.add("Звонок " + i);
                try {
                    Thread.sleep(CALL_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable handlingCall = () -> {
            String str;
            while (true) {
                if ((str = queueCall.poll()) != null) {
                    System.out.println(Thread.currentThread().getName() + " взял: " + str);
                    try {
                        Thread.sleep(TALK);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (queueCall.size() == 0){
                    try {
                        Thread.sleep(DELAY_OPERATOR);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if  (queueCall.size() == 0){
                        System.out.println(Thread.currentThread().getName() + "закончил обработку звнков");
                        break;
                    }
                }
            }
        };

        Thread threadATC = new Thread(null, call, "ATC");
        threadATC.start();

        Thread operator1 = new Thread(null, handlingCall, "Оператор 1");
        Thread operator2 = new Thread(null, handlingCall, "Оператор 2");
        Thread operator3 = new Thread(null, handlingCall, "Оператор 3");
        operator1.start();
        operator2.start();
        operator3.start();

        threadATC.join();
        operator1.join();
        operator2.join();
        operator3.join();

    }
}
