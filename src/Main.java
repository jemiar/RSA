//CS 342 - SPRING 2016
//Project 3
//Group 55
//Member : Hoang Minh Huynh Nguyen (hhuynh20)
//Member : Kevin Molina (kmolin2)

//Class: Main.java
//Responsibility: build GUI, handle interaction

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
	
	private static ArrayList<String> primes;						//arraylist to store prime numbers read from file
	private static JTextField prime1;								//textfield for 1st prime number input
	private static JTextField prime2;								//textfield for 2nd prime number input
	private static JTextField blockSize;							//textfield for blocking size
	private static JTextField unBlockSize;							//textfield for size used in unblocking
	private static HugeInteger pvalue;								//p value in RSA Algorithm
	private static HugeInteger qvalue;								//q value in RSA Algorithm
	private static final HugeInteger ZERO = new HugeInteger();		//constant 0
	private static final HugeInteger ONE = new HugeInteger("1");	//constant 1
	private static final HugeInteger TWO = new HugeInteger("2");	//constant 2
	private static final HugeInteger HDR = new HugeInteger("100");	//constant 3
	private static String base;										//n value
	private static String expo;										//e or d value
	private static ArrayList<String> stringList;					//string list to store strings read from file
	private static int blkSize;										//blocking size
	private static int unBlkSize;									//size for unblocking
	private static StringBuilder message;							//stringbuilder to store string read from message file

	public static void main(String[] args) {
		
//Setup the GUI
		
		//Setup frame
		JFrame frame = new JFrame("RSA Algorithm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new GridLayout(10, 1));
		
		//Setup menu
		JMenu menu = new JMenu("File");
		menu.setMnemonic('F');
		
		//Setup menu item
		JMenuItem about = new JMenuItem("About");
		about.setMnemonic('A');
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String h = "RSA Algorithm 1.0\n"
						+ "Created by Hoang Minh Huynh Nguyen and Kevin Molina.\n"
						+ "GNU License. 2016";
				JOptionPane.showMessageDialog(null, h);
			}
		});
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.setMnemonic('Q');
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int quit = JOptionPane.showConfirmDialog(null, "Are you sure to quit the program?", "", JOptionPane.YES_NO_OPTION);
				if(quit == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		
		//Add menu item into menu
		menu.add(about);
		menu.add(quit);
		
		//Add menu into frame
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		//Generate keys with input
		JLabel label1 = new JLabel(" Generate keys with input--------------------------------------------------------------------------");
		frame.add(label1);
		
		JPanel panel1 = new JPanel();
		JLabel num1 = new JLabel("1st prime:");
		panel1.add(num1);
		prime1 = new JTextField();
		prime1.setColumns(20);
		panel1.add(prime1);
		JLabel num2 = new JLabel("2nd prime:");
		panel1.add(num2);
		prime2 = new JTextField();
		prime2.setColumns(20);
		panel1.add(prime2);
		JButton genkey1 = new JButton("Generate");
		//get values from textfields, check if they are prime numbers then generate keys, stored in privatekey.txt and publickey.txt
		genkey1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String pr1 = prime1.getText();
				String pr2 = prime2.getText();
				long p1;
				long p2;
				try{
					p1 = Long.parseLong(pr1);
					p2 = Long.parseLong(pr2);
					if(isPrime(p1) && isPrime(p2)){
						pvalue = new HugeInteger(pr1);
						qvalue = new HugeInteger(pr2);
						generateKey(pvalue, qvalue);
					}else{
						JOptionPane.showMessageDialog(null, "Please input prime numbers.");
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Please input only numeric values.");
				}
			}
		});
		panel1.add(genkey1);
		
		frame.add(panel1);
		
		//Generate keys with primes file
		JLabel label2 = new JLabel(" Generate keys with primes file----------------------------------------------------------------------");
		frame.add(label2);
		
		JPanel panel2 = new JPanel();
		JLabel ch1 = new JLabel("Choose primes file: ");
		panel2.add(ch1);
		JButton choose1 = new JButton("Browse");
		//when choose a primes file, read them to arraylist of primes, and then randomly take 2 primes
		choose1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				primes = new ArrayList<String>();
				File selectedFile = null;
				
				JFileChooser file = new JFileChooser();
				int option = file.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					if(file.getSelectedFile() != null){
						selectedFile = file.getSelectedFile();
						try (BufferedReader bReader = new BufferedReader(new FileReader(selectedFile));)
						{
							String s;
							while((s = bReader.readLine()) != null){
								primes.add(s);
							}
						} catch (IOException ex){
							ex.printStackTrace();
						}
						Random rand = new Random();
						int pIndex = rand.nextInt(20);
						int qIndex;
						while(true){
							qIndex = rand.nextInt(20);
							if(qIndex != pIndex)
								break;
						}
						
						pvalue = new HugeInteger(primes.get(pIndex));
						qvalue = new HugeInteger(primes.get(qIndex));
					}
				}else{
					JOptionPane.showMessageDialog(null, "Please select a primes file");
				}
			}
		});
		panel2.add(choose1);
		JButton genkey2 = new JButton("Generate");
		//generate keys from 2 random prime numbers that are read from file
		genkey2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				generateKey(pvalue, qvalue);
			}
		});
		panel2.add(genkey2);
		
		frame.add(panel2);
		
		//Block message
		JLabel label3 = new JLabel(" Block message----------------------------------------------------------------------------------");
		frame.add(label3);
		
		JPanel panel3 = new JPanel();
		JLabel fi1 = new JLabel("Choose a text file: ");
		panel3.add(fi1);
		JButton choose2 = new JButton("Browse");
		//when choose a message file, read it to a stringbuilder
		choose2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				File selectedFile = null;
				message = new StringBuilder();
				
				JFileChooser file = new JFileChooser();
				int option = file.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					if(file.getSelectedFile() != null){
						selectedFile = file.getSelectedFile();
						ArrayList<String> res = new ArrayList<String>();
						try (BufferedReader bReader = new BufferedReader(new FileReader(selectedFile));)
						{
							String s;
							while((s = bReader.readLine()) != null){
								res.add(s);
							}
							for(int i = 0; i < res.size() - 1; i++){
								message.append(res.get(i));
								message.append("\n");
							}
							message.append(res.get(res.size() - 1));
						} catch (IOException ex){
							ex.printStackTrace();
						}
					}
				}else{
					JOptionPane.showMessageDialog(null, "Please select a text file");
				}
			}
		});
		panel3.add(choose2);
		JLabel size1 = new JLabel("Blocking size: ");
		panel3.add(size1);
		blockSize = new JTextField();
		blockSize.setColumns(5);
		panel3.add(blockSize);
		JButton block = new JButton("Block");
		//block the message into pre-selected size, write the blocked message to blockfile.txt
		block.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String sz = blockSize.getText();
				try{
					blkSize = Integer.parseInt(sz);
					if(blkSize > 0)
						blockMsg(message.toString(), blkSize);
					else
						JOptionPane.showMessageDialog(null, "Please input blocking size greater than 0.");
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Please input only numeric values.");
				}
			}
		});
		panel3.add(block);
		
		frame.add(panel3);
		
		//Unblock message
		JLabel label4 = new JLabel(" Unblock message--------------------------------------------------------------------------------");
		frame.add(label4);
		
		JPanel panel4 = new JPanel();
		JLabel fi2 = new JLabel("Choose a text file: ");
		panel4.add(fi2);
		JButton choose3 = new JButton("Browse");
		//when choose a file, read array of number to arraylist
		choose3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				stringList = new ArrayList<String>();
				File selectedFile = null;
				
				JFileChooser file = new JFileChooser();
				int option = file.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					if(file.getSelectedFile() != null){
						selectedFile = file.getSelectedFile();
						try (BufferedReader bReader = new BufferedReader(new FileReader(selectedFile));)
						{
							String s;
							while((s = bReader.readLine()) != null){
								stringList.add(s);
							}
						} catch (IOException ex){
							ex.printStackTrace();
						}
					}
				}else{
					JOptionPane.showMessageDialog(null, "Please select a text file");
				}
			}
		});
		panel4.add(choose3);
		JLabel size2 = new JLabel("Blocked size: ");
		panel4.add(size2);
		unBlockSize = new JTextField();
		unBlockSize.setColumns(5);
		panel4.add(unBlockSize);
		//unblock the file, store the message in unblock.txt
		JButton unBlock = new JButton("Unblock");
		unBlock.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String sz = unBlockSize.getText();
				try{
					unBlkSize = Integer.parseInt(sz);
					if(unBlkSize > 0)
						unBlock(stringList, unBlkSize);
					else
						JOptionPane.showMessageDialog(null, "Please input blocking size greater than 0.");
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Please input only numeric values.");
				}
			}
		});
		panel4.add(unBlock);
		
		frame.add(panel4);
		
		//Encrypt/Decrypt
		JLabel label5 = new JLabel(" Encrypt/Decrypt---------------------------------------------------------------------------------");
		frame.add(label5);
		
		JPanel panel5 = new JPanel();
		JLabel fi3 = new JLabel("Choose a text file: ");
		panel5.add(fi3);
		JButton choose4 = new JButton("Browse");
		//choose blocked file
		choose4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				stringList = new ArrayList<String>();
				File selectedFile = null;
				
				JFileChooser file = new JFileChooser();
				int option = file.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					if(file.getSelectedFile() != null){
						selectedFile = file.getSelectedFile();
						try (BufferedReader bReader = new BufferedReader(new FileReader(selectedFile));)
						{
							String s;
							while((s = bReader.readLine()) != null){
								stringList.add(s);
							}
						} catch (IOException ex){
							ex.printStackTrace();
						}
					}
				}else{
					JOptionPane.showMessageDialog(null, "Please select a text file");
				}
			}
		});
		panel5.add(choose4);
		JLabel fi4 = new JLabel("Choose a key file: ");
		panel5.add(fi4);
		JButton choose5 = new JButton("Browse");
		//choose a key file
		choose5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				File selectedFile = null;
				JFileChooser file = new JFileChooser();
				int option = file.showOpenDialog(null);
				if(option == JFileChooser.APPROVE_OPTION){
					if(file.getSelectedFile() != null){
						selectedFile = file.getSelectedFile();
					}
					readKey(selectedFile);
				}else{
					JOptionPane.showMessageDialog(null, "Please select a key file");
				}
			}
		});
		panel5.add(choose5);
		JButton ende = new JButton("Encrypt/Decrypt");
		//encrypt/decrypt, store the result in result.txt
		ende.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				endeCrypt(stringList, base, expo);
			}
		});
		panel5.add(ende);
		
		frame.add(panel5);
		
		frame.pack();
		frame.setVisible(true);
		
//End of setup the GUI
		
	}
	
	//generate key function, used RSA algorithm, store keys in privatekey.txt and publickey.txt
	public static void generateKey(HugeInteger p, HugeInteger q){
		HugeInteger n = new HugeInteger(p.multiply(q));
		HugeInteger m = new HugeInteger(p.subtract(ONE).multiply(q.subtract(ONE)));
		HugeInteger e = new HugeInteger("3");
		
		while(true){
			if(m.GCD(e).equalTo(ONE))
				break;
			e.add(TWO);
		}
		
		HugeInteger k = new HugeInteger(ONE);
		while(true){
			if(m.multiply(k).add(ONE).modulus(e).equalTo(ZERO))
				break;
			k.add(ONE);
		}
		
		HugeInteger d = new HugeInteger(m.multiply(k).add(ONE).divide(e));
		
		String nString = stringtify(n);
		String eString = stringtify(e);
		String dString = stringtify(d);
		
		writeXML(nString, eString, "publickey.txt", "evalue");
		writeXML(nString, dString, "privatekey.txt", "dvalue");
	}
	
	//function to stringtify a HugeInteger object to string
	public static String stringtify(HugeInteger h){
		StringBuilder sb = new StringBuilder();
		for(int i = 99; i >= 0; i--){
			sb.append(h.digits[i]);
		}
		
		int find = 0;
		for(int i = 0; i < 100; i++){
			if(sb.charAt(i) != '0'){
				find = i;
				break;
			}
		}
		if(find != 0)
			return sb.substring(find);
		else
			return "0";
	}
	
	//function to write an xml file, used to write the key
	public static void writeXML(String a, String b, String c, String d){
		try{
			DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
			DocumentBuilder dB = dBF.newDocumentBuilder();
			Document doc = dB.newDocument();
			
			Element rootTag = doc.createElement("rsakey");
			doc.appendChild(rootTag);
			
			Element fElem = doc.createElement(d);
			fElem.appendChild(doc.createTextNode(b));
			rootTag.appendChild(fElem);
			
			Element nvalue = doc.createElement("nvalue");
			nvalue.appendChild(doc.createTextNode(a));
			rootTag.appendChild(nvalue);
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(c));
			
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty(OutputKeys.METHOD, "xml");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");

			trans.transform(source, result);
		}
		catch (ParserConfigurationException ex1) {
			ex1.printStackTrace();
		} 
		catch (TransformerException ex2) {
			ex2.printStackTrace();
		}
	}
	
	//function to block a message, then store it to blockfile.txt
	public static void blockMsg(String sb, int size){
		ArrayList<String> bMsg = new ArrayList<String>();
		int m = sb.length()/size;
		int n = sb.length()%size;
		if(size > sb.length())
			bMsg.add(sb);
		else{
			for(int i = 0; i < m; i++)
				bMsg.add(sb.substring(i*size, (i+1)*size));
			if(n != 0)
				bMsg.add(sb.substring(m*size));
		}
		
		ArrayList<HugeInteger> nList = new ArrayList<HugeInteger>();
		for(int i = 0; i < bMsg.size(); i++)
			nList.add(new HugeInteger());
		
		for(int i = 0; i < bMsg.size(); i++){
			String s = bMsg.get(i);
			for(int j = 0; j < s.length(); j++){
				char c = s.charAt(j);
				int asciivalue = ((int) c) - 27;
				String x = Integer.toString(asciivalue);
				HugeInteger y = new HugeInteger(x);
				nList.get(i).add(y.multiply(HDR.power(j)));
			}
		}
		
		ArrayList<String> tem = new ArrayList<String>();
		for(int i = 0; i < nList.size(); i++)
			tem.add(stringtify(nList.get(i)));
		
		ArrayList<String> fin = new ArrayList<String>();
		for(String s : tem){
			if(s.length() == size*2){
				fin.add(s);
			}else{
				if(s.length() < size*2){
					int l = size*2 - s.length();
					StringBuilder temSB = new StringBuilder();
					for(int i = 0; i < l; i++){
						temSB.append("0");
					}
					temSB.append(s);
					fin.add(temSB.toString());
				}
			}
		}
		writeFile(fin, "blockfile.txt");
	}
	
	//function to unblock a file, store the message in unblock.txt
	public static void unBlock(ArrayList<String> sl, int size){
		ArrayList<HugeInteger> nList = new ArrayList<HugeInteger>();
		for(String s : sl)
			nList.add(new HugeInteger(s));
		
		StringBuilder strList = new StringBuilder();
		for(HugeInteger h : nList){
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < size; i++){
				HugeInteger remainder = new HugeInteger(h.modulus(HDR));
				if(!remainder.equalTo(ZERO)){
					String s = stringtify(remainder);
					sb.append((char)(Integer.parseInt(s) + 27));
				}
				h.assign(h.divide(HDR));
			}
			strList.append(sb.toString());
		}
		ArrayList<String> fin = new ArrayList<String>();
		fin.add(strList.toString());
		writeFile(fin, "unblock.txt");
	}
	
	//function to check if a number is prime
	public static boolean isPrime(long v){
		if(v < 2L)
			return false;
		else{
			if(v == 2L){
				return true;
			}else{
				if(v%2 == 0)
					return false;
				else{
					for(long i = 3L; i*i <= v; i += 2L){
						if(v%i == 0)
							return false;
					}
				}
				return true;
			}
		}
	}
	
	//function to read a xml key file
	public static void readKey(File f){
		try{
			DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
			DocumentBuilder dB = dBF.newDocumentBuilder();
			Document doc = dB.parse(f);
			doc.getDocumentElement().normalize();
			NodeList baseValue = doc.getElementsByTagName("nvalue");
			NodeList expoValue = null;
			if(f.getName().equals("publickey.txt")){
				expoValue = doc.getElementsByTagName("evalue");
			}else{
				if(f.getName().equals("privatekey.txt")){
					expoValue = doc.getElementsByTagName("dvalue");
				}
			}
			
			Node baseNode = baseValue.item(0);
			Node expoNode = expoValue.item(0);
			
			Element baseElem = (Element) baseNode;
			Element expoElem = (Element) expoNode;
			
			base = baseElem.getTextContent();
			expo = expoElem.getTextContent();
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Please input a key file");
			ex.printStackTrace();
		}
	}
	
	//function to encrypt/decrypt a file, used RSA Algorithm
	public static void endeCrypt(ArrayList<String> sl, String a, String b){
		HugeInteger n = new HugeInteger(a);
		HugeInteger expo = new HugeInteger(b);
		ArrayList<HugeInteger> numList = new ArrayList<HugeInteger>();
		for(String s: sl)
			numList.add(new HugeInteger(s));
		ArrayList<HugeInteger> endeList = new ArrayList<HugeInteger>();
		for(int i = 0; i < numList.size(); i++){
			endeList.add(new HugeInteger(ONE));
		}
		
		for(int i = 0; i < endeList.size(); i++){
			for(HugeInteger y = new HugeInteger(); y.lessThan(expo); y.add(ONE)){
				HugeInteger tmp = new HugeInteger(endeList.get(i).multiply(numList.get(i)).modulus(n));
				endeList.get(i).assign(tmp);
			}
		}
		
		ArrayList<String> sList = new ArrayList<String>();
		
		for(int i = 0; i < endeList.size(); i++){
			String str = stringtify(endeList.get(i));
			sList.add(str);
		}
		
		writeFile(sList, "result.txt");
	}
	
	//function to write string arraylist to file
	public static void writeFile(ArrayList<String> sl, String fileName){
		try{
			FileWriter fWriter = new FileWriter(fileName);
			for(int i = 0; i < sl.size() - 1; i++){
				fWriter.write(sl.get(i));
				fWriter.write("\n");
			}
			fWriter.write(sl.get(sl.size() - 1));
			fWriter.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
} //end of Main class
