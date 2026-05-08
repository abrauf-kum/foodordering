package foodordering.model;

public class User extends Model{
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;

    public User(){

    }
    public User(int id,String name,String email,String password,String phone,
        String address,String role)
    {
        super(id);
        this.name=name;
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.address=address;
        this.role=role;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email; 
    }
    public String getPassword(){ 
        return password; 
    }
    public String getPhone(){ 
        return phone; 
    }
    public String getAddress(){ 
        return address; 
    }
    public String getRole(){ 
        return role; 
    }
    public boolean isAdmin(){
        return "admin".equals(role);
    }

    public void setName(String name){ 
        this.name=name; 
    }
    public void setEmail(String email){ 
        this.email=email; 
    }
    public void setPassword(String pass){ 
        this.password=pass; 
    }
    public void setPhone(String phone){ 
        this.phone=phone; 
    }
    public void setAddress(String address){ 
        this.address=address; 
    }
    @Override
    public String toString(){
        return name + " <" + email + ">";
    }
}