package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Insert {
	private DatabaseM info = new DatabaseM();
	private Parser parser = new Parser();
	private String dbbName;
	
	public Insert(String db) {
		this.dbbName = db;
		info.dbName = db;
	}
	
	public int insert(String query){
		Map<String, ArrayList<String>> map = parser.validate_insert(query);
		ArrayList<String> arrList = map.get("values");
		String[] values = new String[arrList.size()];
		for (int i = 0; i < arrList.size(); i++) {
			values[i] = arrList.get(i).toLowerCase();
		}
		try {
			if (map.get("columns") != null) {
				ArrayList<String> arrLis = map.get("columns");
				String[] attrs = new String[arrLis.size()];
				for (int i = 0; i < arrLis.size(); i++) {
					attrs[i] = arrLis.get(i).toLowerCase();
				}
				return this.insertIntoTable(map.get("tablename").get(0), attrs, values);
			} else {
				return this.insertWithout(map.get("tablename").get(0), values);
			}
		} catch (ParserConfigurationException | TransformerException
				| IOException | SAXException e) {
			// throw new SQLException();
			e.printStackTrace();
		}
		return 0;
	}
   /////////////////////////////// insert into table with condition "into determined column"
	public int insertIntoTable(String tablename, String[] attrs, String[] data)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		if (true) {
			if (info.checkFields(tablename, attrs)) {
				String name = tablename;
				tablename = info.path + info.dbName + "\\" + tablename + "\\" + tablename;
				if (info.validate(attrs, data, tablename)) {
					ArrayList<String> arr = info.getFields(name);
					if (arr.size() >= attrs.length) {
						data = info.fill_Arr(tablename, arr, attrs, data);
						attrs = new String[arr.size()];
						for (int i = 0; i < arr.size(); i++)
							attrs[i] = arr.get(i);
					}
					Document doc = info.createDoc(tablename);
					Element toadd = doc.createElement("Entry");
					for (int i = 0; i < data.length; i++) {
						info.createElement(toadd, doc, attrs[i], data[i]);
					}
					doc.getDocumentElement().appendChild(toadd);
					info.transform(doc, tablename);
				}
				return 1;
			}
		}
		return 0;
	}
	////////////////////// insert with out condition
	public int insertWithout(String tablename, String[] data)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException {
		if (true) {
			String name = tablename;
			tablename = info.path + info.dbName + "\\" + tablename + "\\" + tablename;
			ArrayList<String> arr = info.getFields(name);
			Document doc = info.createDoc(tablename);
			Element toadd = doc.createElement("Entry");
			for (int i = 0; i < data.length; i++) {
				info.createElement(toadd, doc, arr.get(i), data[i]);
			}
			doc.getDocumentElement().appendChild(toadd);
			info.transform(doc, tablename);
		}
		return 1;

	}
}
