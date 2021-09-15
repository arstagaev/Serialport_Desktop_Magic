package customview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Engine extends JPanel implements ActionListener {


    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;

    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;  //show directions
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private int xloc = 6*DOT_SIZE;
    private int yloc = 6*DOT_SIZE;

    int width = 320;
    int height = 320;

    int rows = 20;

    int cols = 20;


    public Engine(){
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        System.out.println("check engine start");
//        GridCanvas xyz = new GridCanvas(320,320,20,20);
//        add(xyz);



    }

    public void initGame(){
        for (int i = 0; i< dots ; i++ ){
            x[i] = 48 - i*  DOT_SIZE;
            y[i] = 48;
        }
        dots= 1;
        timer = new Timer(250,this);

    }

    public void loadImages(){
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("check paint comp");

        g.drawLine(0,0,100,100);
        g.setColor(Color.GREEN);
        g.drawLine(100,1000,10,1);
        if(inGame){
            //g.drawImage(apple,appleX,appleY,this);

            int f;
            width = getSize().width;
            height = getSize().height;

            // draw the rows
            int rowHt = 16;
            for (f = 0; f < rows; f++)
                g.drawLine(0, f * rowHt, width, f * rowHt);

            // draw the columns
            int rowWid = 16;
            for (f = 0; f < cols; f++)
                g.drawLine(f * rowWid, 0, f * rowWid, height);


           // g.drawImage(dot,x[i],y[i],this);
            g.drawImage(dot,xloc,yloc,this);

            //xyz2.paint(g);
//            for (int i = 0; i < dots; i++) {
//
//            }


        } else{
            String str = "Game Over";
            //Font f = new Font("Arial",14,Font.BOLD);
            g.setColor(Color.white);
            // g.setFont(f);
            g.drawString(str,125,SIZE/2);
        }
    }
//    public void move(){
//        for (int i = dots; i > 0; i--) {
//            x[i] = x[i-1];
//            y[i] = y[i-1];
//        }
//        if(left){
//            x[0] -= DOT_SIZE; // or this x[0] = x - dotsize
//        }
//        if(right){
//            x[0] += DOT_SIZE;
//        } if(up){
//            y[0] -= DOT_SIZE;
//        } if(down){
//            y[0] += DOT_SIZE;
//        }
//    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("check");
        if (inGame){
            //move();

        }
        //repaint();

    }

//    private void renderGrid(){
//        GridCanvas xyz = new GridCanvas(320,320,20,20);
//        add(xyz);
//    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT){
                System.out.println("Pressed Left");
                xloc = xloc-DOT_SIZE;

                repaint();
                //renderGrid();

            }
            if (key == KeyEvent.VK_RIGHT){
                System.out.println("Pressed Right");
                xloc = xloc+DOT_SIZE;

                repaint();
                //renderGrid();

            }
        }
    }

}
