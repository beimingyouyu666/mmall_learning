package com.mmall.Filter;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户是否登陆的过滤器
 */
public class LoginFilter implements Filter {

    //存放要过滤的请求  request.getServletPath()  -- /user/login.do
    static Set<String> noFilter = new HashSet<>();
    static {
        noFilter.add("/user/login.do");
        noFilter.add("/user/register.do");
        noFilter.add("/user/check_valid.do");
        noFilter.add("/user/forget_get_question.do");
        noFilter.add("/user/forget_check_answer.do");
        noFilter.add("/user/forget_reset_password.do");
    }

    Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (noFilter.contains(request.getServletPath())){
            logger.info(request.getServletPath()+"不需要用户登录");
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
            if (session.getAttribute(Const.CURRENT_USER) == null){
                logger.info(request.getServletPath()+"需要用户登陆");
                response.sendRedirect("index.jsp");
            }else {
                logger.info("当前用户为:"+currentUser.getUsername());
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
