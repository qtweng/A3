/**
 * Singleton UniqueID class produced and used in stages of the production line
 *
 * @author QiTao Weng
 */
// 50% coding error
public class UniqueID {
    private static long idCounter = 0;
    private static UniqueID instance = null;
    private static String id;

    //private constructor for singleton implementation
    private UniqueID() {
        id = String.valueOf(idCounter);
    }

    /**
     * @return return the single instance of this UniqueID class
     */
    public static UniqueID getInstance() {
        if (instance == null) {
            instance = new UniqueID();
        }
        return instance;
    }

    /**
     * @return an incrementing unique ID
     */
    public static String getID() {
        return id = String.format("%06d", idCounter++);
    }
}
