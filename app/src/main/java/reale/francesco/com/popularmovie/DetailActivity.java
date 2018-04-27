package reale.francesco.com.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.squareup.picasso.Picasso;

import reale.francesco.com.popularmovie.databinding.ActivityDetailBinding;


public class DetailActivity extends AppCompatActivity {

    public static String MOVIE = "MOVIE_SELECTED";

    private Movie movieSelected;
    private ActivityDetailBinding bind ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setSupportActionBar(bind.toolbar);
        Intent startIntent = getIntent();
        if (startIntent != null)
            movieSelected = startIntent.getParcelableExtra(MOVIE);
        setSupportActionBar(bind.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       fullActivityStart(movieSelected);
    }


    public void fullActivityStart(Movie movieSelected ){

        Picasso.with(this)
                .load(movieSelected.getFullImage())
                .error(R.mipmap.ic_movie)
                .placeholder(R.mipmap.ic_movie)
                .into(bind.image);
        bind.descriptionTextview.setText(movieSelected.getOverView());
        bind.releaseDateTextview.setText(movieSelected.getDateFromString());
        bind.titleTextview.setText(movieSelected.getTitle());
        bind.voteAverageTextview.setText(String.valueOf(movieSelected.getVote_avarage())+ " / 10");
        bind.toolbar.setTitle("");
    }
}
