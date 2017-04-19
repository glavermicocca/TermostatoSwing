import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import thermostat.bean.Sensor;
import thermostat.bean.Values;
import thermostat.gpio.Rele;
import thermostat.thread.CurrentCalendar;
import thermostat.thread.CurrentDateTime;
import thermostat.thread.CurrentThemperatureUmidity;
import thermostat.thread.ReleToggle;

import javax.swing.border.EtchedBorder;
import javax.swing.text.html.ListView;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class Start {

	private JFrame frame;
	
	private static Sensor sensor;
	private static Rele rele;
	private static Values values;

	private static void LaunchLogToFile() throws Exception
	{
		File file = new File("err.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setErr(ps);
		
		System.err.println("This goes to err.txt");
	}
	
	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		LaunchLogToFile();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start window = new Start();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Start() {
		
		values = new Values(18, false);
		sensor = new Sensor(values);
		rele = new Rele();
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
	    frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setBounds(0, 0, 320, 240);
		
		ImageComponent temperatureImage = new ImageComponent("resources/thermometer.png");
		temperatureImage.setBounds(0, 0, 12, 44);

		JLabel temperatureCurrent = new JLabel("Temp \u00B0");
		temperatureCurrent.setFont(new Font("Roboto Black", Font.PLAIN, 12));
		temperatureCurrent.setBounds(18, 0, 60, 44);
		
		ImageComponent humidityImage = new ImageComponent("resources/humidity.png");
		humidityImage.setBounds(84, 0, 27, 44);
		
		JLabel umidityCurrent = new JLabel("Umid %");
		umidityCurrent.setFont(new Font("Roboto Black", Font.PLAIN, 12));
		umidityCurrent.setBounds(117, 0, 49, 44);
		
		JLabel time = new JLabel("TIME");
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setFont(new Font("Roboto Black", Font.PLAIN, 18));
		time.setBounds(244, 0, 72, 44);
		
		JLabel date = new JLabel("DATE");
		date.setHorizontalAlignment(SwingConstants.CENTER);
		date.setFont(new Font("Roboto Black", Font.PLAIN, 12));
		date.setBounds(172, 0, 72, 44);
		
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(temperatureImage);
		frame.getContentPane().add(temperatureCurrent);
		frame.getContentPane().add(humidityImage);
		frame.getContentPane().add(umidityCurrent);
		frame.getContentPane().add(time);
		frame.getContentPane().add(date);
		
		JPanel panelContainerSchedule = new JPanel();
		panelContainerSchedule.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelContainerSchedule.setBounds(162, 47, 154, 125);
		frame.getContentPane().add(panelContainerSchedule);
				
		JPanel panelContainerSetTemperature = new JPanel();
		panelContainerSetTemperature.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelContainerSetTemperature.setBounds(4, 47, 154, 190);
		frame.getContentPane().add(panelContainerSetTemperature);
		panelContainerSetTemperature.setLayout(null);
		
		final JLabel temperature = new JLabel("18.0\u00B0");
		temperature.setHorizontalAlignment(SwingConstants.CENTER);
		temperature.setFont(new Font("Roboto Black", Font.PLAIN, 44));
		temperature.setBounds(2, 76, 148, 48);
		panelContainerSetTemperature.add(temperature);
		
		JButton btnMinus = new JButton("-");
		btnMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				values.decrementOneDegree();
				temperature.setText(values.getTemperature() + "\u00B0");
			}
		});
		
		btnMinus.setBounds(2, 2, 76, 72);
		btnMinus.setFont(new Font("Roboto Black", Font.PLAIN, 44));
		panelContainerSetTemperature.add(btnMinus);
		
		JButton btnPlus = new JButton("+");
		btnPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				values.addOneDegree();
				temperature.setText(values.getTemperature() + "\u00B0");
			}
		});
		btnPlus.setFont(new Font("Roboto Black", Font.PLAIN, 44));
		btnPlus.setBounds(76, 2, 76, 72);
		panelContainerSetTemperature.add(btnPlus);
		
		final JButton btnOnoff = new JButton("OFF");
		btnOnoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if (values.isToggleButton()) {
					values.setToggleButton(false);
					btnOnoff.setText("ON");
					
					rele.set(Rele.CALDAIA, Rele.OFF);
				} else {
					values.setToggleButton(true);
					btnOnoff.setText("OFF");
					
					//accendo subito se la temperatura lo consente...
					if(sensor.isHigher())
					{
						rele.set(Rele.CALDAIA, Rele.ON);
					}
					else
					{
						rele.set(Rele.CALDAIA, Rele.OFF);
					}
				}
			}
		});
		
		btnOnoff.setFont(new Font("Roboto Black", Font.PLAIN, 44));
		btnOnoff.setBounds(2, 126, 150, 62);
		panelContainerSetTemperature.add(btnOnoff);
		panelContainerSchedule.setLayout(null);
		
		JLabel schedules = new JLabel();
		schedules.setText("Eventi programmati");
		schedules.setFont(new Font("Roboto Black", Font.PLAIN, 16));
		schedules.setBounds(2, 2, 149, 119);
		panelContainerSchedule.add(schedules);
		
		new CurrentCalendar(panelContainerSetTemperature, schedules, values, rele, sensor, temperature, btnOnoff, panelContainerSchedule).execute();
		
		new CurrentDateTime(time, date).execute();
		
		new CurrentThemperatureUmidity(values, rele, sensor, temperatureCurrent, umidityCurrent).execute();
		
		final JButton btnL = new JButton("L");
		btnL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReleToggle(btnL, rele, Rele.LEFT).execute();
			}
		});
		btnL.setFont(new Font("Roboto Black", Font.PLAIN, 44));
		btnL.setBounds(162, 175, 76, 62);
		frame.getContentPane().add(btnL);
		
		final JButton btnR = new JButton("R");
		btnR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReleToggle(btnR, rele, Rele.RIGHT).execute();
			}
		});
		btnR.setFont(new Font("Roboto Black", Font.PLAIN, 44));
		btnR.setBounds(240, 175, 76, 62);
		frame.getContentPane().add(btnR);
	}	
}
