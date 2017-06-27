package eg.edu.alexu.csd.oop.db;

import java.io.File;
import java.sql.SQLException;

public class JUnit {

	public static void main(String[] arg) {
		Database db = new DatabaseM();
		/*
		 * createDataBase("Sample", true);
Return : yourFixedPath/sample
CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)
Return : true
CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)
Return : false
CREATE TABLE incomplete_table_name1
THROW SQLException
====>>> new Instance
CREATE TABLE table_name2(column_name1 varchar, column_name2 int, column_name3 varchar)
THROW RuntimeError
		 */
//		System.out.println(db.createDatabase("MyData1", true));
//		System.out.println(db.createDatabase("Myff2", false));

		try {
			
			//File dbDir = new File(path);
			//String files[] = dbDir.list();System.out.println( files.length );
		//	System.out.println("LOL "+db.executeStructureQuery("CREATE   TABLE   table_name1(column_name1 varchar , column_name2    int,  column_name3 varchar) "));
			//System.out.println(db.executeStructureQuery("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)"));
			//files = dbDir.list();System.out.println( files.length );
			//System.out.println("1 => "+db.executeUpdateQuery("INSERT INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)"));
			//System.out.println("error => "+db.executeUpdateQuery("INSERT INTO * table_name2(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)"));
			//System.out.println("error => "+db.executeUpdateQuery("INS ERT INTO table_name3(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)"));
			//System.out.println("1 => "+db.executeUpdateQuery("INSERT INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)"));
		/*sysntax error	*///System.out.println("fff  "+db.executeUpdateQuery("INSERT INTO table_name1(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)"));
		//	System.out.println("vvv  "+db.executeUpdateQuery("INSERT INTO table_name1 VALUES ('value1', 3,'value3')"));
//			//System.out.println(db.executeUpdateQuery("INSERT INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4"));
//			System.out.println("kkkkkk " +db.executeUpdateQuery("INSERT INTO table_name1(column_NAME1, COLUMN_name3, column_name2) VALUES ('valu', 'value3', 4)"));
		   // db.executeQuery("SELECT * FROM table_name1");
		    //db.executeQuery("SELECT column_NAME1 FROM table_name1 WHERE column_name2 < 5");
			String path = db.createDatabase("hhhhh", true);
			System.out.println(db.executeStructureQuery("CREATE TABLE students (id int , name varchar,grade int)"));   // returns  true
			System.out.println(db.executeUpdateQuery("INSERT INTO  students VALUES (15 ,'samir' , 30)"));   // returns 1
			System.out.println(db.executeUpdateQuery("INSERT INTO students VALUES (5, 'Sherouq', 20)"));   // returns 1
			System.out.println(db.executeUpdateQuery("INSERT INTO students VALUES (7, 'Shehab', 15)"));	// returns 1
			db.executeQuery("SELECT * FROM students");
			db.executeQuery("SELECT * FROM students WHERE id > 5");
			db.executeQuery("SELECT name FROM students WHERE id > 5");
//			db.executeStructureQuery("CREATE TABLE table_name13 (column_name1 varchar, column_name2 int, column_name3 varchar)");
//			db.executeUpdateQuery("INSERT INTO table_name13(column_name1, column_name3, column_name2) VALUES ('value1', 'value3', 4)");
//			db.executeUpdateQuery("INSERT INTO table_name13(column_name1, column_name2, column_name3) VALUES ('value1', 4, 'value3')");
//			db.executeUpdateQuery("INSERT INTO table_name13(column_name1, column_name3, column_name2) values ('value2', 'value4', 5)");
//			db.executeUpdateQuery("INSERT INTO table_name13(column_name1, column_name3, column_name2) values ('value5', 'value6', 6)");
			//select column_name1 from table_name13 where column_name2 < 5
//			db.executeQuery("SELECT * FROM table_name13");
//			db.executeQuery("SELECT column_name1 FROM table_name13 WHERE column_name2 < 5");
			//System.out.println(db.executeUpdateQuery("UPDATE table_name13 SET column_name1='11111111', column_name2=22222222, column_name3='333333333' WHERE column_name2=4"));
			//System.out.println(db.executeUpdateQuery("UPDATE table_name13 SET column_name1='11111111', column_name2=22222222, column_name3='333333333'"));
			//System.out.println(db.executeUpdateQuery("DELETE FROM table_name13"));			
			//System.out.println("fff  "+db.executeUpdateQuery("INSERT INTO table_name1(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)"));	
   //		db.executeUpdateQuery("UPDATE students SET id = 8 , grade = 30 WHERE id > 4");  // returns 2
   //		db.executeUpdateQuery("UPDATE students SET id = 8 , grade = 30");
//			System.out.println(db.executeUpdateQuery("UPDATE table_name13 SET column_name1='11111111', column_name2=22222222  WHERE coLUmn_NAME1='VAlUE1'"));
			 
		//	db.executeUpdateQuery("UPDATE table_name SET column1=value WHERE some_column=some_value");
			//System.out.println("create  " +db.executeStructureQuery("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)"));
//			System.out.println("createT  " +db.executeStructureQuery("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)"));
//			System.out.println(db.executeUpdateQuery ("INSERT INTO students(7, 'Shehab', 15)"));
			//System.out.println("createT  " +db.executeStructureQuery("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)"));
		//	System.out.println("createi  " +db.executeStructureQuery("CREATE TABLE incomplete_table_name1"));
			
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
