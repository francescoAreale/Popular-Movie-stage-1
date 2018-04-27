package reale.francesco.com.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOfMovies implements Parcelable {

    private List<Movie> results;    // list of Movies for the actual page
    private int pageNum;   //  the page of the result
    @SerializedName("total_results")
    private int totalResultsNum;   // total number of results
    @SerializedName("total_pages")
    private int totalPagesNum;     // total number of pages retrived from the app

    public int getPage() {
        return pageNum;
    }

    public void setPage(int page) {
        this.pageNum = page;
    }

    public int getTotalResultsNum() {
        return totalResultsNum;
    }

    public void setTotalResultsNum(int totalResultsNum) {
        this.totalResultsNum = totalResultsNum;
    }

    public int getTotalPagesNum() {
        return totalPagesNum;
    }

    public void setTotalPagesNum(int totalPagesNum) {
        this.totalPagesNum = totalPagesNum;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public static Creator<ListOfMovies> getCREATOR() {
        return CREATOR;
    }
    

    public static final Creator<ListOfMovies> CREATOR = new Creator<ListOfMovies>() {
        @Override
        public ListOfMovies createFromParcel(Parcel in) {
            return new ListOfMovies(in);
        }

        @Override
        public ListOfMovies[] newArray(int size) {
            return new ListOfMovies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected ListOfMovies(Parcel in) {
        pageNum = in.readInt();
        totalResultsNum = in.readInt();
        totalPagesNum = in.readInt();
        results = in.createTypedArrayList(Movie.CREATOR);
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pageNum);
        dest.writeInt(totalResultsNum);
        dest.writeInt(totalPagesNum);
        dest.writeTypedList(results);
    }
}
