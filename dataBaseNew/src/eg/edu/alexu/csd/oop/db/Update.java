package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Update {
	private DatabaseM info = new DatabaseM();
	private Parser parser = new Parser();
	private String [] selected ;
	private String [] types ;
	private String dbbName;
	
	 public Update(String db) {
		// TODO Auto-generated constructor stub
		this.dbbName = db;
		info.dbName = db;
	}	
	public int update(String query) throws SQLException{
		Map<String, ArrayList<String>> map = parser.validate_update(query);
		String name = map.get("tablename").get(0);
		ArrayList<String> s = map.get("sets");
		if (map.get("condition") == null){ // update with no condition
			try {
				return this.updateWithout(name.toLowerCase(), s);
			} catch (ParserConfigurationException | SAXException
					| IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {// with condition
			String cond = map.get("condition").get(0);
			try {
				return this.updateWith(name.toLowerCase(), s , cond.toLowerCase());
			} catch (ParserConfigurationException | SAXException
					| IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	private void get(String name) throws ParserConfigurationException, SAXException, IOException{
		Document doc2 = info.createDoc(name);
		doc2.getDocumentElement().normalize();
		NodeList nList = doc2.getElementsByTagName("attributes");
		NodeList nList2 = nList.item(0).getChildNodes();
		selected = new String[nList2.getLength()];
		types = new String[selected.length];
		for (int j = 0; j < nList2.getLength(); j++) {
			Node node = nList2.item(j);
			String k = node.getNodeName();
			selected[j] = k;
			Element eElement = (Element) node;
			types[j] = eElement.getElementsByTagName("type").item(0)
					.getTextContent();
	}
	}
	//////////////// updateWithout Condition
	public int updateWithout(String name, ArrayList<String> sets)
			throws ParserConfigurationException, SAXException, IOException,
			SQLException {
		Select sel = new Select(dbbName);
		Object[][] all = sel.selectAll(name);
		String table = name;
		name = info.path + info.dbName + "\\" + name + "\\" + name;
		get(name);
		info.dropTable(table);

		for (int i = 0; i < all.length; i++) {
			for (int j = 0; j < selected.length; j++) {
				for (int k = 0; k < sets.size(); k++) {
					String temp = sets.get(k);
					String[] use = temp.split("=");
					if (use[0].toLowerCase().equals(selected[j])) {
						if (types[j].equals("int")) {
							Integer bb = Integer.parseInt(use[1]);
							all[i][j] = bb;
						} else {
							all[i][j] = use[1];
						}
					}
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
			for (int j = 0; j < selected.length; j++) {
				if (types[j].equals("int")) {
					Integer bb = (Integer) all[i][j];
					create[j] = bb + "";
				} else {
					create[j] = (String) all[i][j];
				}
				create[j].toLowerCase();
			}
			try {
				Insert ins = new Insert(dbbName);
				ins.insertWithout(table, create);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return all.length;
	}
	///////// update with condition
	public int updateWith(String name, ArrayList<String> sets, String cond)throws ParserConfigurationException, SAXException, IOException,
			SQLException {
		Select sel = new Select(dbbName);
		Object[][] all = sel.selectAll(name);
		String table = name;
		name = info.path + info.dbName + "\\" + name + "\\" + name;
		get(name);
		info.dropTable(table);
		String[] condition = cond.split("=");
		int pl = 0;
		for (int i = 0; i < selected.length; i++) {
			if (condition[0].toLowerCase().equals(selected[i])) {
				pl = i;
				break;
			}
		}
		int ind = 0;
		for (int i = 0; i < all.length; i++) {
			String u = "";
			if (types[pl].equals("int")) {
				Integer bb = (Integer) all[i][pl];
				u = bb + "";
			} else {
				u = (String) all[i][pl];
			}
			if (u.toLowerCase().equals(condition[1].toLowerCase())) {
				ind++;
				for (int j = 0; j < selected.length; j++) {
					for (int k = 0; k < sets.size(); k++) {
						String temp = sets.get(k);
						String[] use = temp.split("=");
						if (use[0].toLowerCase().equals(selected[j])) {
							if (types[j].equals("int")) {
								Integer bb = Integer.parseInt(use[1]);
								all[i][j] = bb;
							} else {
								all[i][j] = use[1];
							}
						}
					}
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
			for (int j = 0; j < selected.length; j++) {
				if (types[j].equals("int")) {
					Integer bb = (Integer) all[i][j];
					create[j] = bb + "";
				} else {
					create[j] = (String) all[i][j];
				}
				create[j].toLowerCase();
			}
			try {
				Insert ins = new Insert(dbbName);
				ins.insertWithout(table, create);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ind;
	}
}
