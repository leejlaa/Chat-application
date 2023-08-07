package radionica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.LineBorder;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Server1 implements ActionListener {

	static JPanel a1; //kreiramo kontejner tipa JPanel; obicno se koristi kao kontejner za druge komponente Java swing paketa
	static Box vertical = Box.createVerticalBox(); //kontejner tipa Box koji se koristi za smijestanje vise komponenti u horizontalnoj ili vertikalnoj traci (korisno zato sto ne moramo specificirati velicinu (duzinu i sirinu) jer se prilagodjava tome kako se dodaju komponente
	//static JPanel messagesPanel = new JPanel(); //kreiramo JPanel u kojem ce se razmjenjivati poruke i biti vidljive korisnicima

	static JFrame frame = new JFrame(); //kreiramo okvir tj. prozor u koji cemo smijestati komponente naseg GUI-a
	static JTextField unosPoruke = new JTextField(" Type a message..."); //kreiramo polje u koje ce korisnik moci unositi tekst, a defaultni tekst koji ce biti ispisan u tom polju je "Type a message"

	static DataOutputStream dout; //kreiramo varijablu tipa DataOutputStream koja ce nam trebati pri komunikaciji izmedju klijenta i servera; komunikacija preko mreze se odvija u formi bajta; medjutim pisanje poruke u formi bajta (nula i jediniva) za samog korisnika bi bilo dodatno otezavajuce; zbog toga, koristenjem ove varijable omogucava se korisniku da unosi podatke u formi primitivnog tipa podatka (int, UTF-tekst), a potom ce ova varijabla to konvertovati u sekvencu bajta koja je pogodna za mreznu komunikaciju

	Server1() {
		frame.setLayout(null); //iskljucujemo layout managere (relativno u odnosu na i sl.) i specificiramo da cemo sve sto se tice pozicioniranja komponenti unutar glavnog prozora frame mi kontrolisati (pozicija komponenti, visina, sirina i sl.)
		frame.setBounds(300, 50, 400, 600); //koristenjem funkcije setBounds specificiramo poziciju naseg prozora (prva argument je x koordinata, drugi je y koordinata, treci je duzina, a cetvrti visina)

		frame.setUndecorated(true); // uklanjamo default specifikacije na prozoru koji definiramo (title bar, border, buttons, etc.), naslov, cancel dugme i sl.
		// frame.getContentPane().setBackground(new Color(179, 237, 242)); //na ovaj nacin mozemo specificirati boju pozadine glavnog prozora koji kreiramo

		unosPoruke.setBounds(5, 550, 290, 40); //pozicioniramo polje unosPoruke na odgovarajucu lokaciju (x,y,width,height)
		Border okvirPoruke = new LineBorder(new Color(7, 130, 244), 2, true); //kreiramo border za polje u koje ce korisnik moci unositi tekst: prvi argument je boja, drugi argument je debljina, a treci argument moze imati dvije vrijednosti: true ili false, ako je true znaci da ce ivice biti zaobljene, a ako je false ivice nece biti zaobljene
		unosPoruke.setForeground(new Color(7, 130, 244)); //setujemo boju teksta polja unosPoruke
		unosPoruke.setBorder(okvirPoruke); //setujemo definirani border na polje unosPoruke

		JButton posaljiDugme = new JButton("Send"); //kreiramo dugme sa tekstom "Send"
		posaljiDugme.setBounds(300, 550, 90, 40); //pozicioniramo dugme (x,y,sirina, visina)
		posaljiDugme.setForeground(new Color(179, 237, 242)); //setujemo boju teksta "Send" unutar dugmeta
		posaljiDugme.setBackground(new Color(7, 130, 244)); //setujemo boju pozadine dugmeta

		JPanel uvodniPanel = new JPanel(); //kreiramo panel koji ce sluziti kao kontejner za komponente koje cemo smjestati, a on ce da reprezentuje glavnu alatnu traku u GUI-u sa slikom osobe koja komunicira, imenom, ikonama telefona i videa
		uvodniPanel.setBackground(new Color(7, 130, 244)); //setujemo boju pozadine uvodnogPanela
		uvodniPanel.setBounds(0, 0, 400, 67); //pozicioniramo uvodniPanel
		uvodniPanel.setLayout(null); //stujemo layout na null, sto znaci isto kao i za frame: sve sto se tice pozicioniranja komponenti unutar ovog panela rucno mi podesavamo


		// ikona mobitela tj. slusalice
		ImageIcon imageicon = new ImageIcon("C:\\Users\\Asus\\Desktop\\Slike za radionice\\phone.png"); //ucitavamo sliku u varijablu tipa ImageIcon
		Image image = imageicon.getImage(); //uzmemo sliku koju smo ucitali da bismo je na adekvatan nacin skalirali i transformisali
		Image resizeImage = image.getScaledInstance(35, 35, Image.SCALE_SMOOTH); //koristenjem funkcije getScaledInstance() skaliramo sliku na nacin da omjer duzine i visine ostane isti (kvalitet ostane nenarusen); prvi argument je duzina, drugi je visina, a treci argument specificira algoritam skaliranja; ovaj algoritam skaliranja SCALE_SMOOTH daje najbolju kvalitetu
		ImageIcon resizeIcon = new ImageIcon(resizeImage); //ucitamo resize-anu sliku u ImageIcon
		JLabel labelicon = new JLabel(); //kreiramo label u koji cemo ucitati sliku jer je preko labela lakse raditi pozicioniranje
		labelicon.setIcon(resizeIcon); //ucitamo ikonu slike u label
		labelicon.setBounds(340, 20, 35, 35); //specificiramo poziciju labela sa slikom
		uvodniPanel.add(labelicon); //dodamo label u uvodniPanel

		// ikona videa (analogno radimo i sa slikom videa)
		ImageIcon imageicon1 = new ImageIcon("C:\\Users\\Asus\\Desktop\\Slike za radionice\\video.png");
		Image image1 = imageicon1.getImage();
		Image resizeImage1 = image1.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
		ImageIcon resizeIcon1 = new ImageIcon(resizeImage1);
		JLabel labelicon1 = new JLabel();
		labelicon1.setIcon(resizeIcon1);
		labelicon1.setBounds(290, 20, 35, 35);
		uvodniPanel.add(labelicon1);

		// ikona osobe (analogno radimo i sa slikom ikone covjeculjka)
		ImageIcon imageMumka = new ImageIcon("C:\\Users\\Asus\\Desktop\\Slike za radionice\\mumka-removebg-preview.png");
		Image image2 = imageMumka.getImage();
		Image mumka = image2.getScaledInstance(80, 45, Image.SCALE_SMOOTH);
		ImageIcon resizeMumka = new ImageIcon(mumka);
		JLabel labelMumka = new JLabel();
		labelMumka.setIcon(resizeMumka);
		labelMumka.setBounds(5, 20, 80, 40);
		uvodniPanel.add(labelMumka);

		JLabel ime = new JLabel("Mala Mumka"); //specificiramo ime koje ce se koristiti pored slike
		ime.setBounds(110, 25, 150, 30); //pozicioniramo (x,y, sirina, visina)
		uvodniPanel.add(ime); //dodamo labelu u uvodni panel
		ime.setForeground(new Color(179, 237, 242)); //setujemo boju labele
		Font font = new Font("Purisa", Font.BOLD, 15); //kreiramo font teksta: prvi argument je tip fonta, drugi argument je stil fonta, moze biti Font.PLAIN (defaultni font), Font.BOLD, Font.ITALIC, ili Font.BOLD | Font.ITALIC
		ime.setFont(font); //primijenimo definirani font na tekst labele
		posaljiDugme.setFont(font); //primijenimo definirani font na tekst u dugmetu posaljiDugme

		a1 = new JPanel(); //kreiramo novi panel koji cemo koristiti kao kontejner za organizaciju drugih komponenti koje dodajemo
		a1.setBounds(0, 70, 400, 480); //setujemo poziciju i velicinu

		frame.add(a1); //dodamo panel u glavni prozor

		posaljiDugme.addActionListener(this); //dodajemo action listener na posaljiDugme sto ustvari specificira sta ce se dogoditi nad nasim objektom tipa Server1() kad kliknemo na dugme "Send", zbog toga koristimo referencu this

		frame.add(uvodniPanel); //dodamo uvodniPanel u frame
		frame.add(unosPoruke); //dodamo polje za unos teksta u frame
		frame.add(posaljiDugme); //dodamo dugme za slanje poruke u frame
		frame.setVisible(true); //setujemo vidljivost frame-a

	}

	public void actionPerformed(ActionEvent ae) {
		try {
			String out = unosPoruke.getText(); //uzimamo poruku iz polja unosPoruke koju je korisnik unio i zeli da posalje

			JPanel p2 = formatLabel(out); //formatiramo poruku u zeljeni oblik, tako da ce poruka biti ispisana unutar panela p2 pozivom funkcije formatLabel, kao rezultat dobijemo panel u kojem je poruka koju korisnik zeli poslati sa vremenom slanja ispod poruke

			a1.setLayout(new BorderLayout()); //za panel a1 koji nam takodjer sluzi kao kontejner za dio u GUI-u koji ce spremat poruke koje se razmjenjuju izmedju korisnika setuje layout manager kao new BorderLayout() i predstavlja jedan od ugradjenih border layouta u swingu koji podrazumijeva da se prostor koji obuhvata panel a1 podijeli u 5 dijelova: east, west, north, south, center ili page_start, page_end, line_start, line_end, center te da se komponente dodaju u te dijelove 

			JPanel right = new JPanel(new BorderLayout()); //kreiramo novi panel u koji cemo dodati panel sa porukom i vremenom slanja poruke
			right.add(p2, BorderLayout.LINE_END); //dodajemo panel p2 u panel pod imenom right na poziciju definiranu sa BorderLayout.LINE_END, koja predstavlja desnu stranu kontejnera (u ovom slucaju kontejner pod imenom right)
			vertical.add(right); // u vertikalni box dodamo ovaj definirani panel pod imenom right
			vertical.add(Box.createVerticalStrut(15)); //specificira prazan prostor koji se dodaje nakon sto se doda panel pod imenom right koji predstavlja poruku

			a1.add(vertical, BorderLayout.PAGE_START); //dodajemo vertikalni Box sa porukama na pocetak dijela panela a1 koji je predvidjen za dio putem kojeg se mogu poruke razmjenjivati

			dout.writeUTF(out); //u varijablu tipa DataOutputStream upisujemo poruku koju je korisnik unio u field unosPoruke da bi se mogla poslati preko mreze

			unosPoruke.setText(""); //setujemo polje unosPoruke u koje smo unijeli poruku na "" da bi se mogla unijeti nova poruka nakon sto se prethodno unesena procesira

			
			//naredne linije osiguravaju da se promjene koje se desavaju u glavnom prozoru adekvatno primijene
			frame.repaint();
			frame.invalidate();
			frame.validate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JPanel formatLabel(String out) {
		JPanel panel = new JPanel(); //kreiramo novi panel
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //za novokreirani panel setujemo novi layout manager kojeg kreiramo pomocu izraza u zagradi; prvi argument specificira kontejner za koji se primjenjuje novi layout, a drugi argument specificira osu duz koje ce elementi biti redani; BoxLayout.Y_AXIS specificira vertikalno poravnanje; ova linija koda kreira panel sa BoxLayoutom koji osigurava vertikalno poravnanje za komponente; dakle elementi koji se dodaju u ovaj panel ce biti redani jedan ispod drugog vertikalno

		JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");//kreiramo labelu koja ce ustvari predstavljati poruku koja se ispisuje i ovo tretiramo kao html; izraz u zagradi kreira html paragraf sirine 150px u koji ce se upidati vrijednost varijable out odnosno poruke koju je korisnik unio
		output.setFont(new Font("Purisa", Font.BOLD, 15)); //setujemo font u zagradi na tekst unutar labela koji je korisnik unio
		output.setBackground(new Color(7, 130, 244)); //setujemo boju pozadine labele
		output.setForeground(new Color(179, 237, 242)); //setujemo boju teksta labele
		output.setOpaque(true); //na ovaj nacin kazemo da ce nasa komponenta output koja je ustvari label "pokriti" sve iza sebe
		output.setBorder(new EmptyBorder(15, 15, 15, 50)); //kreiramo border oko nase komponente jlabel, a argumenti ustvari predstavljaju duzinu praznog prostora oko komponente sa svake strane: top, left, bottom, right
		panel.add(output); //dodamo output (label sa tekstom u nas panel);

		Calendar cal = Calendar.getInstance(); //kreira varijablu tipa Calendar koja reprezentuje trenutni datum i vrijeme
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //kreiramo varijablu tipa SimpleDateFormat koja se koristi za parsiranje vremena i datuma, a izraz u zagradi specificira nacin na koji ce se vrijeme reprezentirati: sat:minuta

		JLabel time = new JLabel(); //kreiramo novi label
		time.setText(sdf.format(cal.getTime())); //setujemo tekst unutar labela da bude trenutno vrijeme u prethodno definisanom formatu sat:minuta, ako je vrijeme 3:30 PM ispis ce biti 15:30;

		panel.add(time); //dodajemo ga u panel koji kreiramo, a obzirom da je na panelu setovan BoxLayout.Y_AXIS, dodat ce se ispod labela koji reprezentira box sa porukom koju korisnik salje

		return panel; //vratimo kreirani panel sa porukom i vremenom slanja funkciji koja nas je pozvala
	}

	public static void main(String[] args) {
		new Server1(); //koristenjem konstruktora kreiramo objekat tipa Server1()
		unosPoruke.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				unosPoruke.setText("");
			}
		}); //na ovaj nacin specificiramo mouseListener koji ustvari kaze da kad se klikne misem na polje unosPoruke izbrise se defaultna poruka "Type a message" i postavi na prazan string; ovo je odradjeno koristenjem lambda izraza
		
		/* u narednom dijelu specificiramo dio koji se odnosi na komunikaciju preko mreze putem klijenta i servera*/
		try {
			ServerSocket skt = new ServerSocket(20007); //kreira ServerSocket koji slusa na portu 20007 za nadolazece konekcije od klijenata
	//naredna petlja specificira da ce kontinuirano slusati nadolazece konekcije
			while (true) {
				Socket s = skt.accept(); //ceka da se klijent javi serveru i nakon sto se javi uspostavi se konekcija pozivom sistemskog poziva accept() te se za dalju komunikaciju koristi socket kojem pristupamo preko varijable s
				//sve sto server zeli da posalje ili sto mu se posalje treba da se procita iz ovog socketa; ako zeli nesto da posalje server aplikacija ce to zapisati u ovaj socket, a ako zeli nesto da procita, onda ce da to procita iz ovog socketa
				DataInputStream din = new DataInputStream(s.getInputStream()); //kreira DataInputStream koji cita podatke koje klijent posalje serveru i da bi se uspostavila konekcija server mora odgovoriti
				dout = new DataOutputStream(s.getOutputStream()); //koristi prethodno definiranu varijablu dout da bi poslao zeljeni sadrzaj klijentu

				while (true) {
					String msg = din.readUTF(); //na ovaj nacin putem varijable din iz socketa se cita ono sto je klijent poslao i to se smijesta u varijablu msg
					JPanel panel = formatLabel(msg); //ovu poruku formatiramo na identican nacin kao sto je prethodno opisano pozivom funkcije formatLabel

					JPanel left = new JPanel(new BorderLayout()); //kreiramo novi panel u koji ce se procitana poruka spremiti ali s lijeve strane
					left.add(panel, BorderLayout.LINE_START); //dodajemo poruku u panel oznacen varijablom left
					vertical.add(left); // dodajemo vertikalnom boxu i ovu poruku
					frame.validate(); //radimo update da bi se promjene prikazale u GUI-u
				}
			}
		} catch (Exception e) {
			e.printStackTrace(); //u slucaju greske ako konekcija ne uspije ispisujemo gresku koja ce biti uhvacena u ovom catch bloku
		}
	}

}
