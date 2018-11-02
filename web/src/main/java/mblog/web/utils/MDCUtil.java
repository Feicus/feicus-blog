package mblog.web.utils;
import org.slf4j.MDC;

/**
 * @author feicus
 * @Description:
 * @date 13:22
 */

public class MDCUtil {
    public MDCUtil() {
    }

    public static void put(MDCUtil.Type type, String value) {
        MDC.put(type.name(), value);
    }

    public static String get(MDCUtil.Type type) {
        return MDC.get(type.name());
    }

    public static void remove(MDCUtil.Type type) {
        MDC.remove(type.name());
    }

    public static void clear() {
        MDC.clear();
    }

    public static enum Type {
        TRACE_ID("跟踪号"),
        TRANS_ID("订单号");

        private String desc;

        private Type(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }
}
