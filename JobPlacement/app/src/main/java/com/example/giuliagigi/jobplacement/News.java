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

    public static final String TYPE_FIELD = "type";
    public static final String MESSAGE_FIELD = "message";
    public static final String DATE_FIELD = "date";
    public static final String STUDENT_FIELD = "student";
    public static final String COMPANY_FIELD = "company";
    public static final String COMPANY_OFFER_FIELD = "company_offer";
    public static final String OFFER_STATUS_FIELD = "offer_status";
    public static final String COURSE_FIELD = "course";

    public static final int TYPE_NEW_OFFER = 0;
    public static final int TYPE_OFFER_APPLICATION = 1;
    public static final int TYPE_APPLICATION_STATE = 2;
    public static final int TYPE_NEW_COMPANY = 3;
    public static final int TYPE_ADVERTISEMENT = 4;
    public static final int TYPE_OFFER_MODIFIED = 5;
    public static final int TYPE_OFFER_DELETED = 6;
    public static final int TYPE_NEW_NOTICE = 7;

    GlobalData globalData;

    /*
    public News(GlobalData gd){
        super();
        globalData = gd;
    }
    */

    /*****   GETTER   *****/

    public int getType(){
        return getInt(News.TYPE_FIELD);
    }

    public String getMessage(){
        return getString(News.MESSAGE_FIELD);
    }

    public Calendar getDate(){

        Calendar c = Calendar.getInstance();
        c.setTime((Date)this.get(News.DATE_FIELD));

        return c;
    }

    public Student getStudent(){
        return (Student)get(News.STUDENT_FIELD);
    }

    public Company getCompany(){
        return (Company)get(News.COMPANY_FIELD);
    }

    public Course getCourse() {
        return (Course)get(News.COURSE_FIELD);
    }

    public CompanyOffer getCompanyOffer(){
        return (CompanyOffer)get(News.COMPANY_OFFER_FIELD);
    }

    public OfferStatus getOfferStatus(){

        return (OfferStatus) get(News.OFFER_STATUS_FIELD);
    }

    /*****   GETTER   *****/

    public void setType(int type){
        this.put(News.TYPE_FIELD, type);
    }

    public void setMessage(String message){
        this.put(News.MESSAGE_FIELD, message);
    }

    public void setDate(Calendar c){
        this.put(News.DATE_FIELD, c.getTime());
    }

    public void setStudent(Student student){
        this.put(News.STUDENT_FIELD, student);
    }

    public void setCourse(Course course){
        this.put(News.COURSE_FIELD, course);
    }

    public void setCompany(Company company){
        this.put(News.COMPANY_FIELD, company);
    }

    public void setCompanyOffer(CompanyOffer companyOffer){
        this.put(News.COMPANY_OFFER_FIELD, companyOffer);
    }

    public void setOfferStatus(OfferStatus offerStatus){
        this.put(News.OFFER_STATUS_FIELD, offerStatus);
    }

    public void createNews(int type, CompanyOffer offer, Student student, OfferStatus offerStatus, GlobalData globalData){

        this.setType(type);
        this.setDate(Calendar.getInstance());

        String message = "";

        switch (type){

            case TYPE_NEW_OFFER:  // New job offer published
                     this.setCompanyOffer(offer);
                     message = globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.new_job_offer_message) + " \"" + offer.getOfferObject() + "\"";
                     this.setMessage(message);

                     this.saveInBackground();

                     break;

            case TYPE_OFFER_APPLICATION:  // Student applied for a own Job Offer
                     this.setCompanyOffer(offer);
                     this.setStudent((Student) globalData.getUserObject());
                     message = globalData.getUserObject().getName() + " " + ((Student) globalData.getUserObject()).getSurname() + " " + globalData.getResources().getString(R.string.new_application_message) + " \"" + offer.getOfferObject() + "\"";
                     this.setMessage(message);

                     this.setCompany(offer.getCompany());

                     this.saveInBackground();

                     break;

            case TYPE_APPLICATION_STATE:  // Student's application state changed
                     this.setCompanyOffer(offer);
                     this.setStudent(student);
                     this.setOfferStatus(offerStatus);

                                String status=offerStatus.getType();
                                String eng=OfferStatus.getEnglishType(offerStatus.getType());
                switch (OfferStatus.getEnglishType(offerStatus.getType())){

                         case OfferStatus.TYPE_ACCEPTED:
                                                        message = globalData.getResources().getString(R.string.the_company) + " " + globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.application_accepted_message) + " \"" + offer.getOfferObject() + "\"";
                                                        break;

                         case OfferStatus.TYPE_CONSIDERING:
                                                             message = globalData.getResources().getString(R.string.the_company) + " " + globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.application_considering_message) + " \"" + offer.getOfferObject() + "\"";
                                                             break;

                         case OfferStatus.TYPE_REFUSED:
                                                        message = globalData.getResources().getString(R.string.the_company) + " " + globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.application_refused_message) + " \"" + offer.getOfferObject() + "\"";
                                                        break;

                         case OfferStatus.TYPE_START:
                                                        message = globalData.getResources().getString(R.string.the_company) + " " + globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.application_processing_message) + " \"" + offer.getOfferObject() + "\"";
                                                        break;

                         default:
                     }
                     this.setMessage(message);

                     this.saveInBackground();

                     break;

            case TYPE_NEW_COMPANY:  // New Company signed up
                     globalData.getCurrentUser();
                     this.setCompany((Company)globalData.getUserObject());
                     message = globalData.getResources().getString(R.string.the_company) + " " + globalData.getUserObject().getName() + " " + globalData.getResources().getString(R.string.new_company_signed_up_message) + " \"" + globalData.getResources().getString(R.string.app_name) + "\"";
                     this.setMessage(message);

                     this.saveInBackground();

                     break;

            case TYPE_ADVERTISEMENT:  // Advertisement Company

                     break;

            case TYPE_OFFER_MODIFIED: // Modified Job Offer

                    break;

            case TYPE_OFFER_DELETED: // Deleted Job Offer


            default:
        }

    }

    public void createNews(int type, Course course, String message){
        this.setCourse(course);
        this.setDate(Calendar.getInstance());
        this.setType(type);
        this.setMessage(message);
        this.saveEventually();
    }

}
