package myssm.myspringmvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ViewBaseServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() {

        // 1. 获取 Servlet 对象
        ServletContext servletContext = this.getServletContext();

        // 2. 创建 Thymeleaf 解析器对象
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);


    }
}
