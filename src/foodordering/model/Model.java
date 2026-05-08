package foodordering.model;

public abstract class Model implements Persistable{
    private int id;

    public Model(){

    }
    public Model(int id){
        this.id=id;
    }
    @Override
    public int getId(){
        return id;
    }
    protected void setId(int id){
        this.id=id;
    }
    @Override
    public abstract String toString();
}