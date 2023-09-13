package pojos;

public class OrderPOJO {

    private String[] ingredients;

    public OrderPOJO(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public OrderPOJO() {

    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
