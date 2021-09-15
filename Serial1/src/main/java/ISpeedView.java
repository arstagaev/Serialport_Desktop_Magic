import java.util.Date;

/**
 * This interface provides functionality necessary to update a speed view.
 *
 * @author Clemens Krainer
 */
public interface ISpeedView
{
    /**
     * Communicate the current speed to the view.
     *
     * @param date the instant as a <code>Date</code> object.
     * @param speed the according speed.
     */
    public void setSpeed (Date date, double speed);
    /**
     * Invalidate the displayed data. A controller calls this method to notify a
     * view that no more data is available. By receiving this notification, the
     * view should change its appearance.
     */
    public void invalidate ();
}
