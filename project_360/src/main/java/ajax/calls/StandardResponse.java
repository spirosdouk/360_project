package ajax.calls;



import com.google.gson.JsonElement;

public class StandardResponse {
    private String message;
    private JsonElement data;

  
    public StandardResponse(String message) {
        this.message = message;
    }

    public StandardResponse(JsonElement data) {
        this.data = data;
    }

   

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}