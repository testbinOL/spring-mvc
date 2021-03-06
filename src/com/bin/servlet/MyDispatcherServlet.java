package com.bin.servlet;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MyDispatcherServlet extends DispatcherServlet {
    @Override
    protected void initFrameworkServlet() throws ServletException {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        process();
        System.out.println("##################MyDispatcherServlet#####################");
    }

    public static final String WEB_ROOT = "/Users/xingshulin/ownCode/springmvc/out/production/springmvc";
    public static final String JDK_ROOT = "/Library/Java/JavaVirtualMachines/jdk1.8.0_161.jdk/Contents/Home/jre/lib/";
    public static final String J2EE_API = "/Users/xingshulin/Tools/apache-tomcat-8.5.30/lib";

    private void process() {
        String uri = "servlet/TestServlet";
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        System.out.println("扩展目录:" + getExtDirs().toString());
        try {
            // create a URLClassLoader
            URL[] urls = new URL[3];
            URLStreamHandler streamHandler = null;
            File classPath = new File(WEB_ROOT);
            // the forming of repository is taken from the createClassLoader method in
            // org.apache.catalina.startup.ClassLoaderFactory
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            String jdkRepo = (new URL("file", null, new File(JDK_ROOT).getCanonicalPath() + File.separator)).toString();
            String j2eeRepo = (new URL("file", null, new File(J2EE_API).getCanonicalPath() + File.separator)).toString();
            System.out.println("#######repository#########:" + repository + "############JDK#########" + jdkRepo + "##########J2EE##########" + j2eeRepo);
            // the code for forming the URL is taken from the addRepository method in
            // org.apache.catalina.loader.StandardClassLoader class.
            urls[0] = new URL(null, repository, streamHandler);
            urls[1] = new URL(null, jdkRepo, streamHandler);
            urls[2] = new URL(null, j2eeRepo, streamHandler);
            //loader = new URLClassLoader(urls);
            loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
            System.out.println(Thread.currentThread().getContextClassLoader().toString());
            System.out.println("URLS:" + Arrays.asList(((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs()));
            System.out.println("new URLS:" + Arrays.asList(loader.getURLs()));
        } catch (IOException e) {
            System.out.println("#########Exception##########:" + e.toString());
        }
        Class myClass = null;
        try {
            myClass = loader.loadClass("com.bin.servlet." + servletName);
            TestServlet servlet = (TestServlet) myClass.newInstance();
            servlet.getServletConfig();
        } catch (ClassNotFoundException e) {
            System.out.println("############loadclass-Exception############:" + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File[] getExtDirs() {
        //加载<JAVA_HOME>/lib/ext目录中的类库
        String s = System.getProperty("java.ext.dirs");
        File[] dirs;
        if (s != null) {
            StringTokenizer st =
                    new StringTokenizer(s, File.pathSeparator);
            int count = st.countTokens();
            dirs = new File[count];
            for (int i = 0; i < count; i++) {
                dirs[i] = new File(st.nextToken());
            }
        } else {
            dirs = new File[0];
        }
        return dirs;
    }


    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("线程上线文:" + Thread.currentThread().getContextClassLoader());
    }
}
