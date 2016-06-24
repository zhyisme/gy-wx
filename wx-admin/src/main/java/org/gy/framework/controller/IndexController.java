package org.gy.framework.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    public static final String DEFAULT_LOGIN = "sample/login.ftl";

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView toLogin() {
        ModelAndView mav = new ModelAndView(DEFAULT_LOGIN);
        return mav;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView login(String username,
                              String password) {

        ModelAndView mav = new ModelAndView(DEFAULT_LOGIN);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            mav.addObject("error", "用户名或密码不能为空！");
            return mav;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        // 获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            mav.addObject("error", "用户名不存在");
        } catch (IncorrectCredentialsException ice) {
            // request.setAttribute("error", "密码不正确");
            mav.addObject("error", "用户名或密码不正确");
        } catch (LockedAccountException lae) {
            mav.addObject("error", "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            mav.addObject("error", "账号：" + username + " 登录失败次数过多,锁定10分钟!");
        } catch (AuthenticationException ae) {
            // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            mav.addObject("error", "用户名或密码不正确");
        }
        // 验证是否登录成功
        if (currentUser.isAuthenticated()) {
            // 这里可以进行一些认证通过后的一些系统参数初始化操作
            mav.setViewName(InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/index.html");
            return mav;
        } else {
            token.clear();
        }
        return mav;
    }

    /**
     * 用户登出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView logout() {
        // 使用权限管理工具进行用户的退出，注销登录
        SecurityUtils.getSubject().logout();
        return new ModelAndView(InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/login.htm");
    }

}
