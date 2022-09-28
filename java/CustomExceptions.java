public abstract class CustomExceptions {

    private String error_message;

    public CustomExceptions(String msg){
        this.error_message = msg;
    }

    public String getError_message(){
        return this.error_message;
    }
}
