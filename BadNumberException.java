
package bigcalc;

/**
 *
 * @author User--1
 */
public class BadNumberException extends RuntimeException
{

    /**
     * Creates a new instance of <code>BadNumberException</code> without detail message.
     */
    public BadNumberException()
    {
    }

    /**
     * Constructs an instance of <code>BadNumberException</code> with the specified detail message.
     * @param msg the detail message.
     */
    
    public BadNumberException(String msg)
    {
        super(msg);
    }
}
