package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Delete {
	private DatabaseM info = new DatabaseM();
	private Parser parser = new Parser();
	private String [] selected ;
	private String [] types ;
	private String dbbName;
	
	 public Delete(String db) {
		// TODO Auto-generated constructor stub
		this.dbbName = db;
		info.dbName = db;
	}
	 
	private void get(String name) throws ParserConfigurationException, SAXException, IOException{
		Document doc2 = info.createDoc(name);
		doc2.getDocumentElement().normalize();
		NodeList nList = doc2.getElementsByTagName("attributes");
		NodeList nList2 = nList.item(0).getChildNodes();
		selected = new String[nList2.getLength()];
		types = new String[nList2.getLength()];
		for (int j = 0; j < nList2.getLength(); j++) {
			Node node = nList2.item(j);
			String k = node.getNodeName();
			selected[j] = k;
			Element eElement = (Element) node;
			types[j] = eElement.getElementsByTagName("type").item(0).getTextContent();
	}
  }
	//////////// delete with out condition
		public int deleteWithout(String name) throws ParserConfigurationException, SAXException, IOException, SQLException {
		Select sel = new Select(dbbName);	
		Object[][] all = sel.selectAll(name);
		String table = name;
		name = info.path + info.dbName + "\\" + name + "\\" + name;
		get(name);
		info.dropTable(table);
		try {
			info.createTable(table, selected, types);
			File dbDir = new File(info.path + info.dbName);
			String files[] = dbDir.list();
		} catch (TransformerException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return all.length;
	}
		//////////////// delete with condition
		public int deleteWith(String name, String cond) throws ParserConfigurationException, SAXException, IOException,
				SQLException {
			Select sel = new Select(dbbName);	
			Object[][] all = sel.selectAll(name);
			String table = name;
			name = info.path + info.dbName + "\\" + name + "\\" + name;
			get(name);
			info.dropTable(table);
			String[] condition = cond.split("=");
			String temp = condition[0].toLowerCase(), y = condition[1].toLowerCase();
			int ind = 0;
			Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
			for (int i = 0; i < all.length; i++) {
				for (int j = 0; j < selected.length; j++) {
					String ttt = "";
					if (types[j].equals("int")) {
						Integer bb = (Integer) all[i][j];
						ttt = bb + "";
					} else {
						ttt = (String) all[i][j];
					}
					if (selected[j].equals(temp) && ttt.equals(y)) {
						ind++;
						map.put(i, true);
					}
				}
			}
			try {
				info.createTable(table, selected, types);
				File dbDir = new File(info.path + info.dbName);
				String files[] = dbDir.list();
			} catch (TransformerException | JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < all.length; i++) {
				String[] create = new String[selected.length];
				// System.out.println(map.get(i));
				if (map.get(i) == null) {
					for (int j = 0; j < selected.length; j++) {
						if (types[j].equals("int")) {
							Integer bb = (Integer) all[i][j];
							create[j] = bb + "";
						} else {
							create[j] = (String) all[i][j];
						}
					}

					try {
						Insert ins = new Insert(dbbName);
						ins.insertWithout(table, create);
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return ind;
		}
}
