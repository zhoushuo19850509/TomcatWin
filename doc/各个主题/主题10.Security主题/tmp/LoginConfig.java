package com.nbcb.mytomcat.catalina.deploy;

/**
 * 所谓的LoginConfig,其实就是一个容器,
 * 这个容器保存了两个内容:
 * 1.realm具体实现类的名称
 * 2.Authenticator具体实现类的名称
 *
 * 一般是在tomcat初始化的时候,
 * 从配置文件读取realm/Authenticator的配置信息,
 * 然后保存到LoginConfig
 */
public class LoginConfig {

    /**
     * 这个就是我们在security中讨论的authenticator接口的实现类
     * 我们自己实现了BasicAuthenticator,那么在初始化的时候，就把authMethod设置为BASIC
     * 当然还有其他实现类。具体如下：
     *
     * The authentication method to use for application login.  Must be
     * BASIC, DIGEST, FORM, or CLIENT-CERT.
     */
    private String authMethod;


    /**
     * 这个就是我们在security中讨论的Realm接口的实现类
     * 我们自己实现了SimpleRealm,那么在初始化的时候，就把realName设置为SimpleRealm
     * 当然还有其他实现类比如：
     * DataSourceRealm
     * MemoryRealm
     * 等等
     */
    private String realName;

    public LoginConfig(String authMethod, String realName) {
        this.authMethod = authMethod;
        this.realName = realName;
    }

    public String getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }


}
