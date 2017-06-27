package eg.edu.alexu.csd.oop.db;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Select {
	private DatabaseM info = new DatabaseM();
	private Parser parser = new Parser();
	private String [] selected;
	private String [] types;
	private String dbbName;
	
	 public Select(String db){
		// TODO Auto-generated constructor stub
		 this.dbbName = db;
		 info.dbName = db;
	}
	// get data & type
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
	// select all
	public Object[][] selectAll(String name) throws ParserConfigurationException, SAXException, IOException,
			SQLException {
		String table = name;
		name = info.path + info.dbName + "\\" + name + "\\" + name;
		if (true) {
			try {
				get(name);
				if (info.checkFields(table, selected)) {
					Document doc = info.createDoc(name);
					NodeList list = doc.getElementsByTagName("Entry");
					if (list.getLength() == 0) {
						return new Object[0][0];
					}
					Object[][] res = new Object[100][100];
					int temp;
					for (temp = 0; temp < list.getLength(); temp++) {
						Node nNode = list.item(temp);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							for (int k = 0; k < selected.length; k++) {
								String ttt = eElement.getElementsByTagName(selected[k]).item(0).getTextContent();
								if (types[k].equals("int")) {
									Integer bb = Integer.parseInt(ttt);
									res[temp][k] = bb;
								} else {
									res[temp][k] = ttt;
								}}}}
					Object[][] finl = new Object[temp][selected.length];
					for (int i = 0; i < temp; i++) {
						for (int j = 0; j < selected.length; j++) {
							finl[i][j] = res[i][j];
						}
					}
					return finl;
				}
			} catch (Exception e) {
				throw new SQLException();
			}

		} else {
			return new Object[0][0];
		}
		return new Object[0][0];

	}
	//////////// select all with condition
	public Object[][] selectConditionAll (String name, String column ,String condition, int value) throws DOMException,
	ParserConfigurationException, SAXException, IOException, SQLException {
	Object [][] res = selectAll(name);
	int index = 0 , ind = 0;
	for (int i = 0 ; i < selected.length ; i++){
		if(selected[i].equals(column.toLowerCase())){index = i;break;}
	}
	Object [] [] semFinal = new Object[100][100];	
	for (int i = 0 ; i < res.length ; i++){
		int o = (int) res[i][index];
		if ( (condition.equals(">") && o > value) || (condition.equals("<") && o < value) || (condition.equals("=") && o == value)){
			for (int r = 0; r < selected.length; r++) {
				semFinal[ind][r] = res[i][r];
			}
			ind++;
	}
	}
		Object [][] fin = new Object[ind][selected.length];
		for (int i = 0; i < ind; i++) {
			for (int j = 0; j < selected.length; j++) {
				fin[i][j] = res[i][j];
			}
			}
	return fin;
	}
	////// select col 
	public Object[][] selectCol(String name, String column)
			throws ParserConfigurationException, SAXException, IOException, SQLException {
		Object [][] res = selectAll(name);
		int index = 0 , ind = 0;
		for (int i = 0 ; i < selected.length ; i++){
			if(selected[i].equals(column.toLowerCase())){index = i;break;}
		}
		Object [][] fin = new Object[res.length][1];
		for (int i = 0 ; i < res.length ; i++){
			fin[i][0] = res[i][index];
		}
		return fin;
	}
	/// select col with condition
	public Object[][] selectCondition(String name, String condition, String column, int value, String selc) throws DOMException,
	ParserConfigurationException, SAXException, IOException, SQLException {
		Object [][] res = selectAll(name);
		int indexColumn = 0 , indexSelc = 0 , ind = 0;
		for (int i = 0 ; i < selected.length ; i++){
			if(selected[i].equals(column.toLowerCase())){indexColumn = i;}
			else if (selected[i].equals(selc.toLowerCase())){indexSelc = i;}
		}
		Object [] [] semFinal = new Object[100][1];
		for (int i = 0 ; i < res.length ; i++){
			int o = (int) res[i][indexColumn];
			if ( (condition.equals(">") && o > value) || (condition.equals("<") && o < value) || (condition.equals("=") && o == value)){
				semFinal[ind++][0] = res[i][indexSelc];
		}
		}
		Object [][] fin = new Object[ind][1];
		for (int i = 0 ; i < ind ; i++){
			fin[i][0] = semFinal[i][0];
		}
		return fin;
	}
}
