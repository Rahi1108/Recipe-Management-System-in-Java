# Recipe-Management-System-in-Java
Project Overview
The Recipe Management System is a Java application designed to manage recipes with a focus on handling recipes and their ingredients. This system allows users to add, view, and delete recipes, including their associated ingredients, using a graphical user interface (GUI. It also integrates with an SQLite database to store and manage the data).

Components and Their Functions

1. Recipe.java
•	Purpose:
o	Represents a recipe with details including ID, name, description, instructions, and a list of ingredients.
•	Functionality:
o	Manages the attributes of a recipe.
o	Provides methods to get and set recipe details and its ingredients.
2. Ingredient.java
•	Purpose:
o	Represents an ingredient used in a recipe.
•	Functionality:
o	Manages ingredient details including ID, recipe ID, name, and quantity.
o	Provides methods to get and set ingredient information.
3. RecipeDAO.java
•	Purpose:
o	Handles database operations related to recipes and ingredients.

•	Functionality:
o	addRecipe(Recipe recipe): Adds a new recipe along with its ingredients to the database.
o	getAllRecipes(): Retrieves and returns a list of all recipes.
o	getRecipeById(int id): Retrieves a specific recipe by its ID.
o	deleteRecipeByName(String name): Deletes a recipe by its name.
o	getIngredientsForRecipe(int recipeId): Retrieves ingredients associated with a specific recipe.
4. DatabaseInitializer.java
•	Purpose:
o	Initializes the database schema.
•	Functionality:
o	Reads and executes SQL commands from a script file to create necessary tables in the SQLite database.
5. DatabaseConnection.java
•	Purpose:
o	Manages the connection to the SQLite database.
•	Functionality:
o	Provides a method to obtain a connection to the database.
o	Ensures the SQLite JDBC driver is loaded.
6. MainUI.java
•	Purpose:
o	Provides the graphical user interface for the application.
•	Functionality:
o	Add Recipe: Prompts the user to enter recipe details and ingredients, then saves them to the database.
o	View Recipes: Retrieves and displays a list of all recipes along with their details and ingredients.
o	Delete Recipe: Prompts the user to enter a recipe name and deletes the corresponding recipe from the database.
o	User Interaction: Facilitates interaction through buttons and dialog prompts.
7. create_tables.sql
•	Purpose:
o	Defines the schema for the database tables.


•	Functionality:
o	Creates recipes and ingredients tables if they do not exist, defining the necessary fields and relationships.

Functional Flow

1. Start the Application:
•	Execute MainUI.java to launch the graphical user interface of the application.
2. Main Menu (MainUI):
•	Add Recipe: Opens a series of prompts to enter the recipe’s name, description, instructions, and ingredients. Saves the recipe and ingredients to the database.
•	View Recipes: Fetches and displays a list of all recipes with their details and ingredients.
•	Delete Recipe: Prompts the user to enter a recipe name and removes the recipe from the database if it exists.
•	Exit: Closes the application.
3. Database Management:
•	DatabaseConnection.java: Provides a connection to the SQLite database.
•	RecipeDAO.java: Manages CRUD operations on recipes and ingredients using SQL queries.
•	DatabaseInitializer.java: Initializes the database schema by executing SQL commands.

Dependencies

•	Java Development Kit (JDK): Required for compiling and running the Java application.
•	SQLite JDBC Driver: Required for database connectivity.

Error Handling and Validation

•	Recipe Addition: Validates input and ensures that recipes and their ingredients are correctly added to the database.
•	Recipe Viewing: Handles scenarios where no recipes are found and displays appropriate messages.
•	Recipe Removal: Ensures recipes are safely removed from the database and handles cases where the recipe does not exist.
