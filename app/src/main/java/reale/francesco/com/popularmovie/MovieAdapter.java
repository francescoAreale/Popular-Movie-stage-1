package reale.francesco.com.popularmovie;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;

import reale.francesco.com.popularmovie.databinding.ListItemMovieBinding;

public class MovieAdapter extends  RecyclerView.Adapter<MovieAdapter.MovieViewHolder>  {

    final private MovieClickListner clickListner;

    private ArrayList<Movie> ListOfMovie;

    public interface MovieClickListner {
        void onGridMovieClick(Movie clickedFilm);
    }

    MovieAdapter(MovieClickListner clickListner){
        this.clickListner = clickListner;
    }
    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemMovieBinding bind = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_movie,
                        parent,
                        false);
        // we have just one layout so we inflate just that layout on the View
        return new MovieViewHolder(bind);
    }

/**
 * OnBindViewHolder is called by the RecyclerView to display the data at the specified
 * position. In this method, we update the contents of the ViewHolder to display the correct
 * indices in the list for this particular position, using the "position" argument that is conveniently
 * passed into us.
 * */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie tmp = ListOfMovie.get(position); // get the element from the list
        holder.bindMovie(tmp);  // ok let's bind the movie contained a the sepcified position
    }

    @Override
    public int getItemCount() {
       if(ListOfMovie == null)
           return 0;
       else
           return ListOfMovie.size();
    }

    public void setList( ArrayList<Movie> passedList) {
        this.ListOfMovie = passedList;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private ListItemMovieBinding bind;

       public MovieViewHolder(ListItemMovieBinding bind) {

           super(bind.getRoot());
           this.bind = bind;
           bind.getRoot().setOnClickListener(this);
           bind.getRoot().setFocusable(true);
       }

       void bindMovie(Movie selectedMovie) {
           bind.setMovie(selectedMovie);

        // bind the layout with the actual movie selected
           if (selectedMovie.getThmbnail() != null) {
               Picasso.with(bind.getRoot().getContext())
                       .load(selectedMovie.getFullImage())
                       .error(R.mipmap.ic_movie)
                       .placeholder(R.mipmap.ic_movie)
                       .into(bind.imageviewMovie);

           }
       }
        //make the item clickable
        @Override
        public void onClick(View v) {
            Movie clicked = bind.getMovie();
            if (clickListner != null)
                clickListner.onGridMovieClick(clicked);
        }
    }

    public void setList(final List<Movie> newList) {
        ListOfMovie = (ArrayList<Movie>) newList;
    }
}
