Here is the source code for the `README.md` file with the proper markdown formatting for your **Sommier Converter** project:

```markdown
# Sommier Converter

The **Sommier Converter** is a tool designed to convert PDF documents into CSV format. It is ideal for users who need to extract structured data from PDF files and convert it into a CSV format for further analysis, processing, or integration into other systems.

This project is written in **Java** and utilizes libraries like **Apache PDFBox** for PDF processing and **OpenCSV** for CSV file generation.

## Features

- **PDF to CSV Conversion**: Convert structured PDF documents to CSV format with ease.
- **Custom PDF Parsing**: Handles custom PDF layouts and extracts relevant information.
- **Data Extraction**: Automatically extracts and organizes data into rows and columns based on predefined markers within the PDF document.
- **Simple Command-Line Interface (CLI)**: Easy to use with just a few commands to run.

## Requirements

Before running the project, make sure you have the following installed:

- **Java 8 or later**: The project is developed using Java.
- **Apache PDFBox**: For PDF parsing.
- **OpenCSV**: For handling CSV file output.

### Maven Dependencies

Make sure your Maven `pom.xml` includes the necessary dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>2.0.27</version>
    </dependency>
    <dependency>
        <groupId>com.opencsv</groupId>
        <artifactId>opencsv</artifactId>
        <version>5.6</version>
    </dependency>
</dependencies>
```

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/OUSSAMA-EZZIOURI/sommier_converter.git
    cd sommier_converter
    ```

2. Use **Maven** to build the project and resolve dependencies:

    ```bash
    mvn clean install
    ```

3. Ensure that you have **Java 8 or later** installed. You can check this by running:

    ```bash
    java -version
    ```

## Usage

### Convert PDF to CSV

To convert a PDF document into CSV, use the following command:

```bash
java -jar sommier_converter.jar input.pdf output.csv
```

Where:
- `input.pdf`: The PDF file you want to convert.
- `output.csv`: The destination CSV file where the extracted data will be saved.

### Configuration for Specific PDFs

If the PDF structure is customized, you may need to configure the extraction rules. Modify the relevant configuration files or tweak the code to adapt to your specific PDF format.

## Example

Assume we have a PDF file `customs_declaration.pdf`. To convert this PDF to CSV, use:

```bash
java -jar sommier_converter.jar customs_declaration.pdf customs_declaration.csv
```

The output CSV will contain rows and columns with extracted data from the PDF, such as item descriptions, quantities, and tariffs.

## Project Structure

The project is organized as follows:

```
sommier_converter/
│
├── src/                    # Java source files
│   ├── Main.java           # Main class for execution
│   ├── PdfParser.java      # PDF parsing logic
│   └── CsvWriter.java      # Logic for writing CSV
│
├── lib/                    # External libraries
│
└── pom.xml                 # Maven configuration file
```

## Troubleshooting

- **PDF Parsing Issues**: If the converter fails to parse the PDF, ensure that the PDF is in a consistent format. The tool might not work well with scanned images or PDFs that don’t have text-based content.
- **CSV Formatting**: If the output CSV is not formatted as expected, check the delimiters and structure in the input PDF and adjust the parsing logic as necessary.

## Contributing

Contributions are welcome! If you'd like to help improve the project, you can:
- Fork the repository.
- Create a branch for your changes.
- Submit a pull request with your improvements or fixes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For questions or feedback, feel free to reach out to **Oussama Ezziouri** at [oussama.ezziouri@example.com](mailto:oussama.ezziouri@example.com).
```

You can copy and paste the above code into a `README.md` file in the root of your project directory. This markdown file will render with proper formatting on GitHub or any markdown viewer, ensuring clarity and easy understanding of the project structure and instructions.