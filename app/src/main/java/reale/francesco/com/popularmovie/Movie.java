package reale.francesco.com.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movie implements Parcelable{


    private static final SimpleDateFormat staticDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


    @SerializedName("original_title")
    private String title;
    @SerializedName("poster_path")
    private String thmbnail;
    @SerializedName("vote_average")
    private float vote_avarage;
    @SerializedName("overview")
    private String overView;
    @SerializedName("release_date")
    private Date date_release;

    protected Movie(Parcel in) {
        title = in.readString();
        thmbnail = in.readString();
        vote_avarage = in.readFloat();
        overView = in.readString();
        setDateFromString(in.readString());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public Movie(String title, String thmbnail, float vote_avarage, String overView, Date date_release) {
        this.title = title;
        this.thmbnail = thmbnail;
        this.vote_avarage = vote_avarage;
        this.overView = overView;
        this.date_release = date_release;
    }

    public Movie() {

    }

    public void setDateFromString(String date){
        try {
            date_release = staticDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDateFromString(){
        return staticDateFormat.format(date_release);
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getThmbnail() {
        return thmbnail;
    }

    public void setThmbnail(String thmbnail) {
        this.thmbnail = thmbnail;
    }

    public float getVote_avarage() {
        return vote_avarage;
    }

    public void setVote_avarage(float vote_avarage) {
        this.vote_avarage = vote_avarage;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public Date getDate_release() {
        return date_release;
    }

    public void setDate_release(Date date_release) {
        this.date_release = date_release;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public String getFullImage() {
        return "http://image.tmdb.org/t/p/w185" + thmbnail;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(thmbnail);
        dest.writeFloat(vote_avarage);
        dest.writeString(overView);
        dest.writeString(getDateFromString());
    }
}
