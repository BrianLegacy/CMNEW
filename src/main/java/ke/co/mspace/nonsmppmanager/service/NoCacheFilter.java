/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ke.co.mspace.nonsmppmanager.service;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author amos
 */
public class NoCacheFilter  implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response1 = (HttpServletResponse) response;
        
        response1.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response1.setHeader("Pragma", "no-cache");
        response1.setDateHeader("Expires", 0);
        
        chain.doFilter(request, response1);
    }

    @Override
    public void destroy() {
        System.out.println("filter");
    }
    
    
}
