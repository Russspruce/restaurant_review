import java.util.List;
import org.sql2o.*;
import java.util.Arrays;

public class Cuisine{
  private int id;
  private String cuisine_type;

  public Cuisine(String cuisine_type) {
    this.cuisine_type = cuisine_type;
  }

  public String getCuisineType() {
    return cuisine_type;
  }

  public int getId() {
    return id;
     }

  public static List<Cuisine> all() {
    String sql = "SELECT id, cuisine_type FROM cuisine";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Cuisine.class);
    }
  }

  @Override
  public boolean equals(Object otherCuisine) {
    if(!(otherCuisine instanceof Cuisine)) {
      return false;
    } else {
      Cuisine newCuisine = (Cuisine) otherCuisine;
      return this.getCuisineType().equals(newCuisine.getCuisineType()) &&
        this.getId() == newCuisine.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Cuisine(cuisine_type) VALUES (:cuisine_type)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("cuisine_type", this.cuisine_type)
      .executeUpdate()
      .getKey();
    }
  }

  public static Cuisine find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM cuisine where id=:id";
      Cuisine cuisine = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Cuisine.class);
    return cuisine;
    }
  }

  public List<Restaurant> getRestaurants() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurant where cuisine_id=:id";
      return con.createQuery(sql)
      .addParameter("id", this.id)
      .executeAndFetch(Restaurant.class);
    }
  }

}
