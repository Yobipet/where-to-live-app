import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class GUITESTING extends JButton {
    public GUITESTING(String label) {
        super(label);

        // These statements enlarge the button so that it
        // becomes a circle rather than an oval.
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        // This call causes the JButton not to paint the background.
        // This allows us to paint a round background.
        setContentAreaFilled(false);
    }

    // Paint the round background and label.
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            // You might want to make the highlight color
            // a property of the RoundButton class.
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillOval(0, 0, getSize().width-1, getSize().height-1);

        // This call will paint the label and the focus rectangle.
        super.paintComponent(g);
    }

    // Paint the border of the button using a simple stroke.
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width-1, getSize().height-1);
    }

    // Hit detection.
    Shape shape;
    public boolean contains(int x, int y) {
        // If the button has changed size, make a new shape object.
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }

    // Test routine.
    public static void main(String[] args) {
        // Create a button with the label "Jackpot".
        JButton button = new RoundButton("Jackpot");
        button.setBackground(Color.green);
        button.setBounds(0, 0, 100, 100);

        JButton button2 = new RoundButton("Jackpot2");
        button2.setBackground(Color.red);
        button2.setBounds(50, 50, 100, 100);

        // Create a frame in which to show the button.
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.yellow);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(button);
        frame.getContentPane().add(button2);
//        frame.getContentPane().setLayout(new FlowLayout());
        frame.setSize(200, 200);
        frame.setVisible(true);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseEntered( MouseEvent e )
            {}

            public void mouseExited( MouseEvent e )
            {}

            public void mouseClicked( MouseEvent e )
            {
                System.out.println( "clicked " );
            }

            public void mousePressed( MouseEvent e )
            {
                System.out.println( "pressed " );
            }

            public void mouseReleased( MouseEvent e )
            {
                System.out.println( "released " );
            }
        };
        button.addMouseListener( mouseListener );

    }
}