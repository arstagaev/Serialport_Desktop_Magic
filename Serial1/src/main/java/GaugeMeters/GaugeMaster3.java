package GaugeMeters;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class implements a <code>Speedometer</code> view by implementing the
 * <code>GaugeMeters.ISpeedView</code> interface.
 *
 * @author Clemens Krainer
 */
public class GaugeMaster3 extends JPanel implements ISpeedView
{
    private static final long serialVersionUID = -4076648741571762140L;

    /**
     * Some geometric constants.
     */
    private static final int size = 180;
    private static final double f1 = 0.2777777;
    private static final double f2 = 0.3888888;
    private static final double f3 = 0.05;
    private static final double f4 = 0.0888888;
    private static final int r1 = (int)(size * f1);
    private static final int r2 = (int)(size * f2);
    private static final int l3 = (int)(size * f3);
    private static final int l4 = (int)(size * f4);
    private static final int x_origin = size/2;
    private static final int y_origin = size/2;
    private static final double startAngle = 225;
    private static final double endAngle = -45;
    private static double max_speed = 100;
    private static final double main_interval = (startAngle-endAngle) / 5;
    private static final double sub_interval = (startAngle-endAngle) / 25;
    private static final int needleDiameter = (int)(size * 0.1);
    private static final int needleLength = r1 + l3;

    /**
     * The <code>AffineTransform</code> matrix that rotates the speedometer needle.
     */
    AffineTransform	at = new AffineTransform ();

    /**
     * The current speed as a double value.
     */
    private double speed = 0;

    /**
     * The current speed as a string.
     */
    private String speedString = null;

    /**
     * This variable contains all scale gradations of the speedometer.
     */
    private Shape[] shapes;

    /**
     * This <code>Ellipse2D</code> represents the center of the speedometer needle.
     */
    private static final Ellipse2D needleCenter = new Ellipse2D.Double (
            x_origin-needleDiameter/2, y_origin-needleDiameter/2, needleDiameter, needleDiameter);

    /**
     * This <code>Polygon</code> represents the speedometer needle.
     */
    private static final Polygon needlePolygon = new Polygon (
            new int[] {x_origin+needleLength,x_origin,x_origin,x_origin+needleLength,x_origin+needleLength},
            new int[] {y_origin-2, y_origin-needleDiameter/4, y_origin+needleDiameter/4, y_origin+1, y_origin-2},
            5);

    /**
     * This variable contains a <b>en_US</b> schema. The simulator uses this
     * locale for converting numbers into Strings.
     */
    private Locale locale;

    public void setMax(Double maxValue){
        max_speed = maxValue;
    }
    /**
     * Construct a <code>Speedometer</code>.
     */
    public GaugeMaster3()
    {

        setSize (size, size);
        shapes = new Shape[26];


        int i=0;
        for (double k=startAngle; k > endAngle; k -= main_interval)
        {
            if (k == startAngle)
                shapes[i++] = createLine (r1, l4, k);

            for (double j=sub_interval; j <= main_interval-sub_interval; j += sub_interval)
            {
                shapes[i++] = createLine (r1,l3,k-j);
            }
            shapes[i++] = createLine (r1, l4, k-main_interval);
        }
//		System.out.println ("I="+i);

        locale = new Locale ("de","AT");
    }

    /**
     * Create a line of the scale gradations.
     *
     * @param radius the inner radius of the scale
     * @param length the length of the line
     * @param alpha the angle of the line
     * @return the corresponding line as a <class>Shape</class> instance.
     */
    private Shape createLine (double radius, double length, double alpha)
    {
        double sinAlpha = Math.sin (Math.toRadians (alpha));
        double cosAlpha = Math.cos (Math.toRadians (alpha));
        return new Line2D.Double (
                x_origin+radius*cosAlpha, y_origin-radius*sinAlpha,
                x_origin+(radius+length)*cosAlpha, y_origin-(radius+length)*sinAlpha
        );
    }

    /* (non-Javadoc)
     * @see at.uni_salzburg.cs.ckgroup.ui.GaugeMeters.ISpeedView#setSpeed(java.util.Date, double)
     */
    boolean lastSpeedIsZero = false;
    public void setSpeed (Date date, double currentSpeed)
    {
        this.speed = currentSpeed/3.6;


        NumberFormat nf = NumberFormat.getInstance(locale);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setMinimumIntegerDigits(1);
        String h1 = this.speed < 10 ? " " : "";
        String h2 = currentSpeed < 10 ? " " : "";
        this.speedString =  h2 + nf.format(currentSpeed) +"";
//		this.speedString = String.format ("%5.2fm/s = %5.2fkm/h", new Object[] {new Double (speed),new Double (currentSpeed)});
//        if (currentSpeed == 0 && !lastSpeedIsZero){
//            lastSpeedIsZero = true;
//
//        }else{
//            lastSpeedIsZero = false;
//        }
        repaint ();
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent (Graphics g)
    {
        super.paintComponent (g);
        Graphics2D ga = (Graphics2D)g;

        ga.setColor (Color.white);
        ga.fillRect (0, 0, 200, 200);
        ga.setColor (Color.black);
        ga.drawRect (0, 0, 200, 200); // draw border
        ga.drawString ("Pressure", 5, 15);

        if (speedString != null)
            ga.drawString (speedString, 5, size-5);

        for (int k=0; k < shapes.length; k++)
            ga.draw (shapes[k]);

        NumberFormat nf = NumberFormat.getInstance(locale);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        nf.setMinimumIntegerDigits(1);
        for (double alpha=startAngle; alpha >= endAngle; alpha -= main_interval)
        {
            double sinAlpha = Math.sin (Math.toRadians (alpha));
            double cosAlpha = Math.cos (Math.toRadians (alpha));
            int speed = (int)(max_speed*(startAngle - alpha)/(startAngle-endAngle));
            String text = nf.format (speed);
//			String text = String.format ("%d", new Object[] {new Integer (speed)});
            ga.drawString (text, (int)(x_origin-7+(r2+7)*cosAlpha), (int)(y_origin+3-(r2+5)*sinAlpha));
        }

        at.setToIdentity ();
        at.translate (x_origin, y_origin);

        double angle = (3.6*speed*(startAngle - endAngle)/max_speed) - startAngle;
        at.rotate (Math.toRadians (angle));
        AffineTransform saveXform = ga.getTransform ();
        AffineTransform toCenterAt = new AffineTransform ();
        toCenterAt.concatenate (at);
        toCenterAt.translate (-x_origin, -y_origin);
        ga.transform (toCenterAt);

        ga.fill (needleCenter);

        if (speedString != null)
            ga.fillPolygon (needlePolygon);

        ga.setTransform (saveXform);
    }
}