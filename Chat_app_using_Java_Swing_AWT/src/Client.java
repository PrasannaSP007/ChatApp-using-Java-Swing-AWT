import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.net.*;
import java.io.*;

public class Client {

    static JFrame frame;
    static JPanel body;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;

    public Client() {

        frame = new JFrame("Client");

        JPanel header = new JPanel();
        header.setBackground(new Color(7, 94, 84));
        header.setBounds(0, 0, 350, 50);
        header.setLayout(null);
        frame.add(header);

        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("assets/back.png"));
        ImageIcon i1 = new ImageIcon(img1.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        JLabel back = new JLabel(i1);
        back.setBounds(5, 15, 20, 20);
        header.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        ImageIcon img2 = new ImageIcon(ClassLoader.getSystemResource("assets/user2.png"));
        ImageIcon i2 = new ImageIcon(img2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        JLabel user2 = new JLabel(i2);
        user2.setBounds(30, 10, 30, 30);
        header.add(user2);

        JLabel name = new JLabel("User 2");
        name.setBounds(67, 8, 70, 17);
        name.setFont(new Font("SANS_SERIF", Font.BOLD, 16));
        name.setForeground(Color.WHITE);
        header.add(name);

        JLabel status = new JLabel("online");
        status.setBounds(67, 27, 70, 16);
        status.setFont(new Font("SANS_SERIF", Font.BOLD, 12));
        status.setForeground(Color.WHITE);
        header.add(status);

        ImageIcon img3 = new ImageIcon(ClassLoader.getSystemResource("assets/video.png"));
        ImageIcon i3 = new ImageIcon(img3.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        JLabel video = new JLabel(i3);
        video.setBounds(252, 15, 20, 20);
        header.add(video);

        ImageIcon img4 = new ImageIcon(ClassLoader.getSystemResource("assets/call.png"));
        ImageIcon i4 = new ImageIcon(img4.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        JLabel call = new JLabel(i4);
        call.setBounds(288, 15, 20, 20);
        header.add(call);

        ImageIcon img5 = new ImageIcon(ClassLoader.getSystemResource("assets/menu.png"));
        ImageIcon i5 = new ImageIcon(img5.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        JLabel menu = new JLabel(i5);
        menu.setBounds(322, 15, 20, 20);
        header.add(menu);

        body = new JPanel(new BorderLayout());
        body.setBounds(4, 54, 342, 397);
        body.setBackground(Color.lightGray);
        frame.add(body);

        JTextField text = new JTextField();
        text.setBounds(4, 458, 270, 35);
        text.setFont(new Font("SANS_SERIF", Font.BOLD, 15));
        frame.add(text);

        JButton send = new JButton("Send");
        send.setBounds(278, 458, 66, 35);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        // if(text.getText()=="")
        //     send.setEnabled(false);
        // else 
        //     send.setEnabled(true);
        frame.add(send);

        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String msg = text.getText();
                    JPanel p1 = formatLabel(msg);

                    JPanel right = new JPanel(new BorderLayout());
                    right.add(p1, BorderLayout.LINE_END);
                    right.setBackground(Color.lightGray);

                    vertical.add(right);
                    vertical.add(Box.createVerticalStrut(10));
                    body.add(vertical, BorderLayout.PAGE_START);
                
                    dout.writeUTF(msg);
 
                    text.setText("");
                    frame.repaint();
                    frame.invalidate();
                    frame.validate();
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        frame.setLayout(null);
        // frame.setUndecorated(true);
        // frame.setSize(350, 500);
        // frame.setLocation(200, 75);
        frame.setBounds(800, 75, 365, 535);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setVisible(true);
    }

    public static JPanel formatLabel(String msg) {
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.lightGray);

        JLabel output = new JLabel("<html><p style=\"width:125px;\">"+msg+"</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 15));
        output.setBackground(new Color(37, 211, 102));
        output.setBorder(new EmptyBorder(10, 10, 10, 15));
        output.setOpaque(true);
        p.add(output);

        JLabel time = new JLabel();
        // Calendar cal = Calendar.getInstance();
        // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        // time.setText(sdf.format(cal.getTime()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        time.setText(LocalTime.now().format(dtf));
        p.add(time);
        return p;
    }

    public static void main(String[] args) {
        new Client();

        try {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(true) {
                String message = din.readUTF();
                JPanel panel = formatLabel(message);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                left.setBackground(Color.lightGray);
                vertical.add(left, BorderLayout.PAGE_START);
                frame.validate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
