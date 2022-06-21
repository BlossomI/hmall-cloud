package cn.itcast.hmall.dto.common;

/**
 * 用于存储请求头中的userId信息
 */
public class ThreadLocalUtil {
    private static final ThreadLocal<Long> local = new ThreadLocal<>();
    /**
     * 从当前线程中存储userId
     * @param userId
     */
    public static void  setUserId(Long userId){
        local.set(userId);
    }

    /**
     * 从当前线程中获取userId
     * @return
     */
    public static Long getUserId(){
        return local.get();
    }

    /**
     * 从当前线程中清除userId
     */
    public static void clear(){
        local.remove();
    }
//    public static void main(String[] args) {
//        // 密码原文 123123
//        // md5加密后 4297f44b13955235245b2497399d7a93
//        String passwordMd5 = DigestUtils.md5DigestAsHex("123123".getBytes());
//        System.out.println(passwordMd5);
//    }
}