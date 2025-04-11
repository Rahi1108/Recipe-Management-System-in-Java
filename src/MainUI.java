import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainUI {
    private static final Logger LOGGER = Logger.getLogger(MainUI.class.getName());
    private JFrame frame;
    private RecipeDAO recipeDAO;
    private JTextArea recipeTextArea;

    public MainUI() {
        // Initialize the RecipeDAO
        recipeDAO = new RecipeDAO();
    }

    public void initialize() {
        setupUI();
    }

    private void setupUI() {
        frame = new JFrame("Recipe Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 128, 128)); // Dark Teal
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Recipe Management System", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Add Recipe");
        addButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton.setBackground(new Color(102, 204, 255)); // Light Blue
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);
        addButton.addActionListener((ActionEvent e) -> addRecipe());

        JButton viewButton = new JButton("View Recipes");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewButton.setBackground(new Color(102, 204, 255)); // Light Blue
        viewButton.setOpaque(true);
        viewButton.setBorderPainted(false);
        viewButton.addActionListener((ActionEvent e) -> viewRecipes());

        JButton deleteButton = new JButton("Delete Recipe");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteButton.setBackground(new Color(255, 102, 102)); // Light Red
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.addActionListener((ActionEvent e) -> deleteRecipe());

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Recipe Display Area
        recipeTextArea = new JTextArea();
        recipeTextArea.setEditable(false);
        recipeTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        recipeTextArea.setLineWrap(true);
        recipeTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(recipeTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Recipe Details"));

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void addRecipe() {
        String name = JOptionPane.showInputDialog(frame, "Enter recipe name:");
        if (name == null || name.trim().isEmpty()) return;

        String description = JOptionPane.showInputDialog(frame, "Enter recipe description:");
        if (description == null) description = "";

        String instructions = JOptionPane.showInputDialog(frame, "Enter recipe instructions:");
        if (instructions == null) instructions = "";

        // Collect ingredients
        List<Ingredient> ingredients = new ArrayList<>();
        String addMoreIngredients = "yes";
        while (addMoreIngredients.equalsIgnoreCase("yes")) {
            String ingredientName = JOptionPane.showInputDialog(frame, "Enter ingredient name:");
            if (ingredientName == null || ingredientName.trim().isEmpty()) break;

            String quantity = JOptionPane.showInputDialog(frame, "Enter ingredient quantity:");
            if (quantity == null) quantity = "";

            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName);
            ingredient.setQuantity(quantity);
            ingredients.add(ingredient);

            addMoreIngredients = JOptionPane.showInputDialog(frame, "Add another ingredient? (yes/no):");
        }

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);

        try {
            recipeDAO.addRecipe(recipe);
            JOptionPane.showMessageDialog(frame, "Recipe added successfully!");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding recipe", ex);
            JOptionPane.showMessageDialog(frame, "Error adding recipe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewRecipes() {
        try {
            List<Recipe> recipes = recipeDAO.getAllRecipes();
            StringBuilder recipeList = new StringBuilder();
            for (Recipe recipe : recipes) {
                recipeList.append("ID: ").append(recipe.getId())
                          .append("\nName: ").append(recipe.getName())
                          .append("\nDescription: ").append(recipe.getDescription())
                          .append("\nInstructions: ").append(recipe.getInstructions())
                          .append("\nIngredients:\n");
                for (Ingredient ingredient : recipe.getIngredients()) {
                    recipeList.append(" - ").append(ingredient.getName())
                              .append(": ").append(ingredient.getQuantity())
                              .append("\n");
                }
                recipeList.append("\n-------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
            }
            recipeTextArea.setText(recipeList.toString());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error retrieving recipes", ex);
            JOptionPane.showMessageDialog(frame, "Error retrieving recipes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecipe() {
        String name = JOptionPane.showInputDialog(frame, "Enter recipe name to delete:");
        if (name == null || name.trim().isEmpty()) return;

        try {
            recipeDAO.deleteRecipeByName(name);
            JOptionPane.showMessageDialog(frame, "Recipe deleted successfully!");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting recipe", ex);
            JOptionPane.showMessageDialog(frame, "Error deleting recipe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI mainUI = new MainUI();
            mainUI.initialize();
        });
    }
}
