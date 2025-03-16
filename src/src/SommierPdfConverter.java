package src;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.mozilla.universalchardet.UniversalDetector;

public class SommierPdfConverter extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public final JButton btn_pdf_folder = new JButton("Choose...");
	public static JTextField textField_pdf_folder;
	public static JTextField textField_csv_folder;
	public static JTextArea textArea_log;
	public static JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SommierPdfConverter frame = new SommierPdfConverter();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SommierPdfConverter() {
		setResizable(false);
		setTitle("Sommier PDF Converter (by Oussama EZZIOURI)");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 658, 508);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		progressBar = new JProgressBar();
		progressBar.setBounds(7, 165, 625, 23);
		contentPane.add(progressBar);

		JLabel lblNewLabel = new JLabel("PDF files source folder");
		lblNewLabel.setBounds(7, 11, 157, 14);
		contentPane.add(lblNewLabel);

		btn_pdf_folder.setBounds(543, 32, 89, 23);
		contentPane.add(btn_pdf_folder);

		textField_pdf_folder = new JTextField();
		textField_pdf_folder.setEditable(false);
		textField_pdf_folder.setBounds(7, 33, 526, 20);
		contentPane.add(textField_pdf_folder);
		textField_pdf_folder.setColumns(10);

		JLabel lblCsvFilesFolder = new JLabel("CSV Files destination folder");
		lblCsvFilesFolder.setBounds(7, 66, 157, 14);
		contentPane.add(lblCsvFilesFolder);

		textField_csv_folder = new JTextField();
		textField_csv_folder.setEditable(false);
		textField_csv_folder.setColumns(10);
		textField_csv_folder.setBounds(7, 88, 526, 20);
		contentPane.add(textField_csv_folder);

		JButton btn_csv_folder = new JButton("Choose...");
		btn_csv_folder.setBounds(543, 87, 89, 23);
		contentPane.add(btn_csv_folder);

		JButton btn_convert = new JButton("Convert");
		btn_convert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				pdf_to_text();
				text_to_csv();

			}
		});
		btn_convert.setBounds(7, 134, 89, 23);
		contentPane.add(btn_convert);

		textArea_log = new JTextArea();
		textArea_log.setEditable(false);
		textArea_log.setWrapStyleWord(true);
		textArea_log.setLineWrap(true); // ✅ Ensures long text wraps properly

		// ✅ Add scrollbars (vertical always, horizontal never)
		JScrollPane scrollPane_1 = new JScrollPane(textArea_log, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(7, 204, 625, 254);
		contentPane.add(scrollPane_1);

		textArea_log.setWrapStyleWord(true);
		textArea_log.setRows(14);
		textArea_log.setEditable(false);
		textArea_log.setColumns(70);

		JButton btnClearLog = new JButton("Clear Log");
		btnClearLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea_log.setText("");
			}
		});
		btnClearLog.setBounds(543, 131, 89, 23);
		contentPane.add(btnClearLog);

		btn_pdf_folder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Get the JAR directory
				File jarDir = null;
				String runDir = new File("").getAbsolutePath();
				try {
					jarDir = new File(runDir);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				System.out.println("Select PDF source folder path...");
				textArea_log.append("Select PDF source folder path..." + System.lineSeparator());

				// Initialize JFileChooser with JAR directory
				JFileChooser filechooser = new JFileChooser(jarDir);

				// Set the file chooser to select directories (folders) only
				filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// Select PDF source folder
				int response = filechooser.showOpenDialog(null);

				// Check if the user approved the selection
				if (response == JFileChooser.APPROVE_OPTION) {
					// Get the selected folder
					File selectedFolder = filechooser.getSelectedFile();

					// Print the absolute path of the selected folder
					textField_pdf_folder.setText(selectedFolder.getAbsolutePath());
					textArea_log.append(selectedFolder.getAbsolutePath() + System.lineSeparator());
					System.out.println("Selected folder: " + selectedFolder.getAbsolutePath());
				} else {
					System.out.println("No folder selected.");
					textArea_log.append("No folder selected." + System.lineSeparator());
				}

			}
		});

		btn_csv_folder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Get the JAR directory
				File jarDir = null;
				String runDir = new File("").getAbsolutePath();
				try {
					jarDir = new File(runDir);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				System.out.println("Select CSV destination folder path...");
				textArea_log.append("Select CSV destination folder path..." + System.lineSeparator());

				// Initialize JFileChooser with JAR directory
				JFileChooser filechooser = new JFileChooser(jarDir);

				// Set the file chooser to select directories (folders) only
				filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// Select PDF source folder
				int response = filechooser.showOpenDialog(null);

				// Check if the user approved the selection
				if (response == JFileChooser.APPROVE_OPTION) {
					// Get the selected folder
					File selectedFolder = filechooser.getSelectedFile();

					// Print the absolute path of the selected folder
					textField_csv_folder.setText(selectedFolder.getAbsolutePath());
					textArea_log.append(selectedFolder.getAbsolutePath() + System.lineSeparator());
					System.out.println("Selected folder: " + selectedFolder.getAbsolutePath());
				} else {
					System.out.println("No folder selected.");
					textArea_log.append("No folder selected." + System.lineSeparator());
				}

			}
		});

	}
	
	public static String get_current_dir() {
		
	        // Get the path of the JAR file
	        String jarPath = SommierPdfConverter.class.getProtectionDomain().getCodeSource().getLocation().getPath();

	        // Convert the path to a File object
	        File jarFile = new File(jarPath);

	        // Get the parent directory of the JAR file
	        String jarDirectory = jarFile.getParent();
	        
	        return jarDirectory;	        
	    
	}
	
	public static void pdf_to_text() {

	    // Default input folder path
	    String inputFolderPath = get_current_dir() + File.separator + "pdf";
	    
	    // Use path from textField if not empty
	    if (!textField_pdf_folder.getText().isEmpty()) {
	        inputFolderPath = textField_pdf_folder.getText();
	    }

	    // Create File object for input folder
	    File inputFolder = new File(inputFolderPath);

	    // Check if the input folder exists and is a directory
	    if (!inputFolder.exists() || !inputFolder.isDirectory()) {
	        System.out.println("The specified input path is not a valid directory.");
	        textArea_log.append("The specified input path is not a valid directory." + System.lineSeparator());
	        return;
	    }
	    
	    // Default output folder path
	    String outputFolderPath = "./txt";
	    
	    // Create File object for output folder
	    File outputFolder = new File(outputFolderPath);

	    // Create the output folder if it doesn't exist
	    if (!outputFolder.exists()) {
	        boolean created = outputFolder.mkdirs();
	        if (created) {
	            System.out.println("Output folder created at: " + outputFolderPath);
	            textArea_log.append("Output folder created at: " + outputFolderPath + System.lineSeparator());
	        } else {
	            System.out.println("Failed to create output folder.");
	            textArea_log.append("Failed to create output folder." + System.lineSeparator());
	            return;
	        }
	    }

	    System.out.println("inputFolderPath : " + inputFolderPath);
	    System.out.println("outputFolderPath : " + outputFolderPath);

	    // Create a FileFilter to list only PDF files
	    FileFilter pdfFilter = file -> {
	        String name = file.getName().toLowerCase();
	        return file.isFile() && name.endsWith(".pdf");
	    };

	    // List all PDF files in the input folder
	    File[] pdfFiles = inputFolder.listFiles(pdfFilter);
	    int totalFiles = pdfFiles.length;
	    int progress = 0;

	    // Check if there are any PDF files in the folder
	    if (pdfFiles == null || pdfFiles.length == 0) {
	        System.out.println("No PDF files found in the folder: " + inputFolderPath);
	        textArea_log.append("No PDF files found in the folder: " + inputFolderPath + System.lineSeparator());
	        return;
	    }

	    // Loop through each PDF file
	    for (File pdfFile : pdfFiles) {
	        System.out.println("-----------------");
	        textArea_log.append("-----------------" + System.lineSeparator());
	        System.out.println("Processing file: " + pdfFile.getName());
	        textArea_log.append("Processing file: " + pdfFile.getName() + System.lineSeparator());

	        // Generate the output file name
	        String outputFileName = pdfFile.getName().replace(".pdf", ".txt");
	        String outputFilePath = outputFolderPath + File.separator + outputFileName;

	        try (PDDocument document = PDDocument.load(pdfFile)) {
	            PDFTextStripper pdfStripper = new PDFTextStripper();

	            // Create a BufferedWriter with UTF-8 encoding
	            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8))) {

	                // Loop through each page
	                for (int page = 1; page <= document.getNumberOfPages(); page++) {
	                    pdfStripper.setStartPage(page);
	                    pdfStripper.setEndPage(page);

	                    String text = pdfStripper.getText(document);

	                    // Write the extracted text to the output file in UTF-8 encoding
	                    writer.write(text);
	                }

	                System.out.println("Text extracted and saved to: " + outputFilePath);
	                textArea_log.append("Text extracted and saved to: " + outputFilePath + System.lineSeparator());
	                System.out.println("-----------------");
	            } catch (IOException e) {
	                System.err.println("Error writing to file: " + outputFilePath);
	                textArea_log.append("Error writing to file: " + outputFilePath + System.lineSeparator());
	                e.printStackTrace();
	            }

	        } catch (IOException e) {
	            System.err.println("Error processing file: " + pdfFile.getName());
	            textArea_log.append("Error processing file: " + pdfFile.getName() + System.lineSeparator());
	            e.printStackTrace();
	        }

	        // ✅ Update progress bar
	        progress += (100 / totalFiles);
	        progressBar.setValue(progress);
	    }

	    // Ensure progress bar reaches 100% after completion
	    progressBar.setValue(100);
	}


	public static void text_to_csv() {
		// Paths for input and output folders
		
		// Get the JAR directory
		
		String outputFolderPath = textField_csv_folder.getText()+  File.separator;
		String inputFolderPath =  "./txt" ;
		
		System.out.println("inputFolderPath "+inputFolderPath);//

		// Regex patterns
		String regex1 = "(\\d{3}\\s+\\d{3}\\s+\\d{4}\\s+\\d{7}\\s+[A-Z])"; // COMPTE
		String regex2 = "(\\d{10})\\s+(\\d{1,4})\\s+((?:\\d{1,3}[\\s\\u00A0,]?)*\\d{3})\\s+(NB|M|KG.|KG|KG. NET|ROULEAU|RUCHE|ROULE)\\s+((?:\\d{1,3}[\\s\\u00A0,]?)*\\d{3})";
		//String regex2 = "(\\d{10})\\s+(\\d{1,4})\\s+((?:\\d{1,3}[\\s\\u00A0,]?)*\\d{3})\\s+\\w{1,3}\\s+((?:\\d{1,3}[\\s\\u00A0,]?)*\\d{3})";
		String regex3 = "(\\d{2}/\\d{2}/\\d{4})";  // Date format: dd/MM/yyyy
		String regex4 = "(.*\\d{2}/\\d{2}/\\d{4})";  // Date format: dd/MM/yyyy

		// Ensure output folder exists
		File outputFolder = new File(outputFolderPath);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}

		// Get files
		File inputFolder = new File(inputFolderPath);
		File[] files = inputFolder.listFiles();
		if (files == null || files.length == 0) {
			System.out.println("No files found in: " + inputFolderPath);
			textArea_log.append("No files found in: " + inputFolderPath + System.lineSeparator());
			return;
		}

		int totalFiles = files.length;
		int progress = 0;

		for (File file : files) {
			if (file.isDirectory() || !file.getName().endsWith(".txt")) {
				continue;
			}

			System.out.println("Processing: " + file.getName());
			textArea_log.append("Processing: " + file.getName() + System.lineSeparator());

			 // Declare encoding variable outside try block
	        String detectedEncoding = "Unknown";

	        try {
	            detectedEncoding = detectEncoding(file);
	            if (!StandardCharsets.UTF_8.name().equalsIgnoreCase(detectedEncoding)) {
	                System.err.println("Encoding Mismatch in file: " + file.getName() +
	                        " | Detected: " + detectedEncoding + " | Required: UTF-8");
	                textArea_log.append("Encoding Mismatch in file: " + file.getName() +
	                        " | Detected: " + detectedEncoding + " | Required: UTF-8" + System.lineSeparator());
	            }
	        } catch (IOException e) {
	            System.err.println("Failed to detect encoding for: " + file.getName());
	            textArea_log.append("Failed to detect encoding for: " + file.getName() + System.lineSeparator());
	            continue;
	        }

			String outputFileName = file.getName().replace(".txt", ".csv");
			String outputFilePath = outputFolderPath + File.separator + outputFileName;

			try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
					BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath),
							StandardCharsets.UTF_8)) {

				// Write CSV header
				writer.write("COMPTE;NOMENCLATURE;ORDR;QUANTITE;UNITE;VALEUR DU BRUT;POIDS;DATE OUVERTURE;DATE ECHEANCE");
				writer.newLine();

				String line;
				String compte = null;
				String poids = "";
				String dateOuverture = "";
				String dateEcheance = "";
				
				int lineNumber = 0;  // To track line numbers
				
				//System.out.println("Line num : "+lineNumber);

				while ((line = reader.readLine()) != null) {
					
					lineNumber++;  // Increment line number
					//System.out.println("Line num : "+lineNumber);
					line = line.trim();

					// Extract COMPTE
					if (compte == null) {
						Matcher matcher1 = Pattern.compile(regex1).matcher(line);
						if (matcher1.find()) {
							compte = matcher1.group(1).trim();
							System.out.println("Found COMPTE: " + compte);
						}else {
							System.out.println("Compte not found in line " + line);
						}
					}
					
					// Extract DATE OUVERTURE only in lines 32
	                if (lineNumber == 32) {
	                	
	                    Matcher matcher3 = Pattern.compile(regex3).matcher(line);
	                    if (matcher3.find()) {
	                    	 String extractLine = matcher3.group(0).trim(); // Extract the full date line
	                    	 dateOuverture = extractLine.replaceAll(".*?(\\d{2}/\\d{2}/\\d{4}).*", "$1"); // Extract just the date part
	                         System.out.println("Found DATE OUVERTURE: " + dateOuverture);
	                         textArea_log.append("Found DATE OUVERTURE: " + dateOuverture + System.lineSeparator());	                        	                        
	                    }else {
	                    	System.out.println("Line mismatch regex3 : ["+line+"]");
	                    }
	                }
					
					// Extract DATE ECHEANCE only between lines 37 and 40
	                if (lineNumber >= 37 && lineNumber <= 40) {
	                	
	                    Matcher matcher4 = Pattern.compile(regex4).matcher(line);
	                    if (matcher4.find()) {
	                    	 String extractLine = matcher4.group(0).trim(); // Extract the full date line
	                    	 dateEcheance = extractLine.replaceAll(".*?(\\d{2}/\\d{2}/\\d{4}).*", "$1"); // Extract just the date part
	                         System.out.println("Found DATE ECHEANCE: " + dateEcheance);
	                         textArea_log.append("Found DATE ECHEANCE: " + dateEcheance + System.lineSeparator());	                        
	                        System.out.println("Found DATE ECHEANCE: " + dateEcheance);
	                    }else {
	                    	System.out.println("Line mismatch regex3 : ["+line+"]");
	                    }
	                }

					// Extract structured data
					Matcher matcher2 = Pattern.compile(regex2).matcher(line);
					while (matcher2.find()) {
						String nomenclature = matcher2.group(1);
						String ordr = matcher2.group(2);
						String quantite = matcher2.group(3).replace("\u00A0", " "); // Handle non-breaking spaces
						String unite = matcher2.group(4);
						String valeurBrut = matcher2.group(5).replace("\u00A0", " ");

						try {
							// Normalize numbers: Remove spaces, replace commas with dots
							quantite = quantite.replace(" ", "").replace(",", ".");
							valeurBrut = valeurBrut.replace(" ", "").replace(",", ".");

							// Convert to float
							double quantiteFloat = Double.parseDouble(quantite);
							double valeurBrutFloat = Double.parseDouble(valeurBrut);

							// Print extracted values
							System.out.println("Line "+lineNumber+ " Extracted: " + compte + " | " + nomenclature + " | " + ordr + " | "
									+ quantiteFloat + " | " + unite + " | " + valeurBrutFloat + " | " + poids + " | " + dateOuverture + " | " + dateEcheance + System.lineSeparator());
							textArea_log.append("Line "+lineNumber+" Extracted: " + compte + " | " + nomenclature + " | " + ordr + " | "
									+ quantiteFloat + " | " + unite + " | " + valeurBrutFloat + " | " + poids + " | " + dateOuverture + " | " + dateEcheance + System.lineSeparator());

							// Write to CSV with the detected dateEcheance
	                        writer.write(String.format("%s;%s;%s;%.4f;%s;%.4f;%s;%s;%s", compte, nomenclature, ordr,
	                                quantiteFloat, unite, valeurBrutFloat, poids, dateOuverture, dateEcheance));
							writer.newLine();
						} catch (NumberFormatException e) {
							System.err.println("Error parsing number: " + e.getMessage());
							textArea_log.append("Error parsing number: " + e.getMessage() + System.lineSeparator());
						}
					}
				}

				System.out.println("CSV created: " + outputFilePath);

			} catch (MalformedInputException e) {
				 System.err.println("Encoding issue in file: " + file.getName() +
		                    " | Detected: " + detectedEncoding + " | Required: UTF-8");
		            textArea_log.append("Encoding issue in file: " + file.getName() +
		                    " | Detected: " + detectedEncoding + " | Required: UTF-8" + System.lineSeparator());
	        } catch (IOException e) {
	            System.err.println("Error processing: " + file.getName());
	            e.printStackTrace();
	        }

			// ✅ Update progress bar
			progress += (100 / totalFiles);
			progressBar.setValue(progress);
		}

		// Ensure progress bar reaches 100% after completion
		progressBar.setValue(100);
	}

	public static String detectEncoding(File file) throws IOException {
		byte[] buffer = new byte[4096];
		UniversalDetector detector = new UniversalDetector(null);

		try (FileInputStream fis = new FileInputStream(file)) {
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) > 0 && !detector.isDone()) {
				detector.handleData(buffer, 0, bytesRead);
			}
		}
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();

		return encoding != null ? encoding : "Unknown Encoding";
	}
}
