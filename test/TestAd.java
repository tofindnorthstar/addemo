import model.AdDepartment;
import model.User;
import net.sf.json.JSONObject;
import org.junit.Test;
import util.ADUtils;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;
/**
 * @ClassName TestAd
 * @Description: TODO
 * @Author jack
 * @Date 2019/8/31
 * @Version V1.0
 */
public class TestAd {

    public static Hashtable<String, String> env = new Hashtable<String, String>();
    static{
        String adminName = "zhangsan@jack.com";//username
        String adminPassword = "Password123!";//password
        String ldapURL = "LDAP://10.11.42.13:389";//ip:port
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别："none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, adminName);// AD User
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);// AD Password
        env.put(Context.PROVIDER_URL, ldapURL);// LDAP工厂类
        env.put("com.sun.jndi.ldap.connect.timeout", "3000");//连接超时设置为3秒
    }

    // 获取ad用户
    @Test
    public void getAdUsers(){
        LdapContext ctx;
        try {
            ctx = ADUtils.getContext(env);
            List<User> users = ADUtils.getUsers(ctx);
            users.stream().forEach(u->{
                System.out.println(u.toString());
            });
            if (ctx != null) {
                ctx.close();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    // 获取ad部门列表
    @Test
    public void getAdDepartments(){
        LdapContext ctx;
        try {
            ctx = ADUtils.getContext(env);
            TreeSet<AdDepartment> as = ADUtils.getAdDepartment(ctx);
            as.stream().forEach(adDepartment->{
                System.out.println(adDepartment.toString());
            });
            if (ctx != null) {
                ctx.close();
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    // 获取ad部门树结构
    @Test
    public void getTreeAdDepartment(){
        LdapContext ctx;
        try {
            ctx = ADUtils.getContext(env);
            TreeSet<AdDepartment> as = ADUtils.getAdDepartment(ctx);
            System.out.println(ADUtils.getTreeAdDepartment(as));
            if (ctx != null) {
                ctx.close();
            }
            JSONObject jsonObject = JSONObject.fromObject(ADUtils.getTreeAdDepartment(as));
            System.out.println(jsonObject.toString());
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
