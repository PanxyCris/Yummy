package edu.nju.yummy.util;

import edu.nju.yummy.dao.CategoryDao;
import edu.nju.yummy.entity.Category;
import edu.nju.yummy.entity.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.sql.*;

@Configuration
public class ThreadLocalUtil implements Runnable {

    private static String url = "jdbc:mysql://localhost:3306/Yummy?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&tinyInt1isBit=true";
    private static String user = "root";
    private static String password = "usemysql";


    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        Connection con = connectionHolder.get();
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(url, user, password);
                connectionHolder.set(con);
            } catch (ClassNotFoundException | SQLException e) {
                // ...
            }
        }
        return con;
    }

    public void saveAndFlush(Food food) {
        try {
            Statement stat = getConnection().createStatement();
            stat.execute("update food " + " set number = number - " + food.getNumber()+" where fid = " + food.getFid());
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Food getOne(long id) {
        try {
            Statement stat = getConnection().createStatement();
            ResultSet resultSet = stat.executeQuery("select * from food where fid=" + id);
            Food food = null;
            while (resultSet.next()) {
                food = new Food(id,resultSet.getString("name"),
                        null,
                        resultSet.getInt("number")
                        , resultSet.getDouble("price"),
                        resultSet.getDouble("box_price"));
            }
            stat.close();
            return food;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {

    }
}
