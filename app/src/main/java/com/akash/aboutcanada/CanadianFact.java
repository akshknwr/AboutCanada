package com.akash.aboutcanada;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by akash on 8/03/18.
 */

// creating data model that can be used for array list to transmit between adapter and activities
public class CanadianFact implements Parcelable{
    private String title;
    private String description;
    private String imageLink;


    public CanadianFact(){

    }

    public CanadianFact(Parcel parcel){
        title=parcel.readString();
        description=parcel.readString();
        imageLink=parcel.readString();
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(imageLink);


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CanadianFact> CREATOR= new Parcelable.Creator<CanadianFact>(){
        public CanadianFact createFromParcel(Parcel parcel){
            return new CanadianFact(parcel);
        }
        public CanadianFact[] newArray(int size){
            return new CanadianFact[size];
        }

    };
}
