package edu.nju.yummy.service;

import edu.nju.yummy.enums.ResultMessage;
import edu.nju.yummy.service.Impl.FoodServiceImpl;
import edu.nju.yummy.util.ThreadConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoodServiceTest {

    //    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ThreadConfig.class);
//        FoodServiceImpl foodService = context.getBean(FoodServiceImpl.class);
//        for (int i = 0; i < 20; i++) {
//            foodService.consumeFood(1, 1, i);
//        }
//        //最后可以根据结果可以看出结果是并发执行而不是顺序执行的呢
//        context.close();
//    }

    @Autowired
    private FoodService foodService;


    @Test
    public void consumeFood() {

//        long fid = 1;
//        int number = 1;
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 5; i++) {
//            ResultMessage message = foodService.consumeFood(fid, number, i);
//
////            System.out.println(message+" "+i);
//        }
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("Execute time:" + (endTime - startTime));
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


}