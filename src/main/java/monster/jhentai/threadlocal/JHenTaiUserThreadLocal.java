package monster.jhentai.threadlocal;

import monster.jhentai.model.bo.JHenTaiUser;

/**
 * @author JTMonster
 * @date 2024/5/25
 */
public class JHenTaiUserThreadLocal {

    private static final ThreadLocal<JHenTaiUser> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(JHenTaiUser user) {
        THREAD_LOCAL.set(user);
    }

    public static JHenTaiUser get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
