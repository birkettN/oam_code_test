package oam.oamtest.models;


import com.j256.ormlite.field.DatabaseField;

public class ListingModel {

    public ListingProperty interior;
    public ListingProperty model;
    public ListingProperty location;

    @DatabaseField
    public String lastname;
    public ListingProperty colour;

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public String title;
    public ListingProperty duty;
    public ListingProperty drive_setup;

    @DatabaseField
    public String description;

    @DatabaseField
    public String created_at;

    @DatabaseField
    public String currency_symbol;

    @DatabaseField
    public String year;
    public ListingProperty user_id;

    @DatabaseField
    public String mileage;
    public ListingProperty body_type;
    public ListingProperty condition;
    public ListingProperty door_count;
    public ListingProperty listing_status;

    @DatabaseField
    public String firstname;

    @DatabaseField
    public String registration;

    @DatabaseField
    public String default_image;

    @DatabaseField
    public String money_back_guarantee;
    public ListingProperty transmission;

    @DatabaseField
    public String url;

    @DatabaseField
    public String is_negotiable;

    @DatabaseField
    public String disable_at;

    @DatabaseField
    public String engine_size;
    public ListingProperty fuel_type;

    @DatabaseField
    public String updated_at;

    @DatabaseField
    public String price;

    @DatabaseField
    public String mobile_number;

    @DatabaseField
    public String enable_at;

    @DatabaseField
    public String email;

    @DatabaseField
    public String views;

    public ListingProperty contact_method;
    public ListingProperty drive_type;
    public ListingProperty advertiser;
    public ListingProperty make;

}
