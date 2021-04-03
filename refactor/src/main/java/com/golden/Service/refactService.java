//package com.golden.Service;
//
//import com.golden.Main;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.IOException;
//
///**
// * @author HuangYihong
// * @date 2021-03-25 23:06
// */
//
//@EnableAutoConfiguration
//@Controller
//public class refactService  {
//
//    @RequestMapping("/refact")
//    @ResponseBody
//    public String serve(String projectName,Boolean on,Double rate) throws IOException {
//        if (projectName==null || on==null || rate==null){
//            return "lack parameters";
//        }
//        Main.start(projectName,on,rate);
//        return "ok success!!!";
//    }
//}
