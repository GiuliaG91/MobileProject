package com.example.giuliagigi.jobplacement;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Silvia on 20/05/2015.
 */

@ParseClassName("News")
public class News extends ParseObject {

    public static final String TYPE = "type";
    public static final String MESSAGE = "message";
    public static final String DATE = "date";
    public static final String STUDENT = "student";
    public static final String COMPANY = "company";
    public static final String COMPANY_OFFER = "company_offer";

    GlobalData globalData;

    /*
    public News(GlobalData gd){
        super();
        globalData = gd;
    }
    */

    /*****   GETTER   *****/

    public int getType(){
        return getInt(News.TYPE);
    }

    public String getMessage(){
        return getString(News.MESSAGE);
    }

    public Calendar getDate(){

        Calendar c = Calendar.getInstance();
        c.setTime((Date)this.get(News.DATE));

        return c;
    }

    public Student getStudent(){
        return (Student)get(News.STUDENT);
    }

    public Company getCompany(){
        return (Company)get(News.COMPANY);
    }

    public CompanyOffer getCompanyOffer(){
        return (CompanyOffer)get(News.COMPANY_OFFER);
    }

    /*****   GETTER   *****/

    public void setType(int type){
        this.put(News.TYPE, type);
    }

    public void setMessage(String message){
        this.put(News.MESSAGE, message);
    }

    public void setDate(Calendar c){
        this.put(News.DATE, c.getTime());
    }

    public void setStudent(Student student){
        this.put(News.STUDENT, student);
    }

    public void setCompany(Company company){
        this.put(News.COMPANY, company);
    }

    public void setCompanyOffer(CompanyOffer companyOffer){
        this.put(News.COMPANY_OFFER, companyOffer);
    }

    public void createNews(int type, CompanyOffer co, Student student, GlobalData globalData){

        this.setType(type);
        this.setDate(Calendar.getInstance());

        String message;

        switch (type){

            case 0:  // New job offer published
                     this.setCompanyOffer(co);
                     message = globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.new_job_offer_message) + " \"" + co.getOfferObject() + "\"";
                     this.setMessage(message);


                     this.saveInBackground();

                     break;

            case 1:  // Student applied for a own Job Offer
                     this.setCompanyOffer(co);
                     this.setStudent((Student)globalData.getUserObject());
                     message = globalData.getUserObject().getName() + " " + ((Student) globalData.getUserObject()).getSurname() + " " + globalData.getResources().getString(R.string.new_application_message) + " \"" + co.getOfferObject() + "\"";
                     this.setMessage(message);

                     this.setCompany(co.getCompany());

                     this.saveInBackground();

                     break;

            case 2:  // Student applied accepted
                     this.setCompanyOffer(co);
                     this.setStudent(student);
                     message = globalData.getResources().getString(R.string.the_company) + " " + globalData.getUserObject().getName() + globalData.getResources().getString(R.string.application_accepted_message) + " \"" + co.getOfferObject() + "\"";
                     this.setMessage(message);


                     this.saveInBackground();

                     break;

            case 3:  // New Company signed up
                     this.setCompany((Company)globalData.getUserObject());
                     message = globalData.getResources().getString(R.string.the_company) + " " + globalData.getUserObject().getName() + globalData.getResources().getString(R.string.new_company_signed_up_message) + " \"" + globalData.getResources().getString(R.string.app_name) + "\"";
                     this.setMessage(message);


                     this.saveInBackground();

                     break;

            case 4:  // Advertisement Company

                     break;

            case 5: // Modified Job Offer

                    break;

            case 6: // Deleted Job Offer

            default:
        }

    }

}
