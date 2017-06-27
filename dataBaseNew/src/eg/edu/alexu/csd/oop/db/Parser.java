package eg.edu.alexu.csd.oop.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private final String UPDATE_1 = "update\\s+(\\w+)\\s+set\\s+([\\w+\\s*=\\s*\\w+,?\\s*]+)\\s+where\\s(.+)";
	private final String UPDATE_2 = "update\\s+(\\w+)\\s+set\\s+([\\w+\\s*=\\s*\\w+,?\\s*]+)";
	private final String INSERT_1 = "insert into\\s+(\\w+)\\(([\\w+,?\\s*]+)\\)\\s+values\\s+\\((['?\\w+'?,?\\s*]+)\\)";
	private final String INSERT_2 = "insert into\\s+(\\w+)\\s+values\\s+\\((['?\\w+'?,?\\s*]+)\\)";
	private final String SELECT_1 = "select\\s+(\\w+)?\\s+?from\\s+(\\w+)?\\s+?where\\s(.*)";
	private final String SELECT_2 = "select\\s\\*\\s+from\\s(\\w+)";
	private final String SELECT_3 = "select\\s+(\\w+)?\\s+?from\\s+(\\w+)";
	private final String SELECT_4 = "select\\s\\*\\s+from\\s+(\\w+)\\s+?where\\s(.*)";
	private final String DELETE_3 = "delete\\s\\*\\s+from\\s(\\w+)";
	private final String DELETE_1 = "delete from\\s(\\w+)";
	private final String DELETE_2 = "delete\\s+from\\s+(\\w+)?\\s+?where\\s(.*)";
	private final String CREATE_TABLE_1 = "create\\s+table\\s+(\\w+)\\s+\\(([('?\\w+')?,?\\s*]+)\\)";
	private final String CREATE_TABLE_2 = "create\\s+table\\s+(\\w+)\\(([('?\\w+')?,?\\s*]+)\\)";
	private final String CREATE_DATABASE = "create\\s+database\\s+(\\w+)";
	private final String DROP_DATABASE = "drop database\\s(\\w+)";
	private final String DROP_TABLE = "drop table\\s(\\w+)";
	
	public String validate_selectAll(String statement){
		return selectAll(statement, SELECT_2);
	}
	public  Map<String, ArrayList<String>> validate_selectAllWithCond(String statement){
		return selectAllCondition(statement, SELECT_4);
	}
	public String validate_createDatabase(String statement){
		return createDatabase(statement, CREATE_DATABASE);
	}
	public String validate_deleteWithoutCond(String statement){
		statement = statement.replace("'", "");
		String s = delete_1(statement, DELETE_1);
		if(s == null){
			return delete_1(statement, DELETE_3);
		}
		return s;
	}
	public String validate_dropDatabase(String statement){
		statement = statement.replace("'", "");
		return dropDatabase(statement, DROP_DATABASE);
	}
	public String validate_dropTable(String statement){
		statement = statement.replace("'", "");
		return dropTable(statement, DROP_TABLE);
	}
	public Map<String, ArrayList<String>> validate_createTable(String statement){
		statement = statement.replace("'", "");
		Map<String, ArrayList<String>> ma = createTable(statement, CREATE_TABLE_1);
		if(ma.isEmpty()) return createTable(statement, CREATE_TABLE_2);
		else return ma;
	}
	public Map<String, ArrayList<String>> validate_select(String statement){
		statement = statement.replace("'", "");
//		return select(statement, SELECT_1);
		Map<String, ArrayList<String>> ma = select(statement, SELECT_1);
		if(ma.isEmpty()) return select2(statement, SELECT_3);
		else return ma;
	}
	public Map<String, ArrayList<String>> validate_deleteWithCond(String statement){
		statement = statement.replace("'", "");
		return delete_2(statement, DELETE_2);
	}
	public Map<String, ArrayList<String>> validate_update(String statement){
		statement = statement.replace("'", "");
		Map<String, ArrayList<String>> ma = update_1(statement, UPDATE_1);
		if(ma.isEmpty()) return update_2(statement, UPDATE_2);
		else return ma;
	}
	public Map<String, ArrayList<String>> validate_insert(String statement){
		statement = statement.replace("'", "");
		Map<String, ArrayList<String>> ma = insert_1(statement, INSERT_1);
		if(ma.isEmpty()) return insert_2(statement, INSERT_2);
		else return ma;
	}
	/*
	 * "create database students" ,"drop database students" ,"drop table students"
	 * return tableName / databaseName
	 */
	private String createDatabase(String statement, String regex) {

		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		while (m.find()) {
			return m.group(1);

		}
		return null;
	}
	private String dropDatabase(String statement, String regex) {

		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		while (m.find()) {
			return m.group(1);

		}
		return null;
	}
	private String dropTable(String statement, String regex) {

		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		while (m.find()) {
			return m.group(1);

		}
		return null;
	}
	private String selectAll(String statement, String regex) {

		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		while (m.find()) {
			return m.group(1);

		}
		return null;
	}
	/**
	 * "create table Students (id int, name varchar, grade int)"
	 * return tableName
	 */
	private Map<String, ArrayList<String >> createTable(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> dataType = new ArrayList<String>();
		ArrayList<String> columns = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			for(int i=1, c=1; i <= m.groupCount(); i++){
	            String body = m.group(i);
	            String[] arr = body.replaceAll(",", " ").split("[ ]+");
	            
	            for(String s : arr){
	            	if(c == 1){
	            		tableN.add(s);
	            	}
	            	else if(c %2 == 0){
	            		columns.add(s);
	            	}
	            	else if(c %2 == 1){
	            		dataType.add(s);
	            	}
	            	c++;
	            }
	        }
			map.put("tablename", tableN);
			map.put("datatypes", dataType);
			map.put("columns", columns);
		}
		return map;
	}
	/**
	 * "select grade from students where grade > 15" , "select * from students"
	 * return tableName
	 */
	private Map<String, ArrayList<String >> select(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> condition = new ArrayList<String>();
		ArrayList<String> columnN = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			columnN.add(m.group(1));
			tableN.add(m.group(2));
			condition.add(m.group(3));
			map.put("tablename", tableN);
			map.put("condition", condition);
			map.put("columnname", columnN);
		}
		return map;
	}
	private Map<String, ArrayList<String >> selectAllCondition (String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> condition = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			tableN.add(m.group(1));
			condition.add(m.group(2));
			map.put("tablename", tableN);
			map.put("condition", condition);
		}
		return map;
	}
	private Map<String, ArrayList<String >> select2(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> columnN = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			columnN.add(m.group(1));
			tableN.add(m.group(2));
			map.put("tablename", tableN);
			map.put("columnname", columnN);
		}
		return map;
	}
	/**
	 * "update students set id = 8 where id > 4"
	 * return tableName
	 */
	private Map<String, ArrayList<String >> update_1(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> condition = new ArrayList<String>();
		ArrayList<String> sets = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
		for(int i=1; i <= m.groupCount(); i++){
            String body = m.group(i);
            String[] arr = body.replaceAll(",", " ").split("[ ]+");
            for(String s : arr){
            	if(i == 1){
            		tableN.add(s);
            	}
            	else if(i==2){
            		sets.add(s);
            	}
            	else if(i==3){
            		condition.add(s);
            	}
            }
            map.put("tablename", tableN);
			map.put("condition", condition);
			map.put("sets", sets);
        }
		}
		return map;
	}
	private Map<String, ArrayList<String >> update_2(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> sets = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		
		if (m.find()) {
			for(int i=1; i <= m.groupCount(); i++){
	            String body = m.group(i);
	            String[] arr = body.replaceAll(",", " ").split("[ ]+");
	            
	            for(String s : arr){
	            	if(i == 1){
	            		tableN.add(s);
	            	}
	            	else {
	            		sets.add(s);
	            	}
	            }
	        }
			map.put("tablename", tableN);
			map.put("sets", sets);
		}
		return map;
	}
	/**
	 * "insert into table values (value1,value2, value3)"
	 * return tableName
	 */
	private Map<String, ArrayList<String >> insert_2(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			for(int i=1; i <= m.groupCount(); i++){
	            String body = m.group(i);
	            String[] arr = body.replaceAll(",", " ").split("[ ]+");
	            
	            for(String s : arr){
	            	if(i == 1){
	            		tableN.add(s);
	            	}
	            	else {
	            		values.add(s);
	            	}
	            }
	        }
			map.put("tablename", tableN);
			map.put("values", values);
		}
		return map;
	}
	/**
	 * "insert into table (field1,field2) values (value1,value2)"
	 * return tableName
	 */
	private Map<String, ArrayList<String >> insert_1(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> columns = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			for(int i=1; i <= m.groupCount(); i++){
	            String body = m.group(i);
	            String[] arr = body.replaceAll(",", " ").split("[ ]+");
	            
	            for(String s : arr){
	            	if(i == 1){
	            		tableN.add(s);
	            	}
	            	else if(i == 2) {
	            		columns.add(s);
	            	}
	            	else if(i == 3) {
	            		values.add(s);
	            	}
	            }
	        }
			map.put("tablename", tableN);
			map.put("values", values);
			map.put("columns", columns);
		}
		return map;
	}

	private Map<String, ArrayList<String >> delete_2(String statement, String regex) {
		Map<String, ArrayList<String >> map = new HashMap <String, ArrayList<String >>();
		ArrayList<String> tableN = new ArrayList<String>();
		ArrayList<String> condition = new ArrayList<String>();
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			tableN.add(m.group(1));
			condition.add(m.group(2));
			map.put("tablename", tableN);
			map.put("condition", condition);
		}
		return map;
	}
	private String delete_1(String statement, String regex) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(statement);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}
}
