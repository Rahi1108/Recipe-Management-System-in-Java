import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecipeDAO {
    private static final Logger LOGGER = Logger.getLogger(RecipeDAO.class.getName());

    public void addRecipe(Recipe recipe) throws SQLException {
        String insertRecipeSQL = "INSERT INTO recipes(name, description, instructions) VALUES(?, ?, ?)";
        String insertIngredientSQL = "INSERT INTO ingredients(recipe_id, name, quantity) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement recipeStmt = conn.prepareStatement(insertRecipeSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ingredientStmt = conn.prepareStatement(insertIngredientSQL)) {

            // Insert the recipe
            recipeStmt.setString(1, recipe.getName());
            recipeStmt.setString(2, recipe.getDescription());
            recipeStmt.setString(3, recipe.getInstructions());
            recipeStmt.executeUpdate();

            // Get the generated recipe ID
            ResultSet generatedKeys = recipeStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int recipeId = generatedKeys.getInt(1);

                // Insert the ingredients
                for (Ingredient ingredient : recipe.getIngredients()) {
                    ingredientStmt.setInt(1, recipeId);
                    ingredientStmt.setString(2, ingredient.getName());
                    ingredientStmt.setString(3, ingredient.getQuantity());
                    ingredientStmt.addBatch();
                }
                ingredientStmt.executeBatch();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding recipe", e);
            throw e;
        }
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setId(rs.getInt("id"));
                recipe.setName(rs.getString("name"));
                recipe.setDescription(rs.getString("description"));
                recipe.setInstructions(rs.getString("instructions"));
                recipe.setIngredients(getIngredientsForRecipe(recipe.getId()));
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving recipes", e);
            throw e;
        }
        return recipes;
    }

    public Recipe getRecipeById(int id) throws SQLException {
        Recipe recipe = null;
        String sql = "SELECT * FROM recipes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                recipe = new Recipe();
                recipe.setId(rs.getInt("id"));
                recipe.setName(rs.getString("name"));
                recipe.setDescription(rs.getString("description"));
                recipe.setInstructions(rs.getString("instructions"));
                recipe.setIngredients(getIngredientsForRecipe(recipe.getId()));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving recipe", e);
            throw e;
        }
        return recipe;
    }

    public void deleteRecipeByName(String name) throws SQLException {
        String sql = "DELETE FROM recipes WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting recipe", e);
            throw e;
        }
    }
    

    private List<Ingredient> getIngredientsForRecipe(int recipeId) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredients WHERE recipe_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recipeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setRecipeId(rs.getInt("recipe_id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setQuantity(rs.getString("quantity"));
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving ingredients", e);
            throw e;
        }
        return ingredients;
    }
}
