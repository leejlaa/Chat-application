package radionica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Klijent implements ActionListener {

	static JPanel a1;
	static Box vertical = Box.createVerticalBox();
	static JScrollPane scrollPane;

	static JFrame frame = new JFrame();
	static JTextField unosPoruke = new JTextField(" Type a message...");

	static DataOutputStream dout;

	Klijent() {
		
		frame.setLayout(null);
		frame.setBounds(800, 50, 400, 600);
		frame.setUndecorated(true); // Remove window decorations (title bar, border, buttons, etc.)
		// frame.getContentPane().setBackground(new Color(179, 237, 242));

		unosPoruke.setBounds(5, 550, 290, 40);
		Border okvirPoruke = new LineBorder(new Color(7, 130, 244), 2, true);
		unosPoruke.setForeground(new Color(7, 130, 244));
		unosPoruke.setBorder(okvirPoruke);

		JButton posaljiDugme = new JButton("Send");
		posaljiDugme.setBounds(300, 550, 90, 40);
		posaljiDugme.setForeground(new Color(179, 237, 242));
		posaljiDugme.setBackground(new Color(7, 130, 244));

		JPanel uvodniPanel = new JPanel();
		uvodniPanel.setBackground(new Color(7, 130, 244));
		uvodniPanel.setBounds(0, 0, 400, 67);
		uvodniPanel.setLayout(null);

		// ikona mobitela tj. slusalice
		ImageIcon imageicon = new ImageIcon("C:\\Users\\Asus\\Desktop\\Slike za radionice\\phone.png");
		Image image = imageicon.getImage();
		Image resizeImage = image.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
		ImageIcon resizeIcon = new ImageIcon(resizeImage);
		JLabel labelicon = new JLabel();
		labelicon.setIcon(resizeIcon);
		labelicon.setBounds(340, 20, 35, 35);
		uvodniPanel.add(labelicon);

		// video icon
		ImageIcon imageicon1 = new ImageIcon("C:\\Users\\Asus\\Desktop\\Slike za radionice\\video.png");
		Image image1 = imageicon1.getImage();
		Image resizeImage1 = image1.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
		ImageIcon resizeIcon1 = new ImageIcon(resizeImage1);
		JLabel labelicon1 = new JLabel();
		labelicon1.setIcon(resizeIcon1);
		labelicon1.setBounds(290, 20, 35, 35);
		uvodniPanel.add(labelicon1);

		// ikona covjeculjka
		ImageIcon imageMumka = new ImageIcon("C:\\Users\\Asus\\Desktop\\Slike za radionice\\maja1-removebg-preview.png");
		Image image2 = imageMumka.getImage();
		Image mumka = image2.getScaledInstance(80, 45, Image.SCALE_SMOOTH);
		ImageIcon resizeMumka = new ImageIcon(mumka);
		JLabel labelMumka = new JLabel();
		labelMumka.setIcon(resizeMumka);
		labelMumka.setBounds(5, 20, 80, 40);
		uvodniPanel.add(labelMumka);

		JLabel ime = new JLabel("Pčelica Maja");
		ime.setBounds(110, 25, 150, 30);
		uvodniPanel.add(ime);
		ime.setForeground(new Color(179, 237, 242));
		Font font = new Font("Purisa", Font.BOLD, 15);
		ime.setFont(font);
		posaljiDugme.setFont(font);

	
		a1 = new JPanel();
		a1.setBounds(0, 70, 400, 480);

		frame.add(a1);

		
		posaljiDugme.addActionListener(this);
		

		frame.add(uvodniPanel);
		frame.add(unosPoruke);
		frame.add(posaljiDugme);
		frame.setVisible(true);

	}

	public void actionPerformed(ActionEvent ae) {
		try {
			String out = unosPoruke.getText();

			JPanel p2 = formatLabel(out);

			a1.setLayout(new BorderLayout());

			JPanel right = new JPanel(new BorderLayout());
			right.add(p2, BorderLayout.LINE_END);
			vertical.add(right);
			vertical.add(Box.createVerticalStrut(15));
			a1.add(vertical, BorderLayout.PAGE_START);

			dout.writeUTF(out);

			unosPoruke.setText("");

			frame.repaint();
			frame.invalidate();
			frame.validate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static JPanel formatLabel(String out) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Font font = new Font("Purisa", Font.BOLD, 15);

		JLabel output = new JLabel("<html><p style=\"width: 150px;\">" + out + "</p></html>");
		output.setFont(font);
		output.setBackground(new Color(7, 130, 244));
		output.setForeground(new Color(179, 237, 242));
		output.setOpaque(true);
		output.setBorder(new EmptyBorder(15, 15, 15, 50));
		

		panel.add(output);

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		JLabel time = new JLabel();
		time.setText(sdf.format(cal.getTime()));

		panel.add(time);

		return panel;
	}

	public static void main(String[] args) {
		new Klijent();
		unosPoruke.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				unosPoruke.setText("");
			}
		});

		try {
			Socket s = new Socket("127.0.0.1", 20007); //kreira socket sa loopback adresom jer cemo i klijenta i servera pokrenuti na istom racunaru
			DataInputStream din = new DataInputStream(s.getInputStream()); //kreira DataInputStream putem kojeg ce da cita poruke koje server mu posalje
			dout = new DataOutputStream(s.getOutputStream()); //inicijalizira varijablu dout putem koje ce moci da salje serveru zeljeni sadrzaj

			while (true) {
				a1.setLayout(new BorderLayout());
				String msg = din.readUTF();
				JPanel panel = formatLabel(msg);

				JPanel left = new JPanel(new BorderLayout());
				left.add(panel, BorderLayout.LINE_START);
				vertical.add(left);

				vertical.add(Box.createVerticalStrut(15));
				a1.add(vertical, BorderLayout.PAGE_START);

				frame.validate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
