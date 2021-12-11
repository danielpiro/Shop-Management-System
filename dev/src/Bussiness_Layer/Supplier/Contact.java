package Bussiness_Layer.Supplier;

import java.util.regex.Pattern;

public class Contact {

    private final String name;
    private String phoneNumber;
    private String email;

    public Contact(String _name,String _phoneNumber,String _email){
        name = _name;
        if (!isLegalNumber(_phoneNumber))
            throw new IllegalArgumentException("Illegal phone Number");
        phoneNumber = _phoneNumber;
        if(!isValidEmail(_email))
            throw new IllegalArgumentException("not a valid email address");
        email = _email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        if(!isValidEmail(email))
            throw new IllegalArgumentException("not a valid email address");
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!isLegalNumber(phoneNumber))
            throw new IllegalArgumentException("Illegal phone Number");
        this.phoneNumber = phoneNumber;
    }

    public void changeContact(String phoneNumber, String email){
        if(!phoneNumber.equals(this.phoneNumber))
            setPhoneNumber(phoneNumber);
        if(!email.equals(this.email))
            setEmail(email);
    }

    /**
     * @param phoneNumber string of contact's cell phone number
     * @return true if the string contain only 10 digit and start with 05
     */
    private boolean isLegalNumber(String phoneNumber){
        if(phoneNumber.length() != 10)
            return false;
        if (phoneNumber.chars().anyMatch( x -> !Character.isDigit(x)) )
            return  false;
        return phoneNumber.charAt(0) == '0' && phoneNumber.charAt(1) == '5';
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public String getContact(){
        return String.format("Name: %s, Phone Number: %s, Email: %s",name,phoneNumber,email);
    }
}
