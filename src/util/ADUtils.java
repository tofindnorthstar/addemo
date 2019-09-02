package util;

import model.AdDepartment;
import model.User;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.*;
/**
 * @ClassName ADUtils
 * @Description: TODO
 * @Author jack
 * @Date 2019/8/31
 * @Version V1.0
 */
public class ADUtils {
    // 连接ad域
    public static LdapContext getContext(Hashtable<String, String> hashtable) throws NamingException {
        LdapContext ctx = new InitialLdapContext(hashtable, null);
        return ctx;
    }


    public static NamingEnumeration<SearchResult> getSearchResult(LdapContext ctx,String searchFilter,String searchBase) throws NamingException {
        //搜索控制器
        SearchControls searchCtls = new SearchControls();
        //创建搜索控制器
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String returnedAtts[] = {"canonicalName", "distinguishedName", "id",
                "name", "userPrincipalName", "departmentNumber", "telephoneNumber", "homePhone",
                "mobile", "department", "sAMAccountName", "whenChanged"}; // 定制返回属性
        searchCtls.setReturningAttributes(returnedAtts);
        NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
        return answer;
    }


    public static List<User> getUsers(LdapContext ctx) throws NamingException {
        //LDAP搜索过滤器类，此处只获取AD域用户，所以条件为用户user或者person均可
        String searchFilter = "(objectClass=user)";
        //AD域节点结构
        String searchBase = "DC=jack,DC=com";
        NamingEnumeration<SearchResult> answer = getSearchResult(ctx, searchFilter, searchBase);
        List<User> users  = new ArrayList<User>();
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.next();
            User u = new User();
            u.setcName(getAttrValue(sr,"canonicalName"));
            u.setUserName(getAttrValue(sr,"sAMAccountName"));
            users.add(u);
        }
        return users;
    }
    // 获取部门列表
    public static TreeSet<AdDepartment> getAdDepartment(LdapContext ctx) throws NamingException {
        //LDAP搜索过滤器类，此处只获取AD域用户，所以条件为用户user或者person均可
        String searchFilter = "(ou>='')";
        //AD域节点结构
        String searchBase = "DC=jack,DC=com";
        NamingEnumeration<SearchResult> answer = getSearchResult(ctx, searchFilter, searchBase);
        List<AdDepartment> adDepartments = new ArrayList<AdDepartment>();
        TreeSet<AdDepartment> treeSet = new TreeSet<AdDepartment>();
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.next();
            AdDepartment adDepartment = new AdDepartment();
            adDepartment.setName(getAttrValue(sr, "name"));
            adDepartment.setcName(getAttrValue(sr, "canonicalName"));
            adDepartment.setDistinguishedName(getAttrValue(sr, "distinguishedName"));
            treeSet.add(adDepartment);
        }
        return treeSet;
    }

    public static AdDepartment getTreeAdDepartment(TreeSet<AdDepartment> treeSet) {
        AdDepartment root = new AdDepartment();
        root.setName("jack.com");
        root.setcName("jack.com");
        for (AdDepartment ad : treeSet) {
            AdDepartment parentAdDepartment = null;
            if ((parentAdDepartment = root.getParentAdDepartmentBycName(ad.getcName())) != null) {
                parentAdDepartment.addChildren(ad);
            } else {
                root.addChildren(ad);
            }
        }
        return root;
    }

    private static String getAttrValue(SearchResult sr, String attr) throws NamingException {
        Attributes Attrs = sr.getAttributes();
        if (Attrs.get(attr) == null) {
            return null;
        }
        return Attrs.get(attr).getAll().next().toString();
    }

}