package eg.edu.alexu.csd.oop.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DatabaseM implements Database {
	public String path, dbName;
	private boolean firstCreated = false;
	private Parser parser = new Parser();

	public DatabaseM() {
		File f = new File("Database589");
		f.mkdir();
		this.path = "Database589/";
	}

	private void write(String name) {
		try {
			BufferedReader reading = new BufferedReader(new FileReader(path
					+ "dataBases.txt"));
			String current = "";
			String append;
			while ((append = reading.readLine()) != null) {
				current += append;

			}
			current += "," + name + ",";
			PrintWriter pw = new PrintWriter(path + "dataBases.txt", "UTF-8");
			pw.write(current);
			pw.close();
			reading.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean createDatabase(String databaseName) {
		dbName = databaseName;
		write(databaseName);
		File newFolder = new File(path + databaseName);
		newFolder.mkdir();
		firstCreated = true;
		return true;
	}

	private void creatSchema() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("schemas");
			Element privacy = doc.createElement("privacy");
			doc.appendChild(root);
			root.appendChild(privacy);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path + dbName
					+ "\\" + "Schemas"));
			transformer.transform(source, result);
			firstCreated = false;
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private boolean checkDB(String name) { // check 
		String current;
		File file = new File(path + "dataBases.txt");
		try {
			if (!file.createNewFile()) {
				BufferedReader reading = new BufferedReader(
						new FileReader(file));
				while ((current = reading.readLine()) != null) {
					if (current.contains("," + name + ",")) {
						reading.close();
						return true;
					}
				}
				reading.close();
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// /////////////// Drop database
	public void deleteFolder(File file) throws IOException { // delete directory

		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
				System.out.println("Directory is deleted : " + file.getAbsolutePath());
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					deleteFolder(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					System.out.println("Directory is deleted : " + file.getAbsolutePath());
				}
			}
		} else {
			// if file, then delete it
			file.delete();
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}

	public boolean dropDatabase(String databaseName) {
		try {
			BufferedReader reading = new BufferedReader(new FileReader(path + "dataBases.txt"));
			String current = "";
			String append;
			while ((append = reading.readLine()) != null) {
				current += append;
			}
			if (current.contains(databaseName)) {
				current = current.replaceAll("," + databaseName + ",", "");
			}
			PrintWriter pw = new PrintWriter(path + "dataBases.txt", "UTF-8");
			pw.write(current);
			pw.close();
			reading.close();

			File directory = new File(path + databaseName);
			deleteFolder(directory);
			if (current == null || current.equals("")) {
				File f = new File(path + "dataBases.txt");
				f.delete();
			}
			dbName = null;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// //////////////////// Drop Table
	public boolean dropTable(String tableName) {

		try {
			File directory = new File(path + dbName + "\\" + tableName);
			deleteFolder(directory);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	// //////////////// Create Table
	private boolean checkTable(String name)
			throws ParserConfigurationException, SAXException, IOException {
		if (dbName == null) {
			throw new RuntimeException();
		}
		File fXmlFile = new File(path + dbName + "/" + "Schemas");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc2 = dBuilder.parse(fXmlFile);

		doc2.getDocumentElement().normalize();
		NodeList list1 = doc2.getElementsByTagName("schemas");
		NodeList list2 = list1.item(0).getChildNodes();
		for (int i = 0; i < list2.getLength(); i++) {
			Node a = list2.item(i);
			if (a.getNodeName().equals(name))
				return true;
		}
		return false;
	}

	public void createElement(Element parent, Document doc, String value,
			Element parent2, Document doc1, String data) {
		createElement(parent, doc, value, data);
		createElement(parent2, doc1, value, data);
	}

	public void createElement(Element parent, Document doc, String value,
			String data) {
		Element temp = doc.createElement(value);
		temp.appendChild(doc.createTextNode(data));
		parent.appendChild(temp);
	}

	public Document createDoc(String file_path)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dF = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbb = dF.newDocumentBuilder();
		Document doc1 = dbb.parse(new File(file_path));
		return doc1;
	}

	public Document createDoc() throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory dF = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbb = dF.newDocumentBuilder();
		Document doc1 = dbb.newDocument();
		return doc1;
	}

	public void transform(Document doc, String file_path)
			throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(file_path));
		transformer.transform(source, result);
	}

	public boolean createTable(String name, String[] attrs, String[] types) throws ParserConfigurationException, TransformerException,
			JAXBException, IOException, SAXException {
		if (dbName != null) {
			if (firstCreated)
				creatSchema();
			if (true) {
				String attrs1[] = new String[attrs.length + 1];
				String typesb[] = new String[types.length + 1];

				for (int i = 1; i < typesb.length; i++) {
					attrs1[i] = attrs[i - 1];
					typesb[i] = types[i - 1];
				}
				attrs = attrs1;
				types = typesb;
				File newFolder = new File(path + dbName + "\\" + name);
				newFolder.mkdir();
				Document doc1 = createDoc(path + dbName + "\\" + "Schemas");
				Element table = doc1.createElement(name);
				doc1.getDocumentElement().appendChild(table);
				Document doc = createDoc();
				Element parentnode = doc.createElement("Table");
				doc.appendChild(parentnode);
				Element types1 = doc1.createElement("attributes");
				table.appendChild(types1);
				Element types2 = doc.createElement("attributes");
				parentnode.appendChild(types2);
				for (int i = 1; i < attrs.length; i++) {
					Element mytypes = doc.createElement(attrs[i].toLowerCase());
					Element mytypes1 = doc1.createElement(attrs[i]
							.toLowerCase());
					createElement(mytypes, doc, "type", mytypes1, doc1,
							types[i]);
					types2.appendChild(mytypes);
					types1.appendChild(mytypes1);
				}
				transform(doc, path + dbName + "\\" + name + "\\" + name);
				transform(doc1, path + dbName + "\\" + "Schemas");
				return true;
			} else
				return false;
		} else {
			throw new RuntimeException();
		}
	}

	public boolean validate(String[] attrs, String[] data, String tablename)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = createDoc(tablename);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("attributes");
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < nList.getLength(); j++) {
				Node node = nList.item(j);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element temp = (Element) node;
					String see = temp
							.getElementsByTagName(attrs[i].toLowerCase())
							.item(0).getChildNodes().item(0).getTextContent();
					if (see.equals(null))
						return false;
					else {
						try {
							switch (see) {

							case "int":
								Integer.parseInt(data[i]);
								break;
							case "double":
								Double.parseDouble(data[i]);
								break;
							case "varchar":
							case "string":
								data[i].length();
								break;
							case "boolean":
								Boolean.getBoolean(data[i]);
								break;
							case "float":
								Float.parseFloat(data[i]);
								break;
							}
						} catch (Exception e) {
							System.out.println("error valid type");
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public ArrayList<String> getFields(String name)
			throws ParserConfigurationException, SAXException, IOException {
		ArrayList<String> arr = new ArrayList<String>();
		File fXmlFile = new File(path + dbName + "\\Schemas");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		System.out.println(dbName);
		System.out.println(fXmlFile);
		Document doc2 = dBuilder.parse(fXmlFile);

		doc2.getDocumentElement().normalize();
		NodeList list1 = doc2.getElementsByTagName("schemas");
		NodeList list2 = list1.item(0).getChildNodes();
		for (int i = 0; i < list2.getLength(); i++) {
			if (name.equalsIgnoreCase(list2.item(i).getNodeName())) {
				NodeList nlist3 = list2.item(i).getChildNodes().item(0)
						.getChildNodes();
				for (int j = 0; j < nlist3.getLength(); j++) {
					arr.add(nlist3.item(j).getNodeName());
				}
				break;
			}
		}
		return arr;
	}

	private int lastRow(String tablename) throws ParserConfigurationException,
			SAXException, IOException {
		Document doc = createDoc(tablename);
		NodeList list = doc.getElementsByTagName("Entry");
		return list.getLength();
	}

	public String[] fill_Arr(String tablename, ArrayList<String> arr,
			String attrs[], String[] data) throws ParserConfigurationException,
			SAXException, IOException {
		String[] retur = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			boolean already_exists = false;

			for (int j = 0; j < attrs.length; j++) {
				if (attrs[j].equalsIgnoreCase(arr.get(i))) {
					already_exists = true;
					retur[i] = data[j];
					break;
				}
			}
			if (!already_exists)
				retur[i] = "null";
		}
		return retur;
	}

	public boolean checkFields(String name, String[] fields)
			throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(path + dbName + "\\Schemas");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc2 = dBuilder.parse(fXmlFile);

		doc2.getDocumentElement().normalize();
		NodeList list1 = doc2.getElementsByTagName("schemas");
		NodeList list2 = list1.item(0).getChildNodes();
		for (int i = 0; i < list2.getLength(); i++) {
			if (name.equalsIgnoreCase(list2.item(i).getNodeName())) {
				NodeList nlist3 = list2.item(i).getChildNodes().item(0)
						.getChildNodes();
				for (int j = 0; j < fields.length; j++) {
					boolean found = false;
					for (int j2 = 0; j2 < nlist3.getLength(); j2++) {
						if (fields[j].equalsIgnoreCase(nlist3.item(j2)
								.getNodeName()))
							found = true;
					}
					if (!found)
						return false;
				}
				break;
			}
		}
		return true;
	}

	

	
	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		// TODO Auto-generated method stub
		boolean exist = checkDB(databaseName);
		if (exist == false) { 
			// create no matter dropIfExists
			// return this.createDatabase(databaseName);
			try {
				executeStructureQuery("create database " + databaseName);
			} catch (SQLException e) {
			}
		}
		if (dropIfExists == true && exist == true) {
			// drop then create
			try {
				executeStructureQuery("drop database " + databaseName);
				executeStructureQuery("create database " + databaseName);
			} catch (SQLException e) {
			}
		}
		if (exist == true) {
			dbName = databaseName;
		}
		return (path + dbName);
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		if (parser.validate_createDatabase(query) != null) {
			String databaseName = parser.validate_createDatabase(query);
			checkDB(databaseName);
			return this.createDatabase(databaseName);
		} else if (parser.validate_dropDatabase(query) != null) {
			String databaseName = parser.validate_dropDatabase(query);
			return this.dropDatabase(databaseName);
		} else if (parser.validate_dropTable(query) != null) {
			String tableName = parser.validate_dropTable(query);
			return this.dropTable(tableName);
		} else if (!parser.validate_createTable(query).isEmpty()) {
			Map<String, ArrayList<String>> map = parser.validate_createTable(query);
			ArrayList<String> arrList = map.get("datatypes");
			String[] types = new String[arrList.size()];
			for (int i = 0; i < arrList.size(); i++) {
				types[i] = arrList.get(i).toLowerCase();
			}
			arrList = map.get("columns");
			String[] attrs = new String[arrList.size()];
			for (int i = 0; i < arrList.size(); i++) {
				attrs[i] = arrList.get(i).toLowerCase();
			}
			try {
				File ff = new File(path + dbName+ "//" + map.get("tablename").get(0) + ".txt");
				if(ff.exists()) return false;
				boolean s = createTable(map.get("tablename").get(0), attrs, types);
				PrintWriter pw = new PrintWriter(path + dbName+ "//"+map.get("tablename").get(0)+".txt", "UTF-8");
				return s;
			} catch (JAXBException | IOException | SAXException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		}else
		   throw new SQLException();
		return false;
	}


	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		Select sel = new Select(dbName);
		if (!parser.validate_selectAllWithCond(query).isEmpty()) {
			Map<String, ArrayList<String>> map = parser.validate_selectAllWithCond(query);
			String name = map.get("tablename").get(0);
			String selected = map.get("condition").get(0);
			String[] goal = selected.split(" ");
			try {
				return sel.selectConditionAll(name, goal[0], goal[1], Integer.parseInt(goal[2]));
			} catch (NumberFormatException | DOMException
					| ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (parser.validate_selectAll(query) != null) {
			String databaseName = parser.validate_selectAll(query);
			try {
				return sel.selectAll(databaseName);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!parser.validate_select(query).isEmpty()) {	
			Map<String, ArrayList<String>> map = parser.validate_select(query);
			if (map.get("condition") == null) { // select column no condition  
				try {
					return sel.selectCol(map.get("tablename").get(0), map.get("columnname").get(0));
				} catch (ParserConfigurationException | SAXException
						| IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else { // select under condition
				String name = map.get("tablename").get(0);
				String selec = map.get("columnname").get(0);
				String selected = map.get("condition").get(0);
				// System.out.println("select under condition");
				String[] arr = selected.split(" ");
				try {
					return sel.selectCondition(name, arr[1], arr[0], Integer.parseInt(arr[2]), selec);
				} catch (DOMException | ParserConfigurationException
						| SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return null;
	}
	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		// TODO Auto-generated method stub
		if (!parser.validate_insert(query).isEmpty()) {
			Insert ins = new Insert(dbName);
			return ins.insert(query);
		} else if (!parser.validate_update(query).isEmpty()) {
			Update up = new Update(dbName);
			return up.update(query);
		} else if (!parser.validate_deleteWithCond(query).isEmpty()) {
			Map<String, ArrayList<String>> map = parser.validate_deleteWithCond(query);
			String name = map.get("tablename").get(0);
			String cond = map.get("condition").get(0);
			Delete del = new Delete(dbName);
			try {
				return del.deleteWith(name, cond.toLowerCase());
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (parser.validate_deleteWithoutCond(query) != null) {
			String name = parser.validate_deleteWithoutCond(query);
			Delete del = new Delete(dbName);
			try {
				return del.deleteWithout(name);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

}
