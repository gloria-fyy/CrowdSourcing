package com.sysu.crowdsourcing.controllers;

import com.sysu.crowdsourcing.entity.UserEntity;
import com.sysu.crowdsourcing.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by zhengshouzi on 2015/9/7.
 */
@Controller
public class LoginController {
    @Resource(name = "loginService")
    LoginService loginService;

    @RequestMapping("/login.do")
    public ModelAndView login(@ModelAttribute UserEntity userEntity, HttpSession httpSession) {

        System.out.println("-------login---------");
        ModelAndView modelAndView = new ModelAndView();

        boolean b = loginService.login(userEntity);
        if (b == true) {
            userEntity = loginService.getUserByEmail(userEntity.getEmail());
            httpSession.setAttribute("user", userEntity);
            modelAndView.setViewName("redirect:/Home.do");
        } else {
            modelAndView.addObject("loginError", "�û��������������");
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }


}
